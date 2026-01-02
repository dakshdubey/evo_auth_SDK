package com.authsdk.http;

import com.authsdk.core.AuthConfig;
import com.authsdk.exceptions.AuthApiException;
import com.authsdk.exceptions.AuthSdkException;
import com.authsdk.models.ApiError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClientProvider {
    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private final String apiKey; // Can be sent in headers if needed

    public HttpClientProvider(AuthConfig config) {
        this.baseUrl = config.getBaseUrl();
        this.apiKey = config.getApiKey();
        
        this.client = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectionTimeoutMs(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getConnectionTimeoutMs(), TimeUnit.MILLISECONDS)
                .build();

        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> T post(String path, Object body, Class<T> responseType) {
        return execute(requestBuilder(path)
                .post(createRequestBody(body))
                .build(), responseType);
    }
    
    public <T> T post(String path, Object body, Class<T> responseType, String token) {
        return execute(requestBuilder(path, token)
                .post(createRequestBody(body))
                .build(), responseType);
    }

    public <T> T get(String path, Class<T> responseType) {
        return execute(requestBuilder(path).get().build(), responseType);
    }

    public <T> T get(String path, Class<T> responseType, String token) {
        return execute(requestBuilder(path, token).get().build(), responseType);
    }

    private Request.Builder requestBuilder(String path) {
        return requestBuilder(path, null);
    }

    private Request.Builder requestBuilder(String path, String token) {
        Request.Builder builder = new Request.Builder()
                .url(baseUrl + path)
                .header("Accept", "application/json");

        if (apiKey != null) {
            builder.header("x-api-key", apiKey);
        }
        
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }

        return builder;
    }

    private RequestBody createRequestBody(Object body) {
        try {
            String json = mapper.writeValueAsString(body);
            return RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        } catch (JsonProcessingException e) {
            throw new AuthSdkException("Failed to serialize request body", e);
        }
    }

    private <T> T execute(Request request, Class<T> responseType) {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                if (responseType == Void.class) return null;
                if (response.body() == null) return null;
                return mapper.readValue(response.body().string(), responseType);
            } else {
                handleError(response);
                return null; // Unreachable
            }
        } catch (IOException e) {
            throw new AuthSdkException("Network error executing request to " + request.url(), e);
        }
    }

    private void handleError(Response response) throws IOException { // Throws runtime exception
        String errorBody = response.body() != null ? response.body().string() : "";
        String message = "Request failed with status " + response.code();
        
        try {
            ApiError error = mapper.readValue(errorBody, ApiError.class);
            if (error.getMessage() != null) {
                message = error.getMessage();
            }
        } catch (Exception ignored) {
            // Fallback to raw body if JSON parsing fails
            if (!errorBody.isEmpty()) message += ": " + errorBody;
        }

        throw new AuthApiException(message, response.code());
    }
}
