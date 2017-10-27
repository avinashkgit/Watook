package com.watook.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.watook.BuildConfig;
import com.watook.callback.WatookApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Avinash on 22-06-2017.
 */

public class ApiManager {
    private static WatookApi apiInstance;

    public static WatookApi getApiInstance() {
        if (apiInstance == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            httpClient.connectTimeout(1, TimeUnit.MINUTES);
            httpClient.readTimeout(1, TimeUnit.MINUTES);
            httpClient.addInterceptor(logging);
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .excludeFieldsWithoutExposeAnnotation()
                    .serializeNulls()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            apiInstance = retrofit.create(WatookApi.class);
        }
        return apiInstance;
    }

}
