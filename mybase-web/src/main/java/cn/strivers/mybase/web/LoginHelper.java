package cn.strivers.mybase.web;

import cn.strivers.mybase.web.pojo.dto.LoginDTO;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;

@Data
public class LoginHelper {

    private static final TransmittableThreadLocal<LoginDTO> LOGIN_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static LoginDTO getLogin() {
        return LOGIN_THREAD_LOCAL.get();
    }

    public static void setLogin(LoginDTO loginDTO) {
        LOGIN_THREAD_LOCAL.set(loginDTO);
    }

    public static void removeLogin() {
        LOGIN_THREAD_LOCAL.remove();
    }

    public static boolean hasLogin() {
        return LOGIN_THREAD_LOCAL.get() != null;
    }
}
