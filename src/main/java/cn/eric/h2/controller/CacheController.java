package cn.eric.h2.controller;

import cn.eric.h2.entity.User;
import cn.eric.h2.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: CacheController
 * @Description: TODO
 * @company lsj
 * @date 2019/8/9 17:43
 **/
@RestController
@RequestMapping("/cache")
public class CacheController {

    @Resource
    private UserRepository repository;

    @RequestMapping("/listCustomers")
    @Cacheable(value = "listCustomers" , key = "#length", sync = true)
    public List<User> listCustomers(Long length){

        List<User> users = repository.findAll();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return users;
    }


}
