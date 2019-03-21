package com.example.tpeea.se0909_maxence;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Slider.SliderChangeListener {

    private Slider mSlider;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.textView);
        mSlider = findViewById(R.id.mySlider);
        mSlider.setListener(this);
        //mTextView.setText(String.valueOf(savedInstanceState.getFloat("Valeur")));
    }

    @Override
    public void onChange(float value) {
        mTextView.setText(String.valueOf(value));
    }
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat("Valeur", mSlider.getValue());
        super.onSaveInstanceState(outState);

    }*/
}
