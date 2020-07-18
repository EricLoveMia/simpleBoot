package cn.eric.h2.util.http;

import cn.eric.h2.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * HTTP 请求工具类
 *
 * @author YCKJ
 */
@SuppressWarnings("deprecation")
public class HttpUtils {

    private static final String CHARSET = "UTF-8";
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 20000;
    public static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {

    }

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }


    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url
     * @param headers 需要添加的httpheader参数
     * @return
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, Object> params) {


        if (url.startsWith("https://")) {
            return doGetSSL(url, headers, params);
        } else {
            return doGetHttp(url, headers, params);
        }


    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url
     * @param headers 需要添加的httpheader参数
     * @param params
     * @return
     */
    @SuppressWarnings("resource")
    public static String doGetHttp(String url, Map<String, String> headers, Map<String, Object> params) {
        HttpClient httpclient = null;
        String apiUrl = url;
        StringBuilder param = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0) {
                param.append("?");
            } else {
                param.append("&");
            }
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        String result = null;
        try {
            HttpGet httpGet = new HttpGet(apiUrl);
            //添加http headers
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, CHARSET);
            }
        } catch (IOException e) {
            LOGGER.error(StringUtil.EMPTY, e);
        }
        return result;
    }


    /**
     * 发送 SSL Get 请求（HTTPS），K-V形式
     *
     * @param url     API接口URL
     * @param headers
     * @param params  参数map
     * @return
     */
    public static String doGetSSL(String url, Map<String, String> headers, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        CloseableHttpResponse response = null;
        String apiUrl = url;
        String httpStr = null;
        StringBuilder param = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0) {
                param.append("?");
            } else {
                param.append("&");
            }
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        try {
            HttpGet httpGet = new HttpGet(apiUrl);
            httpGet.setConfig(requestConfig);
            //添加http headers
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, CHARSET);
        } catch (Exception e) {
            LOGGER.error(StringUtil.EMPTY, e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    LOGGER.error(StringUtil.EMPTY, e);
                }
            }
        }
        return httpStr;


    }


    /**
     * 发送 POST 请求（HTTP），不带输入数据
     *
     * @param url
     * @param headers 需要添加的httpheader参数
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, Map<String, Object> params) {
        if (url.startsWith("https://")) {
            return doPostSSL(url, headers, params);
        } else {
            return doPostHttp(url, headers, params);
        }


    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param apiUrl  API接口URL
     * @param headers 需要添加的httpheader参数
     * @param params  参数map
     * @return
     */
    public static String doPostHttp(String apiUrl, Map<String, String> headers, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = i.next();
                httpPost.addHeader(key, headers.get(key));
            }
        }
        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(CHARSET)));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, CHARSET);
        } catch (IOException e) {
            LOGGER.error(StringUtil.EMPTY, e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    LOGGER.error(StringUtil.EMPTY, e);
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     *
     * @param apiUrl
     * @param headers 需要添加的httpheader参数
     * @param json    json对象
     * @return
     */
    public static String doPostHttp(String apiUrl, Map<String, String> headers, Object json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = i.next();
                httpPost.addHeader(key, headers.get(key));

            }
        }
        try {
            httpPost.setConfig(requestConfig);
            //解决中文乱码问题
            StringEntity stringEntity = new StringEntity(json.toString(), CHARSET);
            stringEntity.setContentEncoding(CHARSET);
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, CHARSET);
        } catch (IOException e) {
            LOGGER.error(StringUtil.EMPTY, e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    LOGGER.error(StringUtil.EMPTY, e);
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPostSSL(String apiUrl, Map<String, String> headers, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = i.next();
                httpPost.addHeader(key, headers.get(key));

            }
        }
        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(CHARSET)));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, CHARSET);
        } catch (Exception e) {
            LOGGER.error(StringUtil.EMPTY, e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    LOGGER.error(StringUtil.EMPTY, e);
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     *
     * @param apiUrl API接口URL
     * @param json   JSON对象
     * @return
     */
    public static String doPostSSL(String apiUrl, Map<String, String> headers, Object json) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = i.next();
                httpPost.addHeader(key, headers.get(key));

            }
        }
        try {
            httpPost.setConfig(requestConfig);
            //解决中文乱码问题
            StringEntity stringEntity = new StringEntity(json.toString(), CHARSET);
            stringEntity.setContentEncoding(CHARSET);
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            LOGGER.error(StringUtil.EMPTY, e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    LOGGER.error(StringUtil.EMPTY, e);
                }
            }
        }
        return httpStr;
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        // 创建TrustManager
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        // TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext   
        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            // 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
            ctx.init(null, new TrustManager[]{xtm}, null);
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx);
            return sslsf;
        } catch (Exception e) {
            LOGGER.error(StringUtil.EMPTY, e);
        }
        return null;
    }
}