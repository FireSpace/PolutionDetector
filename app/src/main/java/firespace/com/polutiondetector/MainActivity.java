package firespace.com.polutiondetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor temperature;
    private Sensor humidity;
    private Sensor pressure;
    private int listLength = 30;
    double constant = 216.7;
    public ArrayList<Double> list = new ArrayList<Double>();
    public ArrayList<Double> listH = new ArrayList<Double>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
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
            if (list.size() <= 49) {
                list.add(x);
            }

            if(list.size() == 49) {
                sensorManager.unregisterListener(this, pressure);
                System.out.println("The max is: " + Collections.min(list) + "The min is: " + Collections.min(list));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
