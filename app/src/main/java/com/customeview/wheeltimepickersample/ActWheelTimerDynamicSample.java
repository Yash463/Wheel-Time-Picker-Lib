package com.customeview.wheeltimepickersample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customeview.wheeltimepicker.WheelTimePickerView;

public class ActWheelTimerDynamicSample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_wheel_timer_dynamic_sample);

        final TextView txtTimerValue = (TextView) findViewById(R.id.txtTimerValue);
        int valueInPixels = (int) getResources().getDimension(R.dimen.timer_view_height_width);
        RelativeLayout rlLayout = (RelativeLayout) findViewById(R.id.rlLayout);
        WheelTimePickerView wheelTimerLayout = new WheelTimePickerView(this, valueInPixels, valueInPixels);
        wheelTimerLayout.setTimerTextSize(25);
        wheelTimerLayout.setNumeralTestSize(50);
        wheelTimerLayout.setViewBackGroundColor(Color.RED);
        wheelTimerLayout.drawView();
        wheelTimerLayout.setTimeChangeListener(new WheelTimePickerView.OnTimeChangeListener() {
            @Override
            public void onTimeChange(String hour, String minutes) {
                txtTimerValue.setText(hour + ":" + minutes);
            }
        });
        rlLayout.addView(wheelTimerLayout);
    }

}
