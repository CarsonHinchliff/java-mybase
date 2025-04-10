package demo.cn.strivers.mybase.web.controller;

import cn.strivers.mybase.web.util.LocaleMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BaseController Created by liteng.
 *
 * @author liteng
 * @date 2019/04/03 10:29
 */
public class BaseController {

    @Autowired
    LocaleMessageUtil localeMessageUtil;

    public String msg(String msg) {
        return localeMessageUtil.getMessage(msg);
    }

}
