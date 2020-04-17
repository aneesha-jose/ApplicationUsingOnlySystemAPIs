package com.aneeshajose.headlines.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.aneeshajose.headlines.common.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Aneesha Jose on 2020-03-31.
 * P.S : Haven't data for post and out calls for now.
 */
public class NetworkCallHandler<T> implements Callable<T> {

    private static final String TAG = NetworkCallHandler.class.getSimpleName();
    private String url;
    private Type type;
    @Constants.NetworkCallTypes
    private String methodType;

    public NetworkCallHandler(@NonNull String url, @NonNull TypeToken<T> typeToken, @Constants.NetworkCallTypes String methodType) {
        this.url = url;
        this.type = typeToken.getType();
        this.methodType = methodType;
    }

    @Override
    public T call() throws Exception {
        String json = downloadUrl(url);
        return new Gson().fromJson(json, type);
    }

    /**
     * Given a URL, sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response body in String form. Otherwise,
     * it will throw an IOException.
     */
    private String downloadUrl(String urlpath) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            Log.d(TAG, "URL : " + urlpath);
            URL url = new URL(urlpath);
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(Constants.READ_TIMEOUT);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);

            // For this use case, set HTTP method to GET.
            connection.setRequestMethod(methodType);
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();
            Log.d(TAG, "connect called");
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "response code : " + responseCode);
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            Log.d(TAG, "input stream");
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream);
                Log.d(TAG, "result : " + result);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    /**
     * Converts the contents of an InputStream to a String.
     */
    private String readStream(InputStream stream) throws IOException {
        // Read InputStream using the UTF-8 charset.
        //noinspection CharsetObjectCanBeUsed - since min sdk is 16
        InputStreamReader inputReader = new InputStreamReader(stream, "UTF-8");
        //Creating a BufferedReader object
        BufferedReader reader = new BufferedReader(inputReader);
        StringBuilder result = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            result.append(str);
        }
        return result.toString();
    }
}

