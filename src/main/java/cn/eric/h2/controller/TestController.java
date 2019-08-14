package cn.eric.h2.controller;

import cn.eric.h2.cache.Cache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: TestController
 * @Description: TODO
 * @company lsj
 * @date 2019/8/13 17:09
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/cacheWrite")
    public R testCacheWrite(){
        for (int i = 0; i < 1000; i++) {
            Cache.put("a"+i,"data_a"+i,true);
        }
        return R.ok();
    }

    @GetMapping("/getCache/{key}")
    public R testCacheWrite(@PathVariable String key){
        Object o = Cache.get(key);
        return R.ok(o.toString());
    }
}
