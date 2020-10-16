package com.elcom.gateway.restcore;

import java.io.Closeable;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.concurrent.Callable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BasedHttpClient implements Closeable {

    private HttpURLConnection conn = null;
    private static Gson gson = new GsonBuilder().create();
    private String token_type = null;
    private String token = null;

    public BasedHttpClient() {

    }

    public BasedHttpClient(String token_type, String token) {
        this.token_type = token_type;
        this.token = token;
    }

    public <T> T doPost(String subURL, String serializedObject, Class<T> outputObject) throws ISRestCoreException {
        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.POST, ContentType.Xml,
                    AcceptType.Xml);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            conn = BasedHttpClientHelper.setBody(conn, serializedObject);

            ApiResult result = BasedHttpClientHelper.processApiResult(conn);

            return (T) XMLConverter.toObject(result.getBodyContent(), outputObject);
        });
    }

    // for create new item
    public ApiResult doPost(String subURL, String serializedObject, ContentType contentType)
            throws ISRestCoreException {
        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.POST, contentType, AcceptType.Json);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            conn = BasedHttpClientHelper.setBody(conn, serializedObject);

            return BasedHttpClientHelper.processApiResult(conn);
        });
    }

    public ApiResult doPost(String subURL, Object object, ContentType contentType)
            throws ISRestCoreException {
        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.POST, contentType, AcceptType.Json);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            conn = BasedHttpClientHelper.setBody(conn, gson.toJson(object));

            return BasedHttpClientHelper.processApiResult(conn);
        });
    }

    // for create new item
    public <T> T doPost(String subURL, Class<T> inputClass) throws ISRestCoreException {
        return this.doPost(subURL, "", ContentType.Json, inputClass);
    }

    public Boolean doDelete(String subURL) throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.DELETE, ContentType.NotSet,
                    AcceptType.NotSet);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            return BasedHttpClientHelper.processApiResult(conn) != null;
        });

    }

    public <T> T doPost(String subURL, String serializedObject, ContentType contentType, Class<T> inputClass)
            throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.POST, contentType, AcceptType.Json);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            conn = BasedHttpClientHelper.setBody(conn, serializedObject);

            ApiResult result = BasedHttpClientHelper.processApiResult(conn);

            return toObject(inputClass, result.getBodyContent());
        });

    }

    public <T> T doPost(String subURL, String serializedObject, ContentType contentType, Class<T> inputClass,
            String jsonSubject) throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.POST, contentType, AcceptType.Json);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            conn = BasedHttpClientHelper.setBody(conn, serializedObject);

            ApiResult result = BasedHttpClientHelper.processApiResult(conn);
            // result.setBodyContent("{\"Result\":{\"error_status\":\"0\",\"issue_result\":[{\"CardNumber\":123,\"CardId\":456}]}}");
            return toObject(inputClass, result.getBodyContent(), result.getCode(), jsonSubject);
        });

    }

    public ApiResult doPost(String subURL, String serializedObject, ContentType contentType, AcceptType acceptType)
            throws ISRestCoreException {
        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.POST, contentType, acceptType);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            conn = BasedHttpClientHelper.setBody(conn, serializedObject);

            return BasedHttpClientHelper.processApiResult(conn);
        });
    }

    // for update item
    public ApiResult doPut(String subURL, String serializedObject, ContentType contentType) throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.PUT, contentType, AcceptType.Xml);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            conn = BasedHttpClientHelper.setBody(conn, serializedObject);

            return BasedHttpClientHelper.processApiResult(conn);

        });

    }

    public ApiResult doPut(String subURL, String serializedObject, AcceptType acceptType, ContentType contentType)
            throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.PUT, contentType, acceptType);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            conn = BasedHttpClientHelper.setBody(conn, serializedObject);

            return BasedHttpClientHelper.processApiResult(conn);

        });

    }

    // for update item
    public <T> T doPut(String subURL, String serializedObject, ContentType contentType, Class<T> inputClass)
            throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.PUT, contentType, AcceptType.Xml);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            conn = BasedHttpClientHelper.setBody(conn, serializedObject);

            ApiResult result = BasedHttpClientHelper.processApiResult(conn);

            return toObject(inputClass, result.getBodyContent(), result.getBodyContent());

        });

    }

    // retrieve items
    public ApiResult doGet(String subURL, AcceptType acceptType) throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.GET, ContentType.NotSet, acceptType);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            return BasedHttpClientHelper.processApiResult(conn);
        });

    }

    public ApiResult doGet(String subURL, AcceptType acceptType, ContentType contentType) throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.GET, contentType, acceptType);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            return BasedHttpClientHelper.processApiResult(conn);
        });

    }

    public <T> T doGet(String subURL, Class<T> classInput, AcceptType acceptType) throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.GET, ContentType.NotSet, acceptType);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            ApiResult result = BasedHttpClientHelper.processApiResult(conn);

            if (acceptType == AcceptType.Xml) {
                return XMLConverter.toObject(result.getBodyContent(), classInput);
            } else {
                return toObject(classInput, result.getBodyContent());
            }
        });
    }

    public <T> T doGet(String subURL, Class<T> classInput, ContentType contentType, AcceptType acceptType, String jsonSubject)
            throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.GET, contentType, acceptType);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            ApiResult result = BasedHttpClientHelper.processApiResult(conn);

            if (acceptType == AcceptType.Xml) {
                return XMLConverter.toObject(result.getBodyContent(), classInput);
            } else {
                return toObject(classInput, result.getBodyContent(), jsonSubject);
            }
        });
    }

    public <T> T doGet(String subURL, Class<T> classInput, ContentType contentType, AcceptType acceptType)
            throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.GET, contentType, acceptType);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            ApiResult result = BasedHttpClientHelper.processApiResult(conn);

            if (acceptType == AcceptType.Xml) {
                return XMLConverter.toObject(result.getBodyContent(), classInput);
            } else {
                return toObject(classInput, result.getBodyContent());
            }
        });
    }

    // retrieve items
    public ApiResult doGet(String subURL) throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.GET, ContentType.NotSet,
                    AcceptType.Xml);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            return BasedHttpClientHelper.processApiResult(conn);
        });

    }

    public ApiResult doGet(String subURL, ContentType contentType) throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.GET, contentType, AcceptType.NotSet);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            return BasedHttpClientHelper.processApiResult(conn);
        });

    }

    // retrieve items
    public <T> T doGet(String subURL, Class<T> classInput) throws ISRestCoreException {

        return execute(() -> {
            conn = BasedHttpClientHelper.InitHttpURLConnection(subURL, HttpMethod.GET, ContentType.NotSet,
                    AcceptType.Json);

            if (this.token != null && this.token_type != null) {
                conn = BasedHttpClientHelper.setToken(conn, this.token_type, this.token);
            }

            ApiResult result = BasedHttpClientHelper.processApiResult(conn);

            return toObject(classInput, result.getBodyContent());
        });
    }

    // generic method to execute http actions
    private <T> T execute(Callable<T> func) throws ISRestCoreException {

        try {
            return func.call();
        } catch (Exception | Error e) {
            throw new ISRestCoreException(e.getMessage());
        } finally {
            disconnect();
        }

    }

    @Override
    public void close() {
        disconnect();
    }

    private void disconnect() {
        if (conn != null) {
            conn.disconnect();
            conn = null;
        }
    }

    private String getClassName(String input) {
        if (input == null) {
            return "";
        }
        int index = input.lastIndexOf(".");
        if (index != -1) {
            input = input.substring(index);
        }
        if (input.lastIndexOf(".") != -1) {
            input = input.replace(".", "");
        }
        return input;
    }

    public <T> T toObject(Class<T> classInput, String content) {
        Map<String, Object> r = gson.fromJson(content, Map.class);
        String className = this.getClassName(classInput.getName());
        String innerJson = gson.toJson(r.get(className));
        T _r = gson.fromJson(innerJson, classInput);
        return _r;
    }

    public <T> T toObject(Class<T> classInput, String content, String jsonSubject) {
        Map<String, Object> r = gson.fromJson(content, Map.class);
        String className = this.getClassName(classInput.getName());
        String innerJson = gson.toJson(r.get(jsonSubject));
        T _r = gson.fromJson(innerJson, classInput);
        return _r;
    }

    public <T> T toJSONObject(Class<T> classInput, String content, String jsonSubject) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(content).getAsJsonObject().get(jsonSubject).getAsJsonObject();
        T result = gson.fromJson(obj.toString(), classInput);
        return result;
    }

    public <T> T toObject(Class<T> classInput, String content, int httpStatusCode, String jsonSubject) {
        Map<String, Object> r = gson.fromJson(content, Map.class);
        String className = this.getClassName(classInput.getName());
        String innerJson = gson.toJson(r.get(jsonSubject));

        JsonObject jo = new JsonParser().parse(innerJson).getAsJsonObject();
        jo.addProperty("httpStatusCode", httpStatusCode);

        T _r = gson.fromJson(jo.toString(), classInput);
        return _r;
    }

    public <T> T toObjectJson(Class<T> classInput, String content) {
        Map<String, Object> r = gson.fromJson(content, Map.class);
        String innerJson = gson.toJson(r);
        T _r = gson.fromJson(innerJson, classInput);
        return _r;
    }
}
