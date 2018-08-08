package com.liebersonsantos.weathermap.model;

import com.liebersonsantos.weathermap.contract.Contract;
import com.liebersonsantos.weathermap.helper.Constants;
import com.liebersonsantos.weathermap.model.serviceData.RestClient;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class Model implements Contract.Model{

    private Contract.Presenter presenter;
    private CompositeDisposable disposable = new CompositeDisposable();

    public Model(Contract.Presenter presenter){
        this.presenter = presenter;
    }


    @Override
    public Single<WeatherResponse> getDataApi(String location) {
        return RestClient.getInstance().getDataApi(location, Constants.API_KEY, "pt");
    }
}
