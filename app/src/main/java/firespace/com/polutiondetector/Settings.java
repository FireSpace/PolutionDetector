package firespace.com.polutiondetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;

public class Settings extends AppCompatActivity {
    Button addLocation;
    EditText enterLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        addLocation = (Button)findViewById(R.id.addLocation);
        enterLocation = (EditText)findViewById(R.id.enterLocation);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String savedLocation = enterLocation.getText().toString();
                try{
                    PrintWriter writer = new PrintWriter("savedLocation.txt", "UTF-8");
                    writer.println(savedLocation);
                    writer.close();
                } catch (IOException e) {
                    // do something
                }
            }
        });
    }
}
