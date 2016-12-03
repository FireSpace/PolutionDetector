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

    double millibars_of_pressure;
    Button settingsButton;
    private SensorManager sensorManager;
    private Sensor temperature;
    private Sensor humidity;
    private Sensor pressure;
    private int listLength = 30;
    public ArrayList<Double> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingsButton = (Button) findViewById(R.id.settings);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            List<Sensor> pressure = sensorManager.getSensorList(Sensor.TYPE_PRESSURE);
            System.out.println("There is air pressure");
        } else {
            System.out.println("There is no gyroscope");
        }


        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
            List<Sensor> pressure = sensorManager.getSensorList(Sensor.TYPE_RELATIVE_HUMIDITY);
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
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            double x = sensorEvent.values[0];
            System.out.println("The temp is: " + x);
        }

        else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            double x = sensorEvent.values[0];
            System.out.println("The humid is: " + x);
        }

        else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            double x = sensorEvent.values[0];
            millibars_of_pressure = x;
            if (list.size() <= listLength) {
                list.add(x);
            }

            if (list.size() == listLength) {
                sensorManager.unregisterListener(this, pressure);
                sensorManager.unregisterListener(this, temperature);
                sensorManager.unregisterListener(this, humidity);
                System.out.println("The max is: " + Collections.max(list) + "The min is: " + Collections.min(list));
            }
        }

        if (millibars_of_pressure >= Collections.min(list) && millibars_of_pressure <= Collections.max(list)) {
            System.out.println("Your ambient air pressure is acceptable");
        }

        else if(millibars_of_pressure <= Collections.min(list) && millibars_of_pressure >= Collections.max(list)){
            System.out.println("Your ambient air pressure is too high. There may be dangerous levels of pollutants");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}