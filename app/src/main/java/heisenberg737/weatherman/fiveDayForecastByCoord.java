package heisenberg737.weatherman;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class fiveDayForecastByCoord extends Fragment implements View.OnClickListener {

    EditText lati,longi;
    Button showForecast,getCoordinates;
    TextView cityName,countryName,latitude,longitude;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<fiveDayForecastClass> arrayList=new ArrayList<>();
    fiveDayForecastAdapter adapter;
    String url,coordLat,coordLon,temp,press,hum,desc,windspeed,datetime;



    public fiveDayForecastByCoord() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_five_day_forecast_by_coord, container, false);

        lati=view.findViewById(R.id.five_day_forecast_by_coord_latitude_entry);
        longi=view.findViewById(R.id.five_day_forecast_by_coord_longitude_entry);

        cityName=view.findViewById(R.id.five_day_forecast_by_coord_city_name);
        countryName=view.findViewById(R.id.five_day_forecast_by_coord_country_name);
        longitude=view.findViewById(R.id.five_day_forecast_by_coord_longitude);
        latitude=view.findViewById(R.id.five_day_forecast_by_coord_latitude);

        recyclerView=view.findViewById(R.id.five_day_forecast_by_coord_list);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        showForecast=view.findViewById(R.id.five_day_forecast_by_coord_show);
        showForecast.setOnClickListener(this);
        getCoordinates=view.findViewById(R.id.getCoordinates);
        getCoordinates.setOnClickListener(this);



        return view;
    }

    public void Forecast(String coordLat,String coordLon)
    {

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

                        fiveDayForecastClass forecast=new fiveDayForecastClass(temp,press,hum,desc,windspeed,datetime);

                        arrayList.add(forecast);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(),"Something went wrong...",Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

        adapter=new fiveDayForecastAdapter(arrayList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.five_day_forecast_by_coord_show)
        {
            coordLon=longi.getText().toString();
            coordLat=lati.getText().toString();
            Forecast(coordLat,coordLon);
        }
        else if(v.getId()==R.id.getCoordinates)
        {
            Toast.makeText(getContext(),"hi",Toast.LENGTH_SHORT).show();
        }

    }
}
