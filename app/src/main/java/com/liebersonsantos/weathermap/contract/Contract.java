package com.liebersonsantos.weathermap.contract;

import com.liebersonsantos.weathermap.model.WeatherResponse;

import io.reactivex.Single;

public class Contract {

    public interface Model{
        Single<WeatherResponse> getDataApi(String location);
    }

    public interface View{
        void showWeatherResponse(WeatherResponse weatherResponse);

        void showToastErrorResponse(Throwable throwable);

    }

    public interface Presenter{
        void getDataWeather(String location);

        String getDate(long time);
        double getKelvin(Double temp);

    }
}
