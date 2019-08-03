package heisenberg737.weatherman;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiveDayForecastByCoordFragment extends Fragment implements View.OnClickListener {

    EditText lati,longi;
    Button showForecast,getCoordinates;
    TextView cityName,countryName,latitude,longitude,conclusion;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<FiveDayForecastClass> arrayList=new ArrayList<>();
    FiveDayForecastAdapter adapter;
    String url,coordLat,coordLon,temp,press,hum,desc,windspeed,datetime;
    FusedLocationProviderClient fusedLocationProviderClient;
    int LOCATION_PERMISSION_CODE=1,rain_counter=0;
    ProgressBar progressBar;
    int progressStatus=0;
    Handler handler=new Handler();
    float avg_temp=0,avg_windspeed=0;



    public FiveDayForecastByCoordFragment() {
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
        conclusion=view.findViewById(R.id.five_day_forecast_by_coord_fact);

        progressBar=view.findViewById(R.id.five_day_forecast_by_coord_pb);

        recyclerView=view.findViewById(R.id.five_day_forecast_by_coord_list);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new FiveDayForecastAdapter(arrayList);
        recyclerView.setAdapter(adapter);


        showForecast=view.findViewById(R.id.five_day_forecast_by_coord_show);
        showForecast.setOnClickListener(this);
        getCoordinates=view.findViewById(R.id.getCoordinates);
        getCoordinates.setOnClickListener(this);




        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());

        return view;
    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.five_day_forecast_by_coord_show)
        {
            coordLon=longi.getText().toString();
            coordLat=lati.getText().toString();
            if(TextUtils.isEmpty(coordLat))
                lati.setError("Enter a valid Latitude");
            else if(TextUtils.isEmpty(coordLon))
                longi.setError("Enter a valid Longitude");
            else {
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

        }
        else if(v.getId()==R.id.getCoordinates)
        {
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if(location!=null)
                        {
                            coordLat=String.valueOf(location.getLatitude());
                            coordLon=String.valueOf(location.getLongitude());

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

                    }
                });
            } else {
                requestLocationPermission();
            }
        }


    }

    public void requestLocationPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION))
        {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission Needed")
                    .setMessage("This app needs your location for this feature")
                    .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==LOCATION_PERMISSION_CODE)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

                        avg_temp=avg_temp+Float.parseFloat(temp);
                        avg_temp=avg_temp/40;

                        press=jsonObject1.getString("pressure");
                        hum=jsonObject1.getString("humidity");
                        jsonObject1=jsonObject.getJSONObject("wind");
                        windspeed=jsonObject1.getString("speed");

                        avg_windspeed=avg_windspeed+Float.parseFloat(windspeed);
                        avg_windspeed=avg_windspeed/40;

                        datetime=jsonObject.getString("dt_txt");
                        JSONArray jsonArray1=jsonObject.getJSONArray("weather");
                        jsonObject1=jsonArray1.getJSONObject(0);
                        desc=jsonObject1.getString("description");

                        if (desc.contains("rain"))
                        {
                            ++rain_counter;
                        }

                        FiveDayForecastClass forecast=new FiveDayForecastClass(temp,press,hum,desc,windspeed,datetime);

                        arrayList.add(forecast);


                    }
                    adapter.swapData(arrayList);

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
        setFact();

    }

    public void setFact()
    {
        if(avg_temp>288&&avg_temp<308)
        {
            conclusion.setText(R.string.SolarPowerfact);
        }
        else if(avg_temp>308)
        {
            conclusion.setText("Very hot weather conditions. Solar cells in use may get damaged. Cautioned usage is advised.");
        }
        else if(rain_counter>7)
        {
            conclusion.setText(R.string.Rain);



        }
        else if(avg_windspeed>3.5)
        {
            conclusion.setText(R.string.wind);
        }

    }

}
