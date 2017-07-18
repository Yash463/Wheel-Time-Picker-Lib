package com.customeview.wheeltimepickersample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.customeview.wheeltimepicker.WheelTimePickerView;

public class ActWheelTimerXMLSample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_wheel_timer_xmlsample);

        final TextView txtTimerValue = (TextView) findViewById(R.id.txtTimerValue);
        WheelTimePickerView wheelTimerView = (WheelTimePickerView) findViewById(R.id.wheelTimerView);
        wheelTimerView.setTimeChangeListener(new WheelTimePickerView.OnTimeChangeListener() {
            @Override
            public void onTimeChange(String hour, String minutes) {
                txtTimerValue.setText(hour + ":" + minutes);
            }
        });

        Button btnDynamicCodeSample = (Button) findViewById(R.id.btnDynamicCodeSample);
        btnDynamicCodeSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActWheelTimerXMLSample.this, ActWheelTimerDynamicSample.class));
            }
        });
    }
}
