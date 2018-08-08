package com.liebersonsantos.weathermap.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.liebersonsantos.weathermap.R;
import com.liebersonsantos.weathermap.contract.Contract;
import com.liebersonsantos.weathermap.helper.Constants;
import com.liebersonsantos.weathermap.helper.ImageUtil;
import com.liebersonsantos.weathermap.model.Main;
import com.liebersonsantos.weathermap.model.WeatherResponse;
import com.liebersonsantos.weathermap.model.serviceData.RestClient;
import com.liebersonsantos.weathermap.presenter.Presenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, Contract.View {

    @BindView(R.id.text_location)
    EditText textLocation;
    @BindView(R.id.image_icon)
    ImageView imageIcon;
    @BindView(R.id.progressBar_id)
    ProgressBar progressBarIcon;
    @BindView(R.id.text_city_name)
    TextView textCityName;
    @BindView(R.id.text_current_temperature)
    TextView textCurrentTemperature;
    @BindView(R.id.text_temp_min)
    TextView textTempMin;
    @BindView(R.id.text_temp_max)
    TextView textTempMax;
    @BindView(R.id.text_description)
    TextView textDescription;
    @BindView(R.id.text_humidity)
    TextView textHumidity;
    @BindView(R.id.text_sunrise)
    TextView textSunrise;
    @BindView(R.id.text_sunset)
    TextView textSunset;
    @BindView(R.id.card_data)
    CardView cardData;
    @BindView(R.id.card_insert_data)
    CardView cardInsertData;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;

    private Contract.Presenter presenter;
    private static final String TAG = "RESPOSTA_API";
    private GoogleMap mMap;
    private LatLng latLng;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        presenter = new Presenter(this);

    }

    @OnClick(R.id.button_search)
    void searchWeather() {
        presenter.getDataWeather(textLocation.getText().toString());
    }

    private void setCardData() {
        PropertyValuesHolder cardDataProperty = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        PropertyValuesHolder cardInsertProperty = PropertyValuesHolder.ofFloat("y", 20f, 20f);

        cardData.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(cardData, cardDataProperty);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(cardInsertData, cardInsertProperty);
        objectAnimator.setDuration(2500);
        objectAnimator1.setDuration(3000);

        if (flag) {
            objectAnimator.start();
            objectAnimator1.start();
        } else {
            objectAnimator.reverse();
            objectAnimator1.reverse();
            flag = false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void showWeatherResponse(WeatherResponse weatherResponse){
        textCityName.setText(weatherResponse.getName());
        textCurrentTemperature.setText(String.valueOf(String.format(Locale.US, "%.1f", presenter.getKelvin(weatherResponse.getMain().getTemp()))) + "°");
        textTempMin.setText(String.valueOf(String.format(Locale.US, "%.1f", presenter.getKelvin(weatherResponse.getMain().getTempMin()))) + "°");
        textTempMax.setText(String.valueOf(String.format(Locale.US, "%.1f", presenter.getKelvin(weatherResponse.getMain().getTempMax()))) + "°");
        textHumidity.setText(String.valueOf(weatherResponse.getMain().getHumidity()));
        textDescription.setText(weatherResponse.getWeatherList().get(0).getDescription());
        textSunset.setText(presenter.getDate(weatherResponse.getSys().getSunset()));
        textSunrise.setText(presenter.getDate(weatherResponse.getSys().getSunrise()));

        ImageUtil.loadImage(Constants.BASE_URL_IMAGE + weatherResponse.getWeatherList().get(0).getIcon() + ".png", imageIcon, progressBarIcon, R.drawable.ic_launcher_background);
        setCardData();

        mMap.clear();
        latLng = new LatLng(weatherResponse.getCoord().getLat(), weatherResponse.getCoord().getLon());
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

        //Esconde teclado
        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(textLocation.getWindowToken(), 0);
        }
    }

    @Override
    public void showToastErrorResponse(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

}



