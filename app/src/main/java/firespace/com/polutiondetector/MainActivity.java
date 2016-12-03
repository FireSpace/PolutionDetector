package firespace.com.polutiondetector;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    Context context;

    TextView textView;
    Button button;
    Button button2;
    Button button3;

    LocationStuff locationStuff;

    Double[] originalCoords = {40.948914, -74.334407}; //for testing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        textView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);

        locationStuff = new LocationStuff(context);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double[] coords = locationStuff.getCurrentCoordinates();

                System.out.println("Coords: " + coords[0] + ", " + coords[1]);
                originalCoords = coords;
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationStuff.withinRangeOfCurrentLocation(originalCoords, 5000)) {
                    System.out.println("We are within the range of the original location!");
                } else {
                    System.out.println("We are not within the range of ");
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleMatrixRequest request = new GoogleMatrixRequest(context);
                try {
                    Weather weather = request.getWeatherInfo(locationStuff.getCurrentCoordinates());
                    System.out.println("buttonclick: weather humidity = " + weather.humidity + ", presure = " + weather.pressure);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}

