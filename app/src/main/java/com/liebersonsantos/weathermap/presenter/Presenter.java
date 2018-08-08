package com.liebersonsantos.weathermap.presenter;

import android.util.Log;

import com.liebersonsantos.weathermap.contract.Contract;
import com.liebersonsantos.weathermap.model.Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Presenter implements Contract.Presenter{

    private Contract.View view;
    private Contract.Model model;
    private CompositeDisposable disposable = new CompositeDisposable();

    public Presenter(Contract.View view){
        this.view = view;
        this.model = new Model(this);
    }

    @Override
    public void getDataWeather(String location) {
        disposable.add(model.getDataApi(location)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((Disposable disposable1) -> {
                            // colocar o loading para a tela
                        })
                        .doAfterTerminate(() -> {
                            // tirar o loading da tela
                        })
                        .doOnError(throwable -> {
                            view.showToastErrorResponse(throwable);
                        })
                        .subscribe(weatherResponse -> {
                            view.showWeatherResponse(weatherResponse);

                        }, throwable -> {
                            // mostrar a mensagem pro usuario, de error
                            Log.i("TAG", "MensagemServidor: " + throwable.getMessage());
                        })
        );

    }

    @Override
    public String getDate(long milliSeconds) {
        Long millisUntilFinished = milliSeconds * 1000;

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisUntilFinished);
        return formatter.format(calendar.getTime());
    }

    @Override
    public double getKelvin(Double kelvin) {
        double celsius;
        celsius = kelvin - 273.15;
        return celsius;
    }
}
