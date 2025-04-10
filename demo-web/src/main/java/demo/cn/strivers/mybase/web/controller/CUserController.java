package demo.cn.strivers.mybase.web.controller;

import cn.strivers.mybase.core.result.Result;
import cn.strivers.mybase.core.utils.ValidatorUtil;
import demo.cn.strivers.mybase.web.entity.CUser;
import demo.cn.strivers.mybase.web.service.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CUserController {

    @Autowired
    CUserService cUserService;

    @PostMapping("/testI18n")
    public Result test() {
        return Result.success();
    }

    @RequestMapping(value = "/findUserById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Result findUserById(@RequestBody CUser user) {
        Long id = user.getUserId();
        ValidatorUtil.validateNotEmpty(id, "id必传");
        return Result.success(cUserService.findByUserId(id));
    }
}
