package cn.eric.h2.config.http;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName RestTemplateConfig
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2020/7/15
 * @Version V1.0
 **/
//@Configuration
public class RestTemplateConfig {

    @Autowired
    RestTemplatePerties restTemplatePerties;

    @Bean
    public RestTemplate restTemplate() {
        // 长连接保持时长30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(
                restTemplatePerties.getTimeToLive(), TimeUnit.SECONDS);
        // 最大连接数
        pollingConnectionManager.setMaxTotal(restTemplatePerties.getPoolMaxTotal());
        // 单路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(restTemplatePerties.getPoolDefaultMaxPerRoute());
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // 重试次数2次，并开启
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(restTemplatePerties.getRequestSentretryCount(),
                restTemplatePerties.getRequestSentRetryEnabled()));
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        httpClientBuilder.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);
        /** RequestConfig 配置 **/
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(restTemplatePerties.getRequestConfigSocketTimeout()) //ms
                .setConnectTimeout(restTemplatePerties.getRequestConfigConnectTimeout())
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);

        HttpClient httpClient = httpClientBuilder.build();
        // httpClient连接底层配置clientHttpRequestFactory
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
//        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        OkHttp3ClientHttpRequestFactory clientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(restTemplatePerties.getRequestFactoryReadTimeout());// ms
//        clientHttpRequestFactory.setConnectionRequestTimeout(20000);
        clientHttpRequestFactory.setConnectTimeout(restTemplatePerties.getRequestFactoryConnectTimeout());//
        clientHttpRequestFactory.setBufferRequestBody(false);

        return new RestTemplate(clientHttpRequestFactory);
    }





}
