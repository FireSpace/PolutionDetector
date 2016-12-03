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
import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int listLength = 30;
    public ArrayList<Double> list = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null){
            List<Sensor> pressure = mSensorManager.getSensorList(Sensor.TYPE_PRESSURE);
            System.out.println("There is an air pressure");
        }
        else {
            System.out.println("There is no gyroscope");
        }
    }



    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        final double millibars_of_pressure = event.values[0];
        if(list.size() <= listLength) {
            list.add(millibars_of_pressure);
        }

        if(list.size() == listLength) {
            System.out.println("The max is: " + Collections.max(list)+ " The min is: " + Collections.min(list));
            onPause();
        }
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
