package io.github.zkhan93.pm.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import io.github.zkhan93.pm.BuildConfig;
import io.github.zkhan93.pm.util.Constants;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zeeshan Khan on 6/12/2016.
 */
public class ApiServiceGenerator {
    public static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory
                    (GsonConverterFactory.create(gson));
    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    public static <S> S createClient(Class<S> clientClass) {
        final String apiKey = BuildConfig.API_KEY;
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder().addQueryParameter(Constants.PARAMS
                        .API_KEY, apiKey).build();
                Request.Builder requestBuilder = original.newBuilder().url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        retrofitBuilder.client(httpClientBuilder.build());
        return retrofitBuilder.build().create(clientClass);
    }
}
