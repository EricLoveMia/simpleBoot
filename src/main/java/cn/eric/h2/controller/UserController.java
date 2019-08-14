package cn.eric.h2.controller;

import cn.eric.h2.entity.User;
import cn.eric.h2.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: controller
 * @Description: TODO
 * @company lsj
 * @date 2019/8/9 16:53
 **/
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserRepository repository;

    @GetMapping("/{id}")
    public R getUser(@PathVariable Integer id){
        User one = repository.getOne(id);
        log.info(one.toString());
        return R.ok();
    }
}
