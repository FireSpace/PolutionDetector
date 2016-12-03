package firespace.com.polutiondetector;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Button settingsButton;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int listLength = 30;
    public ArrayList<Double> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingsButton = (Button) findViewById(R.id.settings);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            List<Sensor> pressure = mSensorManager.getSensorList(Sensor.TYPE_PRESSURE);
            System.out.println("There is air pressure");
        } else {
            System.out.println("There is no gyroscope");
        }


        if (mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
            List<Sensor> pressure = mSensorManager.getSensorList(Sensor.TYPE_RELATIVE_HUMIDITY);
            System.out.println("There is a HUMIDITY");
        } else {
            System.out.println("There is no HUMIDITY");
        }


        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Settings.class));
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double millibars_of_pressure = sensorEvent.values[0];
        double humidity = sensorEvent.values[0];

        if(list.size() <= listLength) {
            list.add(millibars_of_pressure);
        }

        if(list.size() == listLength) {
            System.out.println("The max is: " + Collections.max(list)+ " The min is: " + Collections.min(list));
            onPause();
        }

        if (millibars_of_pressure >= Collections.min(list) && millibars_of_pressure <= Collections.min(list)) {
            System.out.println("Your ambient air pressure is acceptable");
        }

        else if(millibars_of_pressure <= Collections.min(list) && millibars_of_pressure >= Collections.min(list)){
            System.out.println("Your ambient air pressure is too high. There may be dangerous levels of pollutants");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}