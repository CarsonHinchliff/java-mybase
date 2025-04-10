package cn.strivers.gateway.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.strivers.gateway.utils.JWTUtils;
import cn.strivers.gateway.utils.WebFluxUtils;
import cn.strivers.mybase.core.result.ResultCode;
import cn.strivers.mybase.core.utils.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;


@Slf4j
public class TokenGatewayFilter implements GlobalFilter {

    @Autowired
    private MybaseConf mybaseConf;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private AuthSkip authSkip;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //获取请求的Url
        URI uri = request.getURI();
        String url = uri.getPath();
        String clientInfo = request.getHeaders().getFirst("clientInfo");
        log.debug("请求接口：{}，clientInfo：{}", url, clientInfo);
        //校正tenant
        Map<String, String> tenant = mybaseConf.getTenant();
        if (MapUtil.isNotEmpty(tenant)) {
            String domain = uri.getHost();
            int tenantId = Convert.toInt(tenant.get(domain), 0);
            if (tenantId > 0 && StrUtil.isNotBlank(clientInfo)) {
                clientInfo = clientInfo.replaceAll("t\\d", "t" + tenantId);
            }
        }
        //跳过的接口
        if (isSkipUrl(url, request)) {
            if (StrUtil.isEmpty(clientInfo)) {
                //未传clientInfo，则默认为三方请求
                ServerHttpRequest.Builder builder = request.mutate();
                builder.header("clientInfo", "c9-" + System.currentTimeMillis());
                return chain.filter(exchange.mutate().request(builder.build()).build());
            }
            return chain.filter(exchange);
        }
        //校验token
        return checkToken(exchange, chain, url, clientInfo);
    }

    /**
     * 跳过接口
     *
     * @param url
     * @param request
     * @return
     */
    private boolean isSkipUrl(String url, ServerHttpRequest request) {
        String[] split = url.split("/");
        String ipAddress = getIpAddr(request);
        String interfaceName = split[split.length - 1].split("\\?")[0];
        String contextPath = split[1];
        log.info("请求:ip:{},url:{},{},{}", ipAddress, url, interfaceName, contextPath);
        String interfaceNames;
        if ("127.0.0.1".equals(ipAddress) || ipAddress.startsWith("10.") || ipAddress.startsWith("172.") || ipAddress.startsWith("192.168")) {
            interfaceNames = Convert.toStr(authSkip.getLan().get(contextPath));
        } else {
            interfaceNames = Convert.toStr(authSkip.getWan().get(contextPath));
        }
        if (StrUtil.isBlank(interfaceNames)) {
            return false;
        }
        return interfaceNames.contains(interfaceName);
    }

    /**
     * 检查token
     *
     * @param exchange
     * @param requestUrl
     * @return
     */
    private Mono<Void> checkToken(ServerWebExchange exchange, GatewayFilterChain chain, String requestUrl, String clientInfo) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("token");
        try {
            if (StrUtil.isBlank(token)) {
                log.error("无效token：{} {}", requestUrl, clientInfo);
                return WebFluxUtils.resultMessage(exchange, ResultCode.ERROR_A1008);
            }
            String verifyToken = JWTUtils.verifyToken(token);
            if (StringUtils.isBlank(verifyToken)) {
                return WebFluxUtils.resultMessage(exchange, ResultCode.ERROR_A1008);
            }

            //从token中解析用户信息并设置到Header中去
            ServerHttpRequest newRequest = WebFluxUtils.getNewHttpRequest(request, WebFluxUtils.getNewHttpHeadersConsumer(token));
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);
        } catch (Exception e) {
            log.error("检查token方法错误:" + e.getMessage());
            return WebFluxUtils.resultMessage(exchange, ResultCode.ERROR_A1008);
        }
    }


    /**
     * 获取真实ip
     *
     * @param request
     * @return
     */
    private String getIpAddr(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }

}