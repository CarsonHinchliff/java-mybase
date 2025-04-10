package demo.cn.strivers.mybase.web.config;

import cn.hutool.json.JSONUtil;
import cn.strivers.mybase.core.result.Result;
import cn.strivers.mybase.core.result.ResultCode;
import cn.strivers.mybase.core.utils.StrUtil;
import cn.strivers.mybase.web.LoginHelper;
import cn.strivers.mybase.web.pojo.dto.LoginDTO;
import demo.cn.strivers.mybase.web.tenant.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        String requestUrl = request.getRequestURI();
        String clientInfo = request.getHeader("clientInfo");
        log.debug("此次访问的接口[{}]：{}", clientInfo, requestUrl);
        if (StrUtil.isEmpty(clientInfo)) {
            return setResultMessage(response, ResultCode.ERROR_A1001);
        }
        LoginDTO login = LoginHelper.getLogin();
        int clientType = login.getClientType();
        int tenantId = login.getTenantId();
        if (clientType != 9 && Tenant.schemaMap != null && !Tenant.schemaMap.containsKey(tenantId)) {
            return setResultMessage(response, ResultCode.ERROR_A1011);
        }
        return true;
    }

    public static Boolean setResultMessage(HttpServletResponse response, ResultCode resultCode) {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        ServletOutputStream writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getOutputStream();
            writer.write(JSONUtil.toJsonStr(Result.fail(resultCode)).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
