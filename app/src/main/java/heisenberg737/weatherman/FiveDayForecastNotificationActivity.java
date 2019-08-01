package heisenberg737.weatherman;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FiveDayForecastNotificationActivity extends AppCompatActivity {

    TextView cityName,countryName,latitude,longitude;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<FiveDayForecastClass> arrayList=new ArrayList<>();
    FiveDayForecastAdapter adapter;
    String url,coordLat,coordLon,temp,press,hum,desc,windspeed,datetime;
    ProgressBar progressBar;
    int progressStatus=0;
    Handler handler=new Handler();
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_day_forecast_notification);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Forecast");

        cityName=findViewById(R.id.five_day_forecast_notification_city_name);
        countryName=findViewById(R.id.five_day_forecast_notification_country_name);
        latitude=findViewById(R.id.five_day_forecast_notification_latitude);
        longitude=findViewById(R.id.five_day_forecast_notification_longitude);

        progressBar=findViewById(R.id.five_day_forecast_notification_pb);

        recyclerView=findViewById(R.id.five_day_forecast_notification_list);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new FiveDayForecastAdapter(arrayList);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences=getSharedPreferences("Location", Context.MODE_PRIVATE);
        coordLat=sharedPreferences.getString("Latitude","10.77");
        coordLon=sharedPreferences.getString("Longitude","78.82");

        progressBar.setVisibility(View.VISIBLE);
        progressStatus=0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus<100)
                    progressStatus+=1;

                try {
                    Thread.sleep(20);

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        progressBar.setProgress(progressStatus);

                        if(progressStatus==100)
                            progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        Forecast(coordLat,coordLon);


    }

    public void Forecast(String coordLat, String coordLon)
    {   arrayList.clear();

        url="http://api.openweathermap.org/data/2.5/forecast?lat="+coordLat+"&lon="+coordLon+"&appid=157f04f54cb3229c013c8b608c9adf57";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    JSONObject jsonObject=response.getJSONObject("city");
                    cityName.setText(jsonObject.getString("name"));
                    JSONObject object=jsonObject.getJSONObject("coord");
                    latitude.setText(object.getString("lat"));
                    longitude.setText(object.getString("lon"));
                    countryName.setText(jsonObject.getString("country"));

                    JSONArray jsonArray=response.getJSONArray("list");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        jsonObject=jsonArray.getJSONObject(i);
                        JSONObject jsonObject1=jsonObject.getJSONObject("main");
                        temp=jsonObject1.getString("temp");
                        press=jsonObject1.getString("pressure");
                        hum=jsonObject1.getString("humidity");
                        jsonObject1=jsonObject.getJSONObject("wind");
                        windspeed=jsonObject1.getString("speed");
                        datetime=jsonObject.getString("dt_txt");
                        JSONArray jsonArray1=jsonObject.getJSONArray("weather");
                        jsonObject1=jsonArray1.getJSONObject(0);
                        desc=jsonObject1.getString("description");

                        FiveDayForecastClass forecast=new FiveDayForecastClass(temp,press,hum,desc,windspeed,datetime);

                        arrayList.add(forecast);


                    }
                    adapter.swapData(arrayList);
                    Log.d("ArrayList",arrayList.size()+"");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Something went wrong...",Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);




    }

}
