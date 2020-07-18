package cn.eric.h2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2020/5/19
 * @Version V1.0
 **/
@RestController
@RequestMapping("/hello")
public class HelloController {

    /**
     * 人脸图片最大为默认60K. 超过就压缩
     */
    @Value("{eric.max:61440}")
    private static Integer max;

    @GetMapping("/sayHello")
    public R sayHello(){
        System.out.println("hello" + max);
        testStatic();
        return R.ok("hello");
    }

    @GetMapping("/sayHi")
    public R sayHi(){
        System.out.println("hi");
        return R.ok("hi");
    }

    public static void testStatic(){
        System.out.println(max);
    }
}
