package firespace.com.polutiondetector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Brian on 12/3/2016.
 */

public class GoogleMatrixRequest {

    private static final String API_KEY = "AIzaSyCsSixxnzPX4pIN4F7UNP4ot0rSJVbW7hU";

    Context mContext;

    int finalTravelDuration = -1;   //initially set to -1 to determine if it is later changed

    OkHttpClient client = new OkHttpClient();

    boolean wantMapsApi = false;
    boolean wantWeatherApi = false;


    public GoogleMatrixRequest (Context context){
        this.mContext = context;
    }


    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String makeRequestUrl(Double coords1[], Double coords2[]) {
        String origin=coords1[0] + "," + coords1[1];
        String destination = coords2[0] + "," + coords2[1];

        String url_request = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origin + "&destinations=" + destination + "&mode=driving&language=en&key=" + API_KEY;

        return url_request;
    }

    public String getResponse(String url) {

        String response = null;
        try {
            response = run(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public int getDurationFromJSON(String response) throws JSONException {
        int duration = 0;

        JSONObject obj = new JSONObject(response);

        String number = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").get("value").toString();
        duration = Integer.parseInt(number);

        return duration;

    }



    private class GetResponseTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        protected String doInBackground(String... strings){
            System.out.println("strings[0] = " + strings[0]);

            return getResponse(strings[0]); //returns the response json

        }

        protected void onPostExecute(String response){

            if  (wantMapsApi) {
                int durationFromJSON = 0;
                try {
                    durationFromJSON = getDurationFromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("durationFromJSON = " + durationFromJSON);

                finalTravelDuration = durationFromJSON;
            }

            progressDialog.cancel();

        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog((Activity) mContext);
            progressDialog.setMessage("Working...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

    }

    public  int getRouteDuration(Double coords1[], Double coords2[]) throws JSONException {

        wantMapsApi = true;
        wantWeatherApi = false;

        String url = makeRequestUrl(coords1, coords2);


        try {
            String result = new GetResponseTask().execute(url, url, url).get();

            int durationFromJSON = 0;
            try {
                durationFromJSON  = getDurationFromJSON(result);
                finalTravelDuration = durationFromJSON;
                System.out.println("in getRouteDuration, finalTravelDuration = " + finalTravelDuration);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return finalTravelDuration;
    }

    public Weather getWeatherInfo(Double[] coords) throws JSONException {

        wantMapsApi = false;
        wantWeatherApi = true;

        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + coords[0] + "&lon=" + coords[1] + "&apikey=95ebe25e0a17df1629358645cb80bed3";

        try {
            String result = new GetResponseTask().execute(url, url, url).get();

            JSONObject obj = new JSONObject(result);

            double humidityResult = Double.parseDouble(obj.getJSONObject("main").getString("humidity"));
            double pressureResult = Double.parseDouble(obj.getJSONObject("main").getString("pressure"));

            System.out.println("weatherinfo result: humidity=" + humidityResult + ", pressure=" + pressureResult);
            Weather weather = new Weather(humidityResult, pressureResult);

            return weather;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;

    }


}