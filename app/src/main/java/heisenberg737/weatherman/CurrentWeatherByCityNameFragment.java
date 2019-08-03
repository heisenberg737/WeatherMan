package heisenberg737.weatherman;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherByCityNameFragment extends Fragment {

    String url,city,country;
    TextView note,latitude,longitude,timezone,weather,visibility,temperature,pressure,humidity,wind_speed,cname;
    EditText city_name,country_name;
    Button show_weather;
    ProgressBar progressBar;
    int progressStatus=0;
    Handler handler=new Handler();


    public CurrentWeatherByCityNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_current_weather_by_city_name, container, false);
        note=view.findViewById(R.id.note);
        note.setMovementMethod(LinkMovementMethod.getInstance());

        progressBar=view.findViewById(R.id.current_weather_by_city_pb);




        city_name=view.findViewById(R.id.city_name);
        country_name=view.findViewById(R.id.country_name);
        pressure=view.findViewById(R.id.pressure);
        humidity=view.findViewById(R.id.humidity);
        wind_speed=view.findViewById(R.id.wind_speed);
        weather=view.findViewById(R.id.weather_desc);
        timezone=view.findViewById(R.id.timezone);
        longitude=view.findViewById(R.id.longitude);
        latitude=view.findViewById(R.id.latitude);
        visibility=view.findViewById(R.id.visibility);
        temperature=view.findViewById(R.id.temperature);
        cname=view.findViewById(R.id.cname);

        show_weather=view.findViewById(R.id.show_weather);
        show_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city=city_name.getText().toString();
                country=country_name.getText().toString().toLowerCase();
                if(TextUtils.isEmpty(city))
                    city_name.setError("Enter valid city name");
                else if(TextUtils.isEmpty(country))
                    country_name.setError("Enter valid country name");
                else
                {   progressBar.setVisibility(View.VISIBLE);
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

                    showWeather(city,country);
                }
            }
        });



        return view;
    }

    public void showWeather(String city,String country)
    {
        url="http://api.openweathermap.org/data/2.5/weather?q="+city+","+country+"&appid=157f04f54cb3229c013c8b608c9adf57";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject;
                JSONArray jsonArray;
                try {
                    visibility.setText(response.getString("visibility"));
                    timezone.setText(response.getString("timezone"));
                    cname.setText(response.getString("name"));
                    jsonObject=response.getJSONObject("coord");
                    latitude.setText(jsonObject.getString("lat"));
                    longitude.setText(jsonObject.getString("lon"));
                    jsonObject=response.getJSONObject("main");
                    temperature.setText(jsonObject.getString("temp"));
                    pressure.setText(jsonObject.getString("pressure"));
                    humidity.setText(jsonObject.getString("humidity"));
                    jsonObject=response.getJSONObject("wind");
                    wind_speed.setText(jsonObject.getString("speed"));
                    jsonArray=response.getJSONArray("weather");
                    jsonObject=jsonArray.getJSONObject(0);
                    weather.setText(jsonObject.getString("description"));



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(),"Something went wrong. Please check the entered fields and make sure you have there's an active internet connection.",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }

}
