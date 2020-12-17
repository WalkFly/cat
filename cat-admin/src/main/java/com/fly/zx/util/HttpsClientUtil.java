/**
 * @name: HttpsClientUtil.java
 * @copyright: soyea©2019
 * @description:
 * @author: ywm
 * @createtime: 2019/1/10 14:32
 */

package com.fly.zx.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @name HttpsClientUtil
 * @description
 * @auther ywm
 * @see
 * @since
 */
@SuppressWarnings("deprecation")
@Component
public class HttpsClientUtil {


    public HttpsClientUtil() {

    }

    public static CloseableHttpClient createDefault() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        /*cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        cm.setDefaultMaxPerRoute(50);*/
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    public static CloseableHttpClient createBasicAuthDefault(CredentialsProvider credentialsProvider) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        /*cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        cm.setDefaultMaxPerRoute(50);*/
        return HttpClients.custom().setConnectionManager(cm).setDefaultCredentialsProvider(credentialsProvider).build();
    }


    public static HttpsClientUtil getHttpClient(){
        return  new HttpsClientUtil(0 , null , null , null ,null);
    }

    public HttpsClientUtil(int sslInitWay, String keyStoreFile, String keyStorePass, String trustStoreFile, String trustStorePass) {
        this.sslInitWay = sslInitWay;
        this.keyStoreFile = keyStoreFile;
        this.keyStorePass = keyStorePass;
        this.trustStoreFile = trustStoreFile;
        this.trustStorePass = trustStorePass;
        if (0 == sslInitWay) {
            httpClient = createDefault();
//            httpClient = HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();
        } else if (1 == sslInitWay) {
            httpClient = createIgnoreVerifySSL();
        } else if (2 == sslInitWay) {
            httpClient = initOneWay();
        } else if (3 == sslInitWay) {
            httpClient = initTwoWay();
        }
    }

    public final static String CONTENT_LENGTH = "Content-Length";



    // 0: 普通的http连接；1-忽略SSL认证的https连接；2-单向自信任的https连接；3-双向https连接
    private int sslInitWay = 0;

    // 密钥库文件路径
    private String keyStoreFile;

    // 密钥库文件密码
    private String keyStorePass;

    // 信任库文件路径
    private String trustStoreFile;

    // 信任库文件路径
    private String trustStorePass;

    private CloseableHttpClient httpClient;

    public HttpsClientUtil(int sslInitWay) {
        this.sslInitWay = sslInitWay;
    }

    public String getKeyStoreFile() {
        return keyStoreFile;
    }

    public void setKeyStoreFile(String keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
    }

    public String getKeyStorePass() {
        return keyStorePass;
    }

    public void setKeyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass;
    }

    public String getTrustStoreFile() {
        return trustStoreFile;
    }

    public void setTrustStoreFile(String trustStoreFile) {
        this.trustStoreFile = trustStoreFile;
    }

    public String getTrustStorePass() {
        return trustStorePass;
    }

    public void setTrustStorePass(String trustStorePass) {
        this.trustStorePass = trustStorePass;
    }

    /**
     * 模拟请求
     *
     * @param url      资源地址
     * @param map
     * @param encoding 编码参数列表
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public String postMap(String url, Map<String, String> headerMap, Map<String, String> map, String encoding) {
        //新建client
        CloseableHttpClient client = createDefault();
        String body = "";

        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        CloseableHttpResponse response = null;

        try {
            //装填参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }

            //设置参数到请求对象中
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

            addRequestHeader(httpPost, headerMap);

            //执行请求操作，并拿到结果（同步阻塞）
            response = client.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, encoding);
                }
                EntityUtils.consume(entity);

                //释放链接
                response.close();
            }
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != client) {
                        httpPost.releaseConnection();
//                        client.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String postBasicAuthMap(String url, Map<String, String> headerMap, Map<String, String> map, String encoding , String userName , String pass) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 设置BasicAuth
        CredentialsProvider provider = new BasicCredentialsProvider();
        // Create the authentication scope
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        // Create credential pair，在此处填写用户名和密码
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, pass);
        // Inject the credentials
        provider.setCredentials(scope, credentials);
        // Set the default credentials provider
        httpClientBuilder.setDefaultCredentialsProvider(provider);

        //新建client
        CloseableHttpClient client = httpClientBuilder.build();
        String body = "";

        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        CloseableHttpResponse response = null;

        try {
            //装填参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }

            //设置参数到请求对象中
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

            addRequestHeader(httpPost, headerMap);

            //执行请求操作，并拿到结果（同步阻塞）
            response = client.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, encoding);
                }
                EntityUtils.consume(entity);

                //释放链接
                response.close();
            }
            return body;
        } catch (Exception e) {
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != client) {
                        httpPost.releaseConnection();
//                        client.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "error";
    }


        /**
         * get请求，参数拼接在地址上
         *
         * @param url       请求地址加参数
         * @param headerMap 请求头参数
         * @param encoding  编码
         * @return 响应
         */
    public String get(String url, Map<String, String> headerMap, String encoding) {
        //新建client
        CloseableHttpClient client = createDefault();
        String body = null;
        HttpGet httpGet = new HttpGet(url);
        addRequestHeader(httpGet, headerMap);
        CloseableHttpResponse response = null;
        try {
            //执行请求操作，并拿到结果（同步阻塞）
            response = client.execute(httpGet);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, encoding);
                }
                EntityUtils.consume(entity);

                //释放链接
                response.close();
            }
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != client) {

                        client.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * get请求，参数放在map里
     *
     * @param url       请求地址
     * @param headerMap 请求头参数
     * @param paramMap  请求参数
     * @param encoding  编码
     * @return 响应
     */
    public String getMap(String url, Map<String, String> headerMap, Map<String, String> paramMap, String encoding) {
        //新建client
        CloseableHttpClient client = createDefault();
        String body = null;
        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            builder.setParameters(pairs);
            HttpGet httpGet = new HttpGet(builder.build());
            addRequestHeader(httpGet, headerMap);
            response = client.execute(httpGet);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, encoding);
                }
                EntityUtils.consume(entity);

                //释放链接
                response.close();
            }
            return body;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != client) {
                        client.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * post请求，参数为json字符串
     *
     * @param url        请求地址
     * @param headerMap  请求头参数
     * @param jsonString json字符串
     * @param encoding   编码
     * @return 响应
     */
    public String postJson(String url, Map<String, String> headerMap, String jsonString, String encoding) throws IOException {
        //新建client
        CloseableHttpClient client = createDefault();
        String body = null;
        CloseableHttpResponse response = null;
        HttpPost post=null;
        try {
            post = new HttpPost(url);
            addRequestHeader(post, headerMap);
            post.addHeader("Content-Type", "application/json");
            post.setEntity(new ByteArrayEntity(jsonString.getBytes(encoding)));
            response = client.execute(post);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, encoding);
                }
                EntityUtils.consume(entity);
            }else if((response != null && response.getStatusLine().getStatusCode() != 200)){
                return response.getStatusLine().getStatusCode()+response.getStatusLine().getReasonPhrase();
            }
            return body;
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != client) {
                        post.releaseConnection();
                        client.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String postBasicAuthJson(String url, Map<String, String> headerMap, String jsonString, String encoding , String userName , String pass){

        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials(userName, pass);
        provider.setCredentials(AuthScope.ANY, credentials);

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        //新建client
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).setDefaultCredentialsProvider(provider).build();


        String body = null;
        CloseableHttpResponse response = null;
        HttpPost post=null;
        try {
            post = new HttpPost(url);
            addRequestHeader(post, headerMap);
            post.addHeader("Content-Type", "application/json");
            post.setEntity(new ByteArrayEntity(jsonString.getBytes(encoding)));
            response = client.execute(post);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, encoding);
                }
                EntityUtils.consume(entity);
            }
            return body;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != client) {
                        post.releaseConnection();
                        client.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static CloseableHttpClient createIgnoreVerifySSL() {

        try {
            SSLContext sc = SSLContext.getInstance("SSLv3");

            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                        String paramString) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                        String paramString) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sc.init(null, new TrustManager[]{trustManager}, null);

            //创建自定义的httpclient对象
            CloseableHttpClient client = createHttpClient(sc);

            return client;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static CloseableHttpClient createHttpClient(SSLContext sc) {
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sc, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();

        return client;
    }

    /**
     * 设置信任自签名证书（单向认证的HTTPS连接，证书由自己创建）
     *
     * @return
     */
    public CloseableHttpClient initOneWay() {
        SSLContext sc = null;
        FileInputStream instream = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            instream = new FileInputStream(new File(this.keyStoreFile));
            trustStore.load(instream, this.keyStorePass.toCharArray());

            // 相信自己的CA和所有自签名的证书
            sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();

            //创建自定义的httpclient对象
            CloseableHttpClient client = createHttpClient(sc);

            return client;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | KeyManagementException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                instream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 设置信任自签名证书（双向认证的HTTPS连接）
     *
     * @return
     * @throws Exception
     */
    public CloseableHttpClient initTwoWay() {

        FileInputStream ksis = null;
        FileInputStream tsis = null;
        try {

            ksis = new FileInputStream(new File(this.keyStoreFile));
            tsis = new FileInputStream(new File(this.trustStoreFile));

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(ksis, this.keyStorePass.toCharArray());

            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(tsis, this.trustStorePass.toCharArray());

            // 相信自己的CA和所有自签名的证书
            SSLContext sc = SSLContexts.custom().loadKeyMaterial(ks, keyStorePass.toCharArray()).loadTrustMaterial(ts, new TrustSelfSignedStrategy()).build();

            //创建自定义的httpclient对象
            CloseableHttpClient client = createHttpClient(sc);

            return client;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                ksis.close();
                tsis.close();
            } catch (IOException e) {
            }
        }
    }

    private static void addRequestHeader(HttpUriRequest request, Map<String, String> headerMap) {

        if (headerMap == null) {
            return;
        }

        for (String headerName : headerMap.keySet()) {
            if (CONTENT_LENGTH.equalsIgnoreCase(headerName)) {
                continue;
            }

            String headerValue = headerMap.get(headerName);
            request.addHeader(headerName, headerValue);
        }
    }

    private String entityToString(HttpEntity entity) throws IOException {
        String result = null;
        if (entity != null) {
            long lenth = entity.getContentLength();
            if (lenth != -1 && lenth < 2048) {
                result = EntityUtils.toString(entity, "UTF-8");
            } else {
                InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while ((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = buffer.toString();
            }
        }
        return result;
    }


}
