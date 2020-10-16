package com.elcom.gateway.restcore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class BasedHttpClientHelper {

    public static HttpURLConnection setBody(HttpURLConnection conn, String content) throws ISRestCoreException {
        //set request body data

        OutputStream os;
        try {

            os = conn.getOutputStream();
            os.write(content.getBytes("UTF-8"));
            os.flush();
            return conn;
        } catch (IOException e) {
            throw new ISRestCoreException(e.getMessage());
        }

    }

    public static HttpURLConnection setToken(HttpURLConnection conn, String token_type, String token) throws ISRestCoreException {
        String value = String.format("%s %s", token_type, token);
        conn.setRequestProperty("Authorization", value);
        return conn;

    }

    public static String[] processBodyContent(HttpURLConnection conn) throws ISRestCoreException {
        String[] result = new String[2];
        BufferedReader br;
        try {
            long start = System.nanoTime();
            try {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } catch (Exception e) {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }
            String output = "";
            String value;
            while ((value = br.readLine()) != null) {
                output += value;
            }
            br.close();

            long elapsed = System.nanoTime() - start;
            result[0] = output;
            //result[1] = (elapsed / 1000) + "";
            result[1] = Long.toString(elapsed / 1000);
            return result;
        } catch (IOException e) {
            throw new ISRestCoreException(e.getMessage());
        }
    }

    public static HttpURLConnection InitHttpURLConnection(String subURL, HttpMethod method, ContentType contentType, AcceptType acceptType) throws ISRestCoreException {

        try {
            URL tempUrl;
            tempUrl = consolidateURL(subURL);
            HttpURLConnection conn = (HttpURLConnection) tempUrl.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(method.getDescription());
            conn.setRequestProperty("Accept-Charset", "utf-8");

            if (contentType != ContentType.NotSet) {
                conn.setRequestProperty("Content-Type", contentType.getDescription());
            }

            if (acceptType != AcceptType.NotSet) {
                conn.setRequestProperty("Accept", acceptType.getDescription());
            }

            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android 4.4; Mobile; rv:41.0) Gecko/41.0 Firefox/41.0");

            conn.setConnectTimeout(RestConstants.REQ_TIME_OUT);

            return conn;
        } catch (ISRestCoreException | IOException e) {
            throw new ISRestCoreException(e.getMessage());
        }

    }

    public static ApiResult processApiResult(HttpURLConnection conn) throws ISRestCoreException {
        ApiResult result = new ApiResult();
        try {
            result.setCode(conn.getResponseCode());
            String[] responseData = processBodyContent(conn);
            result.setBodyContent(responseData[0]);
            String executeTimes = responseData[1];
            if (conn.getConnectTimeout() <= Long.parseLong(executeTimes)) {
                result.setCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
            }
            conn.disconnect();

            //something wrong 
//			if(result != null && !result.getStatus())
//				throw new ISRestCoreException(result.getBodyContent());
            return result;
        } catch (IOException e) {
            throw new ISRestCoreException(e.getMessage());
        }

    }

    public static URL consolidateURL(String subURL) throws ISRestCoreException {
        try {
            return new URL(subURL);
        } catch (MalformedURLException e) {
            throw new ISRestCoreException(e.getMessage());
        }
    }
}
