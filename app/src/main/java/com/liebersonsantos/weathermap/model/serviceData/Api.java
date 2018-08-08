package com.liebersonsantos.weathermap.model.serviceData;

import com.liebersonsantos.weathermap.model.WeatherResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("data/2.5/weather")
    Single<WeatherResponse> getDataApi(@Query("q") String city, @Query("appId") String apiKey, @Query("lang") String lang);
}
