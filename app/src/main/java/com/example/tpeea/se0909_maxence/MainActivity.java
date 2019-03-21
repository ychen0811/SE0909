package com.example.tpeea.se0909_maxence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Slider.SliderChangeListener {

    private Slider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slider.setListener(this);
        //onChange();
        setContentView(R.layout.activity_main);
    }
}
