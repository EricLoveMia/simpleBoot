package cn.eric.h2.util.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @ClassName HttpClientUtil
 * @Description: 网络请求工具类
 * @Author YCKJ2725
 * @Date 2020/5/12
 * @Version V1.0
 **/
/**
 * httpclient请求工具类
 * httpclient 连接池
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static PoolingHttpClientConnectionManager cm = null;

    private static CloseableHttpClient httpClient = null;

    // 默认content 类型
    private static final String DEFAULT_CONTENT_TYPE = "application/json";

    /**
     * 连接池设置
     */
    // 长连接时间保持设置
    private static final int HTTP_DEFAULT_KEEP_TIME = 60000;

    /*
     * 1、MaxtTotal是整个池子的大小； 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
     * MaxtTotal=400 DefaultMaxPerRoute=200
     * 而我只连接到http://sishuok.com时，到这个主机的并发最多只有200；而不是400； 而我连接到http://sishuok.com 和
     * http://qq.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；
     * 所以起作用的设置是DefaultMaxPerRoute。
     */
    private static final int DEFAULT_MAX_PER_ROUTE = 200;
    //设置连接池的大小
    private static final int MAX_TOTAL = 200;

    /**
     * 请求相关超时时间设置
     */
    //设置链接超时
    private static final int CONNECTION_TIMEOUT = 5000;
    //设置等待数据超时时间
    private static final int SOCKET_TIMEOUT = 15000;

    /**
     * 初始化连接池
     */
    private static synchronized void initPools() {
        if (httpClient == null) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
            cm.setMaxTotal(MAX_TOTAL);
            httpClient = HttpClients.custom().setKeepAliveStrategy(defaultStrategy).setConnectionManager(cm).build();
        }
    }

    /**
     * 长连接保持设置
     * Http connection keepAlive 设置
     */
    private static ConnectionKeepAliveStrategy defaultStrategy = new ConnectionKeepAliveStrategy() {
        @Override
        public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
            HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            int keepTime = HTTP_DEFAULT_KEEP_TIME;
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("format KeepAlive timeout exception, exception:" + e.toString());
                    }
                }
            }
            return keepTime * 1000;
        }
    };

    /**
     * 执行http post请求 默认采用Content-Type：application/json，Accept：application/json
     * @param uri 请求地址
     * @param data  请求数据
     * @return 返回是数据
     */
    public static String sendPost(String uri, String data) {
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        HttpEntityEnclosingRequestBase method = null;
        String responseBody = "";
        try {
            if (httpClient == null) {
                initPools();
            }
            method = (HttpEntityEnclosingRequestBase) getRequest(uri, HttpPost.METHOD_NAME, DEFAULT_CONTENT_TYPE);
            method.setEntity(new StringEntity(data));
            HttpContext context = HttpClientContext.create();
            CloseableHttpResponse httpResponse = httpClient.execute(method, context);
            httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                responseBody = EntityUtils.toString(httpEntity, "UTF-8");
            }

        } catch (Exception e) {
            if(method != null){
                method.abort();
            }
            e.printStackTrace();
            logger.error(
                    "execute post request exception, url:" + uri + ", exception:" + e.toString() + ", cost time(ms):"
                            + (System.currentTimeMillis() - startTime));
        } finally {
            if (httpEntity != null) {
                try {
                    EntityUtils.consumeQuietly(httpEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(
                            "close response exception, url:" + uri + ", exception:" + e.toString() + ", cost time(ms):"
                                    + (System.currentTimeMillis() - startTime));
                }
            }
        }
        return responseBody;
    }

    /**
     * 执行GET 请求
     * @param uri 请求链接
     * @return 请求结果
     */
    public static String sendGet(String uri) {
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        HttpRequestBase method = null;
        String responseBody = "";
        try {
            if (httpClient == null) {
                initPools();
            }
            method = getRequest(uri, HttpGet.METHOD_NAME, DEFAULT_CONTENT_TYPE);
            HttpContext context = HttpClientContext.create();
            CloseableHttpResponse httpResponse = httpClient.execute(method, context);
            httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                responseBody = EntityUtils.toString(httpEntity, "UTF-8");
                logger.info("请求信息：\n请求URL: "+uri+"\n返回状态码："+httpResponse.getStatusLine().getStatusCode()+"\n返回结果："+ responseBody);
            }
        } catch (Exception e) {
            if(method != null){
                method.abort();
            }
            e.printStackTrace();
            logger.error("execute get request exception, url:" + uri + ", exception:" + e.toString() + ",cost time(ms):"
                    + (System.currentTimeMillis() - startTime));
        } finally {
            if (httpEntity != null) {
                try {
                    EntityUtils.consumeQuietly(httpEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("close response exception, url:" + uri + ", exception:" + e.toString() + ",cost time(ms):"
                            + (System.currentTimeMillis() - startTime));
                }
            }
        }
        return responseBody;
    }


    /**
     * 创建请求
     * @param uri 请求url
     * @param methodName 请求的方法类型
     * @param contentType contentType类型
     * @return
     */
    private static HttpRequestBase getRequest(String uri, String methodName, String contentType) {
        HttpRequestBase method = null;

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_TIMEOUT).setExpectContinueEnabled(false).build();

        if (HttpPut.METHOD_NAME.equalsIgnoreCase(methodName)) {
            method = new HttpPut(uri);
        } else if (HttpPost.METHOD_NAME.equalsIgnoreCase(methodName)) {
            method = new HttpPost(uri);
        } else if (HttpGet.METHOD_NAME.equalsIgnoreCase(methodName)) {
            method = new HttpGet(uri);
        } else {
            method = new HttpPost(uri);
        }
        if (StringUtils.isBlank(contentType)) {
            contentType = DEFAULT_CONTENT_TYPE;
        }
        method.addHeader("Content-Type", contentType);
        method.addHeader("Accept", contentType);
        method.setConfig(requestConfig);
        return method;
    }

    public static CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public static PoolingHttpClientConnectionManager getHttpConnectionManager() {
        return cm;
    }
}

