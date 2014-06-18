package com.joymetec.push;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Network {
    private static final String LogTag = "com.xiaomi.common.Network";

    /**
     * user agent for chrome browser on PC
     */
    public static final String UserAgent_PC_Chrome_6_0_464_0 = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.464.0 Safari/534.3";

    public static final String UserAgent_PC_Chrome = UserAgent_PC_Chrome_6_0_464_0;

    public static final String CMWAP_GATEWAY = "10.0.0.172";

    public static final int CMWAP_PORT = 80;

    public static final String CMWAP_HEADER_HOST_KEY = "X-Online-Host";

    public static final String USER_AGENT = "User-Agent";

    public static final int CONNECTION_TIMEOUT = 10 * 1000;

    public static final int READ_TIMEOUT = 15 * 1000;

    public static final String NETWORK_TYPE_3GWAP = "3gwap";

    public static final String NETWORK_TYPE_3GNET = "3gnet";

    public static final String NETWORK_TYPE_WIFI = "wifi";

    public static final String NETWORK_TYPE_CHINATELECOM = "#777";

    public static InputStream downloadXmlAsStream(Context context, URL url) throws IOException {
        return downloadXmlAsStream(context, url, true, null, null, null, null);
    }

    public static InputStream downloadXmlAsStream(Context context, URL url, boolean noEncryptUrl,
            String userAgent, String cookie) throws IOException {
        return downloadXmlAsStream(context, url, noEncryptUrl, userAgent, cookie, null, null);
    }

    /**
     * 閸栧懓顥�HTTP request/response 閻ㄥ嫯绶熼崝鈺佸毐閺侊拷     *
     * @param context 鎼存梻鏁ょ粙瀣碍娑撳﹣绗呴弬锟�    * @param url HTTP閸︽澘娼�     * @param noEncryptUrl 閺勵垰鎯侀崝鐘茬槕
     * @param userAgent
     * @param cookie
     * @param requestHdrs 閻劋绨导鐘插弳闂勵槯serAgent閸滃畱ookie娑斿顧囬惃鍕従娴犳潑eader info
     * @param responseHdrs 鏉╂柨娲栭惃鍑TP response headers;
     * @return
     * @throws IOException
     */
    public static InputStream downloadXmlAsStream(
    /* in */Context context,
    /* in */URL url, boolean noEncryptUrl, String userAgent, String cookie,
            Map<String, String> requestHdrs,
            /* out */HttpHeaderInfo responseHdrs) throws IOException {
        if (null == context)
            throw new IllegalArgumentException("context");
        if (null == url)
            throw new IllegalArgumentException("url");

        URL newUrl = url;
        if (!noEncryptUrl)
            newUrl = new URL(encryptURL(url.toString()));

        InputStream responseStream = null;
        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection conn = getHttpUrlConnection(context, newUrl);
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);
        if (!TextUtils.isEmpty(userAgent)) {
            conn.setRequestProperty(Network.USER_AGENT, userAgent);
        }
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        if (null != requestHdrs) {
            for (String key : requestHdrs.keySet()) {
                conn.setRequestProperty(key, requestHdrs.get(key));
            }
        }

        if ((responseHdrs != null)
                && (url.getProtocol().equals("http") || url.getProtocol().equals("https"))) {
            responseHdrs.ResponseCode = conn.getResponseCode();
            if (responseHdrs.AllHeaders == null)
                responseHdrs.AllHeaders = new HashMap<String, String>();
            for (int i = 0;; i++) {
                String name = conn.getHeaderFieldKey(i);
                String value = conn.getHeaderField(i);

                if (name == null && value == null)
                    break;
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(value))
                    continue;
                responseHdrs.AllHeaders.put(name, value);
            }
        }

        responseStream = new DoneHandlerInputStream(conn.getInputStream());
        return responseStream;
    }

    public static InputStream downloadXmlAsStreamWithoutRedirect(URL url, String userAgent,
            String cookie) throws IOException {
        InputStream responseStream = null;
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);
        if (!TextUtils.isEmpty(userAgent)) {
            conn.setRequestProperty(Network.USER_AGENT, userAgent);
        }
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }

        int resCode = conn.getResponseCode();
        if (resCode < 300 || resCode >= 400) {
            responseStream = conn.getInputStream();
        }
        return new DoneHandlerInputStream(responseStream);
    }

    public static String downloadXml(Context context, URL url) throws IOException {
        return downloadXml(context, url, false, null, "UTF-8", null);
    }

    public static String downloadXml(Context context, URL url, boolean noEncryptUrl,
            String userAgent, String encoding, String cookie) throws IOException {
        InputStream responseStream = null;
        StringBuilder sbReponse;
        try {
            responseStream = downloadXmlAsStream(context, url, noEncryptUrl, userAgent, cookie);
            sbReponse = new StringBuilder(1024);
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream,
                    encoding), 1024);
            String line;
            while (null != (line = reader.readLine())) {
                sbReponse.append(line);
                sbReponse.append("\r\n");
            }
        } finally {
            if (null != responseStream) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    Log.e(LogTag, "Failed to close responseStream" + e.toString());
                }
            }
        }

        String responseXml = sbReponse.toString();
        return responseXml;
    }

    public static String downloadXml(Context context, URL url, String userAgent, String cookie,
            Map<String, String> requestHdrs, HttpHeaderInfo response) throws IOException {
        InputStream responseStream = null;
        StringBuilder sbReponse;
        try {
            responseStream = downloadXmlAsStream(context, url, true, userAgent, cookie,
                    requestHdrs, response);
            sbReponse = new StringBuilder(1024);
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream,
                    "UTF-8"), 1024);
            String line;
            while (null != (line = reader.readLine())) {
                sbReponse.append(line);
                sbReponse.append("\r\n");
            }
        } finally {
            if (null != responseStream) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    Log.e(LogTag, "Failed to close responseStream" + e.toString());
                }
            }
        }

        String responseXml = sbReponse.toString();
        return responseXml;
    }

    /**
     * Based on the doc at
     * "http://diveintomark.org/archives/2004/02/13/xml-media-types" RFC 3023
     * (XML Media Types) defines the interaction between XML and HTTP as it
     * relates to character encoding. HTTP uses MIME to define a method of
     * specifying the character encoding, as part of the Content-Type HTTP
     * header, which looks like this: Content-Type: text/html; charset="utf-8"
     * If no charset is specified, HTTP defaults to iso-8859-1, but only for
     * text/* media types. (Thanks, Ian.) For other media types, the default
     * encoding is undefined, which is where RFC 3023 comes in. In XML, the
     * character encoding is optional and can be given in the XML declaration in
     * the first line of the document, like this: <xml version="1.0"
     * encoding="iso-8859-1"?> If no encoding is given and no Byte Order Mark is
     * present (don閳ユ獩 ask), XML defaults to utf-8. (For those of you smart
     * enough to realize that this is a Catch-22, that an XML processor can閳ユ獩
     * possibly read the XML declaration to determine the document閳ユ獨 character
     * encoding without already knowing the document閳ユ獨 character encoding,
     * please read Section F of the XML specification and bow in awe at the
     * intricate care with which this issue was thought out.) According to RFC
     * 3023, if the media type given in the Content-Type HTTP header is
     * application/xml, application/xml-dtd,
     * application/xml-external-parsed-entity, or any one of the subtypes of
     * application/xml such as application/atom+xml or application/rss+xml or
     * even application/rdf+xml, then the encoding is: 1. the encoding given in
     * the charset parameter of the Content-Type HTTP header, 2. or the encoding
     * given in the encoding attribute of the XML declaration within the
     * document, 3. or utf-8. On the other hand, if the media type given in the
     * Content-Type HTTP header is text/xml, text/xml-external-parsed-entity, or
     * a subtype like text/AnythingAtAll+xml, then the encoding attribute of the
     * XML declaration within the document is ignored completely, and the
     * encoding is 1. the encoding given in the charset parameter of the
     * Content-Type HTTP header, 2. or us-ascii.
     *
     * @param url
     * @param userAgent
     * @return
     * @throws IOException
     */
    public static String tryDetectCharsetEncoding(URL url, String userAgent) throws IOException {
        if (null == url)
            throw new IllegalArgumentException("url");

        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(15000);
        if (!TextUtils.isEmpty(userAgent)) {
            conn.setRequestProperty(Network.USER_AGENT, userAgent);
        }

        String ret = null;

        // 1. the encoding given in the charset parameter of the Content-Type
        // HTTP header,
        String contentType = conn.getContentType();
        if (!TextUtils.isEmpty(contentType)) {
            Matcher matcher = ContentTypePattern_Charset.matcher(contentType);
            if (matcher.matches() && matcher.groupCount() >= 3) {
                String charset = matcher.group(2);
                if (!TextUtils.isEmpty(charset)) {
                    ret = charset;
                    Log.v(LogTag, "HTTP charset detected is: " + ret);
                }
            }

            // 2. or the encoding given in the encoding attribute of the XML
            // declaration within the document,
            if (TextUtils.isEmpty(ret)) {
                matcher = ContentTypePattern_MimeType.matcher(contentType);
                if (matcher.matches() && matcher.groupCount() >= 2) {
                    String mimetype = matcher.group(1);
                    if (!TextUtils.isEmpty(mimetype)) {
                        mimetype = mimetype.toLowerCase();
                        if (mimetype.startsWith("application/")
                                && (mimetype.startsWith("application/xml") || mimetype
                                        .endsWith("+xml"))) {
                            InputStream responseStream = null;
                            try {
                                responseStream = new DoneHandlerInputStream(conn.getInputStream());
                                BufferedReader reader = new BufferedReader(new InputStreamReader(
                                        responseStream));
                                String aLine;
                                while ((aLine = reader.readLine()) != null) {
                                    aLine = aLine.trim();
                                    if (aLine.length() == 0)
                                        continue;

                                    matcher = ContentTypePattern_XmlEncoding.matcher(aLine);
                                    if (matcher.matches() && matcher.groupCount() >= 3) {
                                        String charset = matcher.group(2);
                                        if (!TextUtils.isEmpty(charset)) {
                                            ret = charset;
                                            Log.v(LogTag, "XML charset detected is: " + ret);
                                        }
                                    }
                                    break;
                                }
                            } finally {
                                if (responseStream != null)
                                    responseStream.close();
                            }
                        }
                    }
                }
            }
        }

        return ret;
    }

    public static final Pattern ContentTypePattern_MimeType = Pattern.compile("([^\\s;]+)(.*)");

    public static final Pattern ContentTypePattern_Charset = Pattern.compile(
            "(.*?charset\\s*=[^a-zA-Z0-9]*)([-a-zA-Z0-9]+)(.*)", Pattern.CASE_INSENSITIVE);

    public static final Pattern ContentTypePattern_XmlEncoding = Pattern.compile(
            "(\\<\\?xml\\s+.*?encoding\\s*=[^a-zA-Z0-9]*)([-a-zA-Z0-9]+)(.*)",
            Pattern.CASE_INSENSITIVE);

    public static InputStream getHttpPostAsStream(URL url, String data,
            Map<String, String> headers, String userAgent, String cookie) throws IOException {
        if (null == url)
            throw new IllegalArgumentException("url");

        URL newUrl = url;

        InputStream responseStream = null;
        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection conn = (HttpURLConnection) newUrl.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        if (!TextUtils.isEmpty(userAgent)) {
            conn.setRequestProperty(Network.USER_AGENT, userAgent);
        }

        if (!TextUtils.isEmpty(cookie)) {
            conn.setRequestProperty("Cookie", cookie);
        }

        conn.getOutputStream().write(data.getBytes());
        conn.getOutputStream().flush();
        conn.getOutputStream().close();

        String responseCode = conn.getResponseCode() + "";
        headers.put("ResponseCode", responseCode);

        for (int i = 0;; i++) {
            String name = conn.getHeaderFieldKey(i);
            String value = conn.getHeaderField(i);
            if (name == null && value == null) {
                break;
            }
            headers.put(name, value);

        }
        responseStream = conn.getInputStream();
        return responseStream;
    }

    public static HttpHeaderInfo getHttpHeaderInfo(String urlString, String userAgent, String cookie) {
        try {
            URL url = new URL(urlString);
            if (!url.getProtocol().equals("http") && !url.getProtocol().equals("https")) {
                // this is not a http protocol, return
                return null;
            }
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (urlString.indexOf("wap") == -1) {
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
            } else {
                // this is suspected as a wap site,
                // let's wait for the result a little longer
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
            }
            if (!TextUtils.isEmpty(userAgent)) {
                conn.setRequestProperty(Network.USER_AGENT, userAgent);
            }

            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
            }

            HttpHeaderInfo ret = new HttpHeaderInfo();
            ret.ResponseCode = conn.getResponseCode();

            ret.UserAgent = userAgent;
            for (int i = 0;; i++) {
                String name = conn.getHeaderFieldKey(i);
                String value = conn.getHeaderField(i);
                if (name == null && value == null) {
                    break;
                }
                if (name != null && name.equals("content-type")) {
                    ret.ContentType = value;
                }

                if (name != null && name.equals("location")) {
                    URI uri = new URI(value);
                    if (!uri.isAbsolute()) {
                        URI baseUri = new URI(urlString);
                        uri = baseUri.resolve(uri);
                    }
                    ret.realUrl = uri.toString();
                }
            }
            return ret;
        } catch (MalformedURLException e) {
            Log.e(LogTag, "Failed to transform URL", e);
        } catch (IOException e) {
            Log.e(LogTag, "Failed to get mime type", e);
        } catch (URISyntaxException e) {
            Log.e(LogTag, "Failed to parse URI", e);
        }
        return null;
    }

    public static class HttpHeaderInfo {
        public int ResponseCode;

        public String ContentType;

        public String UserAgent;

        public String realUrl;

        public Map<String, String> AllHeaders;

        @Override
        public String toString() {
            return String.format("resCode = %1$d, headers = %2$s", ResponseCode, AllHeaders.toString());
        }
    }

    public static String fromParamListToString(List<NameValuePair> nameValuePairs) {
        StringBuffer params = new StringBuffer();
        for (NameValuePair pair : nameValuePairs) {
            try {
                if (pair.getValue() == null)
                    continue;
                params.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                params.append("=");
                params.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
                params.append("&");
            } catch (UnsupportedEncodingException e) {
                Log.d(LogTag, "Failed to convert from param list to string: " + e.toString());
                Log.d(LogTag, "pair: " + pair.toString());
                return null;
            }
        }
        if (params.length() > 0) {
            params = params.deleteCharAt(params.length() - 1);
        }
        return params.toString();
    }

    /**
     * 閸氭垶婀囬崝锛勵伂閹绘劒姘ttpPost鐠囬攱鐪�鐠佸墽鐤嗘稉锟界粔鎺楁寭鏉╃偞甯寸搾鍛閿涘苯褰傞柅浣规殶閹诡喛绉撮弮鏈佃礋15缁夛拷     *
     * @param url: HTTP post閻ㄥ垊RL閸︽澘娼�     * @param nameValuePairs: HTTP post閸欏倹鏆�     * @return JSONObject
     *         {
     *             "RESPONSE_CODE" : 200,
     *             "RESPONSE_BODY" : "Hello, world!"
     *         }
     * @throws IOException: 鐠嬪啰鏁ゆ潻鍥┾柤娑擃厼褰查懗鑺ュ閸戝搫鍩宔xception
     */
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_BODY = "RESPONSE_BODY";
    public static JSONObject doHttpPostWithResponseStatus(Context context, String url,
            List<NameValuePair> nameValuePairs, Map<String, String> headers, String userAgent,
            String cookie) {

        if (null == context)
            throw new IllegalArgumentException("context");

        if (TextUtils.isEmpty(url))
            throw new IllegalArgumentException("url");

        JSONObject result = new JSONObject();

        BasicHttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, READ_TIMEOUT);
        if (!TextUtils.isEmpty(userAgent)) {
            HttpProtocolParams.setUserAgent(httpParameters, userAgent);
        }
        if (!TextUtils.isEmpty(cookie)) {
            httpParameters.setParameter("Cookie", cookie);
        }
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        try {
            HttpPost httpPost;
            if (isCmwap(context)) {
                URL _url = new URL(url);
                String cmwapUrl = getCMWapUrl(_url);
                String host = _url.getHost();
                httpPost = new HttpPost(cmwapUrl);
                httpPost.addHeader(CMWAP_HEADER_HOST_KEY, host);
            } else {
                httpPost = new HttpPost(url);
            }
            if (null != nameValuePairs && nameValuePairs.size() != 0)
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            String strResponseBody = "";
            int responseCode = response.getStatusLine().getStatusCode();
            HttpEntity body = response.getEntity();
            if (null != body) {
                strResponseBody = EntityUtils.toString(body);
            }

            result.put(Network.RESPONSE_CODE, responseCode);
            result.put(Network.RESPONSE_BODY, strResponseBody);

        } catch (ParseException e) {
            Log.e(LogTag, "doHttpPostWithResponseStatus", e);
        } catch (IOException e) {
            Log.e(LogTag, "doHttpPostWithResponseStatus", e);
        } catch (JSONException e) {
            Log.e(LogTag, "doHttpPostWithResponseStatus", e);
        } finally {
            if(!result.has(Network.RESPONSE_CODE) || !result.has(Network.RESPONSE_BODY)) {
                result.remove(Network.RESPONSE_CODE);
                result.remove(Network.RESPONSE_BODY);
            }
        }

        return result;
    }

    /**
     * 閸氭垶婀囬崝锛勵伂閹绘劒姘ttpPost鐠囬攱鐪�鐠佸墽鐤嗘稉锟界粔鎺楁寭鏉╃偞甯寸搾鍛閿涘苯褰傞柅浣规殶閹诡喕绗夌搾鍛閿涳拷     *
     * @param url: HTTP post閻ㄥ垊RL閸︽澘娼�     * @param nameValuePairs: HTTP post閸欏倹鏆�     * @return: 婵″倹鐏塸ost
     *          response娴狅絿鐖滄稉宥嗘Ц2xx閿涘矁銆冪粈鍝勫絺閻㈢喍绨￠柨娆掝嚖閿涘矁绻戦崶鐎梪ll閵嗗倸鎯侀崚娆掔箲閸ョ偞婀囬崝鈥虫珤鏉╂柨娲栭惃鍕殶閹诡噯绱欐俊鍌涚亯閺堝秴濮熼崳銊︾梾閺堝绻戦崶鐐版崲娴ｆ洘鏆熼幑顕嗙礉鏉╂柨娲�"閿涘绱�     * @throws IOException: 鐠嬪啰鏁ゆ潻鍥┾柤娑擃厼褰查懗鑺ュ閸戝搫鍩宔xception
     */
    public static String doHttpPost(Context context, String url, List<NameValuePair> nameValuePairs)
            throws IOException {
        return doHttpPost(context, url, nameValuePairs, null, null, null);
    }

    public static String doHttpPost(Context context, String url,
            List<NameValuePair> nameValuePairs, HttpHeaderInfo responseHdrs, String userAgent,
            String cookie) throws IOException {
        if (TextUtils.isEmpty(url))
            throw new IllegalArgumentException("url");

        HttpURLConnection conn = getHttpUrlConnection(context, new URL(url));
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setRequestMethod("POST");
        if (!TextUtils.isEmpty(userAgent)) {
            conn.setRequestProperty(Network.USER_AGENT, userAgent);
        }
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }

        String strParams = fromParamListToString(nameValuePairs);
        if(null == strParams) {
            throw new IllegalArgumentException("nameValuePairs");
        }

        conn.setDoOutput(true);
        byte[] b = strParams.getBytes();
        conn.getOutputStream().write(b, 0, b.length);
        conn.getOutputStream().flush();
        conn.getOutputStream().close();
        int statusCode = conn.getResponseCode();
        Log.d(LogTag, "Http POST Response Code: " + statusCode);
        if (responseHdrs != null) {
            responseHdrs.ResponseCode = statusCode;
            if (responseHdrs.AllHeaders == null)
                responseHdrs.AllHeaders = new HashMap<String, String>();

            for (int i = 0;; i++) {
                String name = conn.getHeaderFieldKey(i);
                String value = conn.getHeaderField(i);
                if (name == null && value == null) {
                    break;
                }
                responseHdrs.AllHeaders.put(name, value);
                i++;
            }
        }

        BufferedReader rd = new BufferedReader(new InputStreamReader(new DoneHandlerInputStream(
                conn.getInputStream())));
        String tempLine = rd.readLine();
        StringBuffer tempStr = new StringBuffer();
        String crlf = System.getProperty("line.separator");
        while (tempLine != null) {
            tempStr.append(tempLine);
            tempStr.append(crlf);
            tempLine = rd.readLine();
        }
        String responseContent = tempStr.toString();
        rd.close();

        return responseContent;
    }

    /**
     * @param strUrl 鐟曚礁濮炵�鍡欐畱URL string
     * @return 閼惧嘲褰囬崝鐘茬槕閸氬海娈慤RL string
     */
    public static String encryptURL(String strUrl) {
        if (!TextUtils.isEmpty(strUrl)) {
            new String();
            String strTemp = String.format("%sbe988a6134bc8254465424e5a70ef037", strUrl);
            return strTemp;
            //return String.format("%s&key=%s", strUrl, MD5.MD5_32(strTemp));
        } else
            return null;
    }

    public interface PostDownloadHandler {
        void OnPostDownload(boolean sucess);
    }

    /**
     * 瀵拷顬婃稉瀣祰鏉╂粎鈻奸弬鍥︽閸掔増瀵氱�姘崇翻閸戠儤绁�     *
     * @param url 鏉╂粎鈻奸弬鍥︽閸︽澘娼�     * @param output 鏉堟挸鍤ù锟�    * @param handler 娑撳娴囬幋鎰閹存牞锟芥径杈Е閻ㄥ嫬顦甸悶锟�    */
    public static void beginDownloadFile(String url, OutputStream output,
            PostDownloadHandler handler) {
        DownloadTask task = new DownloadTask(url, output, handler);
        task.execute();
    }

    public static void beginDownloadFile(String url, OutputStream output, Context context,
            boolean bOnlyWifi, PostDownloadHandler handler) {
        DownloadTask task = new DownloadTask(url, output, handler, bOnlyWifi, context);
        task.execute();
    }

    /**
     * 娑撳娴囨潻婊呪柤閺傚洣娆㈤崚鐗堝瘹鐎规俺绶崙鐑樼ウ
     *
     * @param url 鏉╂粎鈻奸弬鍥︽閸︽澘娼�     * @param output 鏉堟挸鍤ù锟�    * @return 閹存劕濮涙稉搴℃儊
     */
    public static boolean downloadFile(String urlStr, OutputStream output) {
        return downloadFile(urlStr, output, false, null);
    }

    public static boolean downloadFile(String urlStr, OutputStream output, boolean bOnlyWifi,
            Context context) {
        boolean bCanceled = false;

        InputStream input = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            HttpURLConnection.setFollowRedirects(true);
            conn.connect();
            input = conn.getInputStream();

            byte[] buffer = new byte[1024];
            int count;

            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
                if (bOnlyWifi && context != null && !isWifi(context)) {
                    bCanceled = true;
                    break;
                }
            }
            return !bCanceled;
        } catch (IOException e) {
            Log.e(LogTag, "error while download file" + e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    //
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    //
                }
            }
        }

        return false;
    }

    /**
     * 娑撳娴囨潻婊呪柤閺傚洣娆㈤崚鐗堝瘹鐎规俺绶崙鐑樼ウ
     *
     * @param url 鏉╂粎鈻奸弬鍥︽閸︽澘娼�     * @param output 鏉堟挸鍤ù锟�    * @return 閹存劕濮涙稉搴℃儊
     */
    public static boolean downloadFile(String urlStr, OutputStream output, Context context) {
        try {
            HttpURLConnection conn = null;
            URL url = new URL(urlStr);
            if (Network.isCmwap(context)) {
                HttpURLConnection.setFollowRedirects(false);
                String cmwapUrl = Network.getCMWapUrl(url);
                String host = url.getHost();
                conn = (HttpURLConnection) new URL(cmwapUrl).openConnection();
                conn.setRequestProperty(Network.CMWAP_HEADER_HOST_KEY, host);
                int resCode = conn.getResponseCode();
                while (resCode >= 300 && resCode < 400) {
                    String redirectedUrl = conn.getHeaderField("location");
                    if (TextUtils.isEmpty(redirectedUrl)) {
                        break;
                    }
                    url = new URL(redirectedUrl);
                    cmwapUrl = Network.getCMWapUrl(url);
                    host = url.getHost();
                    conn = (HttpURLConnection) new URL(cmwapUrl).openConnection();
                    conn.setRequestProperty(Network.CMWAP_HEADER_HOST_KEY, host);
                    resCode = conn.getResponseCode();
                }
            } else {
                conn = (HttpURLConnection) url.openConnection();
                HttpURLConnection.setFollowRedirects(true);
            }

            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.connect();
            InputStream input = conn.getInputStream();

            byte[] buffer = new byte[1024];
            int count;

            while ((count = input.read(buffer)) > 0) {
                output.write(buffer, 0, count);
            }

            input.close();
            output.close();
            return true;
        } catch (IOException e) {
            Log.e(LogTag, "error while download file" + e);
        }

        return false;
    }

    public static String uploadFile(String url, File file, String fileKey) throws IOException {

        if (!file.exists()) {
            return null;
        }
        String filename = file.getName();

        HttpURLConnection conn = null;

        final String lineEnd = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "*****";

        FileInputStream fileInputStream = null;
        DataOutputStream dos = null;
        BufferedReader rd = null;

        try {
            URL _url = new URL(url);
            conn = (HttpURLConnection) _url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);

            // Allow Inputs
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            final int EXTRA_LEN = 77; // 闂勩倕骞撻弬鍥︽閸氬秴鎷伴弬鍥︽閸愬懎顔愭稊瀣檱閿涘本澧嶉張澶婂敶鐎瑰湱娈憀ength
            int len = EXTRA_LEN + filename.length() + (int) file.length() + fileKey.length();
            conn.setFixedLengthStreamingMode(len);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + fileKey + "\";filename=\""
                    + file.getName() + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            // read file and write it into form...
            fileInputStream = new FileInputStream(file);
            int bytesRead = -1;
            final int BUFFER_SIZE = 1024;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
                dos.flush();
            }
            // send multi-part form data necessary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens);
            dos.writeBytes(boundary);
            dos.writeBytes(twoHyphens);
            dos.writeBytes(lineEnd);

            // flush streams
            dos.flush();
            StringBuffer sb = new StringBuffer();
            rd = new BufferedReader(new InputStreamReader(new DoneHandlerInputStream(
                    conn.getInputStream())));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (rd != null) {
                    rd.close();
                }
            } catch (IOException e) {
                Log.e(LogTag, "error while closing strean", e);
            }
        }
    }

    private static class DownloadTask extends AsyncTask<Void, Void, Boolean> {
        private String url;

        private OutputStream output;

        private PostDownloadHandler handler;

        private boolean bOnlyWifi;

        private Context mContext;

        public DownloadTask(String url, OutputStream output, PostDownloadHandler handler) {
            this(url, output, handler, false, null);
        }

        public DownloadTask(String url, OutputStream output, PostDownloadHandler handler,
                boolean bOnlyWifi, Context context) {
            this.url = url;
            this.output = output;
            this.handler = handler;
            this.bOnlyWifi = bOnlyWifi;
            this.mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return Network.downloadFile(this.url, this.output, this.bOnlyWifi, this.mContext);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            this.handler.OnPostDownload(result);
        }
    }

    public static int getActiveNetworkType(Context context) {
        int defaultValue = -1;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return defaultValue;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null)
            return defaultValue;
        return info.getType();
    }

    public static String getActiveNetworkName(final Context context) {
        String defaultValue = "null";
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return defaultValue;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null)
            return defaultValue;
        if (TextUtils.isEmpty(info.getSubtypeName()))
            return info.getTypeName();
        return String.format("%s-%s", info.getTypeName(), info.getSubtypeName());
    }

    public static boolean isWifi(Context context) {
        return (getActiveNetworkType(context) == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isCmwap(Context context) {
        // 婵″倹鐏夋稉宥嗘Ц娑擃厼娴梥im閸椻槄绱濋惄瀛樺复鏉╂柨娲栭崥锟�      
    	TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String countryISO = tm.getSimCountryIso();
        if (!"CN".equalsIgnoreCase(countryISO)) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null)
            return false;
        String extraInfo = info.getExtraInfo();
        if (TextUtils.isEmpty(extraInfo) || (extraInfo.length() < 3))
            return false;
        if (extraInfo.contains("ctwap")) {
            return false;
        }
        return extraInfo.regionMatches(true, extraInfo.length() - 3, "wap", 0, 3);
    }

    public static boolean isCtwap(Context context) {
        // 婵″倹鐏夋稉宥嗘Ц娑擃厼娴梥im閸椻槄绱濋惄瀛樺复鏉╂柨娲栭崥锟�   
    	TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String countryISO = tm.getSimCountryIso();
        if (!"CN".equalsIgnoreCase(countryISO)) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null)
            return false;
        String extraInfo = info.getExtraInfo();
        if (TextUtils.isEmpty(extraInfo) || (extraInfo.length() < 3))
            return false;
        if (extraInfo.contains("ctwap")) {
            return true;
        }

        return false;
    }

    public static HttpURLConnection getHttpUrlConnection(Context context, URL url)
            throws IOException {
        if (isCtwap(context)) {
            java.net.Proxy proxy = new java.net.Proxy(Type.HTTP, new InetSocketAddress(
                    "10.0.0.200", 80));
            return (HttpURLConnection) url.openConnection(proxy);
        }
        if (!isCmwap(context)) {
            return (HttpURLConnection) url.openConnection();
        } else {
            String host = url.getHost();
            String cmwapUrl = getCMWapUrl(url);
            URL gatewayUrl = new URL(cmwapUrl);
            HttpURLConnection conn = (HttpURLConnection) gatewayUrl.openConnection();
            conn.addRequestProperty(CMWAP_HEADER_HOST_KEY, host);
            return conn;
        }
    }

    public static String getCMWapUrl(URL oriUrl) {
        StringBuilder gatewayBuilder = new StringBuilder();
        gatewayBuilder.append(oriUrl.getProtocol()).append("://").append(CMWAP_GATEWAY)
                .append(oriUrl.getPath());
        if (!TextUtils.isEmpty(oriUrl.getQuery())) {
            gatewayBuilder.append("?").append(oriUrl.getQuery());
        }
        return gatewayBuilder.toString();
    }

    /**
     * This input stream won't read() after the underlying stream is exhausted.
     * http://code.google.com/p/android/issues/detail?id=14562
     */
    public final static class DoneHandlerInputStream extends FilterInputStream {
        private boolean done;

        public DoneHandlerInputStream(InputStream stream) {
            super(stream);
        }

        @Override
        public int read(byte[] bytes, int offset, int count) throws IOException {
            if (!done) {
                int result = super.read(bytes, offset, count);
                if (result != -1) {
                    return result;
                }
            }
            done = true;
            return -1;
        }
    }

    /**
     * 濞夈劍鍓伴敍姘煙濞夋洝绻戦崶鐐垫畱閺勵垰缍嬮崜宥嗘箒濞屸剝婀乤ctive閻ㄥ嫮缍夌紒婊愮礉閼板奔绗夐弰顖氱秼閸撳秵婀佸▽鈩冩箒閸欘垯浜掓潻鐐村复瀵版ぞ绗傞惃鍕秹缂佹嚎锟芥稉宥堫渽娣囶喗鏁奸弬瑙勭《閻ㄥ嫬鐤勯悳鑸拷
     *
     * @param context
     * @return
     */
    public static boolean hasNetwork(final Context context) {
        return Network.getActiveNetworkType(context) >= 0;
    }

    public static String getLocalNetworkType(final Context context) {
        if (isWIFIConnected(context)) {
            return NETWORK_TYPE_WIFI;
        }

        String extraInfo = "unknown";
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo mobNetInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobNetInfo != null) {
            extraInfo = mobNetInfo.getExtraInfo();
        }

       /* if (TelephonyUtils.isChinaTelecom(context)) {
            extraInfo = NETWORK_TYPE_CHINATELECOM;
        }*/

        return extraInfo;
    }


    public static boolean isWIFIConnected(final Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }

        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }

        return ConnectivityManager.TYPE_WIFI == info.getType();
    }

    public static String getActiveConnPoint(final Context context) {
        if (isWIFIConnected(context)) {
            return "wifi";
        } else {
            final ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                return "";
            }

            final NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null) {
                return "";
            }
            return info.getExtraInfo();
        }
    }
}
