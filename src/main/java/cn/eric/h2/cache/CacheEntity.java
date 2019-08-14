package cn.eric.h2.cache;

import java.io.Serializable;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: CacheEntity
 * @Description: TODO
 * @company lsj
 * @date 2019/8/13 16:18
 **/
public class CacheEntity implements Serializable {
    private String key;

    //键值对的value
    private Object value;

    private long expire;

    public CacheEntity(){

    }

    public CacheEntity(String key, Object value, long expire) {
        this.key = key;
        this.value = value;
        if(expire == -1){
            this.expire = -1;
        }else {
            this.expire = System.currentTimeMillis() + expire;
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
