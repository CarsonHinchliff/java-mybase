package cn.strivers.gateway.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.strivers.gateway.filter.GlobalCacheRequestFilter;
import cn.strivers.mybase.core.result.Result;
import cn.strivers.mybase.core.result.ResultCode;
import cn.strivers.mybase.core.utils.GsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

/**
 * WebFlux 工具类
 *
 * @author mozhu
 * @date 2023-03-20 14:31:45
 */
public class WebFluxUtils {

    /**
     * 获取原请求路径
     */
    public static String getOriginalRequestUrl(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        LinkedHashSet<URI> uris = exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR, new LinkedHashSet<>());
        URI requestUri = uris.stream().findFirst().orElse(request.getURI());
        return UriComponentsBuilder.fromPath(requestUri.getRawPath()).build().toUriString();
    }

    /**
     * 是否是Json请求
     *
     * @param exchange HTTP请求
     */
    public static boolean isJsonRequest(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        return StringUtils.startsWithIgnoreCase(header, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 读取request内的body
     * <p>
     * 注意一个request只能读取一次 读取之后需要重新包装
     */
    public static String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        // 获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }

    /**
     * 从缓存中读取request内的body
     * <p>
     * 注意要求经过 {@link ServerWebExchangeUtils#cacheRequestBody(ServerWebExchange, Function)} 此方法创建缓存
     * 框架内已经使用 {@link GlobalCacheRequestFilter} 全局创建了body缓存
     *
     * @return body
     */
    public static String resolveBodyFromCacheRequest(ServerWebExchange exchange) {
        Object obj = exchange.getAttributes().get(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        DataBuffer buffer = (DataBuffer) obj;
        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
        return charBuffer.toString();
    }

    /**
     * 返回新的request
     *
     * @param httpRequest
     * @param httpHeadersConsumer
     * @return
     */
    public static ServerHttpRequest getNewHttpRequest(ServerHttpRequest httpRequest, Consumer<HttpHeaders> httpHeadersConsumer) {
        return httpRequest.mutate()
                .headers(httpHeadersConsumer)
                .build();
    }

    /**
     * 组装透传数据
     *
     * @param token token
     * @return
     */
    public static Consumer<HttpHeaders> getNewHttpHeadersConsumer(String token) {
        return headers -> {
            headers.set("token", token);
        };
    }

    /**
     * 统一返回错误信息的处理
     *
     * @return
     */
    public static Mono<Void> resultMessage(ServerWebExchange exchange, ResultCode resultCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        HttpHeaders headers = response.getHeaders();
        List<String> contentType = new ArrayList<>();
        contentType.add("application/json; charset=utf-8");
        headers.put("Content-Type", contentType);
        DataBuffer dataBuffer = response.bufferFactory().wrap(GsonUtil.toJsonString(Result.fail(resultCode.getCode(), resultCode.getMessage())).getBytes());
        return response.writeWith(Flux.just(dataBuffer));
    }
}
