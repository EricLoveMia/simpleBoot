package cn.eric.h2.config.http;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author YCKJ2351
 * @Date 2020/7/17 10:26
 * @Description restTemplate 调优
 */
@Configuration
@ConfigurationProperties(prefix = "cw.gateway.route.feign")
public class RestTemplatePerties {

    /**
     * 长连接保持时长 s
     */
    private long timeToLive = 30L;

    /**
     * 最大连接数
     */
    private Integer poolMaxTotal = 2000;

    /**
     * 单路由的并发数
     */
    private Integer poolDefaultMaxPerRoute = 50;

    /**
     * 重试开关
     */
    private Boolean requestSentRetryEnabled = true;

    /**
     * 重试次数
     */
    private Integer requestSentretryCount = 2;

    /**
     * RequestConfig socket超时
     */
    private Integer requestConfigSocketTimeout = 60000;

    /**
     * RequestConfig socketconnect超时
     */
    private Integer requestConfigConnectTimeout = 50000;

    /**
     * HttpComponentsClientHttpRequestFactory readtimeout ms
     */
    private Integer requestFactoryReadTimeout = 50000;

    /**
     * HttpComponentsClientHttpRequestFactory ConnectTimeout ms
     */
    private Integer requestFactoryConnectTimeout = 50000;

    public Boolean getRequestSentRetryEnabled() {
        return requestSentRetryEnabled;
    }

    public void setRequestSentRetryEnabled(Boolean requestSentRetryEnabled) {
        this.requestSentRetryEnabled = requestSentRetryEnabled;
    }

    public Integer getRequestSentretryCount() {
        return requestSentretryCount;
    }

    public void setRequestSentretryCount(Integer requestSentretryCount) {
        this.requestSentretryCount = requestSentretryCount;
    }

    public Integer getRequestConfigSocketTimeout() {
        return requestConfigSocketTimeout;
    }

    public void setRequestConfigSocketTimeout(Integer requestConfigSocketTimeout) {
        this.requestConfigSocketTimeout = requestConfigSocketTimeout;
    }

    public Integer getRequestConfigConnectTimeout() {
        return requestConfigConnectTimeout;
    }

    public void setRequestConfigConnectTimeout(Integer requestConfigConnectTimeout) {
        this.requestConfigConnectTimeout = requestConfigConnectTimeout;
    }

    public Integer getRequestFactoryReadTimeout() {
        return requestFactoryReadTimeout;
    }

    public void setRequestFactoryReadTimeout(Integer requestFactoryReadTimeout) {
        this.requestFactoryReadTimeout = requestFactoryReadTimeout;
    }

    public Integer getRequestFactoryConnectTimeout() {
        return requestFactoryConnectTimeout;
    }

    public void setRequestFactoryConnectTimeout(Integer requestFactoryConnectTimeout) {
        this.requestFactoryConnectTimeout = requestFactoryConnectTimeout;
    }

    public Long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public Integer getPoolMaxTotal() {
        return poolMaxTotal;
    }

    public void setPoolMaxTotal(Integer poolMaxTotal) {
        this.poolMaxTotal = poolMaxTotal;
    }

    public Integer getPoolDefaultMaxPerRoute() {
        return poolDefaultMaxPerRoute;
    }

    public void setPoolDefaultMaxPerRoute(Integer poolDefaultMaxPerRoute) {
        this.poolDefaultMaxPerRoute = poolDefaultMaxPerRoute;
    }
}
