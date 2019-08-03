package heisenberg737.weatherman;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherByLatAndLonFragment extends Fragment implements View.OnClickListener {

    String url, lati, longi;
    TextView latitude, longitude, timezone, weather, temperature, pressure, humidity, wind_speed, cname;
    EditText lat, lon;
    Button show_weather, getLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    int LocationPermissioncode=1,progressStatus=0;
    ProgressBar progressBar;
    Handler handler=new Handler();
    Context context;
    SharedPreferences sharedPreferences;

    public CurrentWeatherByLatAndLonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_current_weather_by_lat_and_lon, container, false);
        getLocation = view.findViewById(R.id.getLocation);
        lat = view.findViewById(R.id.lati);
        lon = view.findViewById(R.id.longi);
        pressure = view.findViewById(R.id.pressure_lat_lon);
        humidity = view.findViewById(R.id.humidity_lat_lon);
        wind_speed = view.findViewById(R.id.wind_speed_lat_lon);
        weather = view.findViewById(R.id.weather_desc_lat_lon);
        timezone = view.findViewById(R.id.timezone_lat_lon);
        longitude = view.findViewById(R.id.longitude_lat_lon);
        latitude = view.findViewById(R.id.latitude_lat_lon);
        temperature = view.findViewById(R.id.temperature_lat_lon);
        cname = view.findViewById(R.id.cname_lat_lon);
        show_weather = view.findViewById(R.id.show_weather_lat_lon);

        context=getContext();


        progressBar=view.findViewById(R.id.current_weather_by_coord_pb);

        show_weather.setOnClickListener(this);
        getLocation.setOnClickListener(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.show_weather_lat_lon) {
            lati = lat.getText().toString();
            longi = lon.getText().toString();
            if (TextUtils.isEmpty(lati)) {
                lat.setError("Enter a valid latitude");
            } else if (TextUtils.isEmpty(longi)) {
                lon.setError("Enter a valid longitude");
            } else {

                progressBar.setVisibility(View.VISIBLE);
                progressStatus=0;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(progressStatus<100)
                            progressStatus+=1;

                        try {
                            Thread.sleep(30);

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

                ShowWeather(lati, longi);
            }
        }
        if (v.getId() == R.id.getLocation) {

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location == null)
                            Toast.makeText(getContext(), "Switch on location!", Toast.LENGTH_LONG).show();
                        else {
                            String lati = String.valueOf(location.getLatitude());
                            String longi = String.valueOf(location.getLongitude());

                            sharedPreferences=context.getSharedPreferences("Location",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("Latitude",lati);
                            editor.putString("Longitude",longi);
                            editor.apply();


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

                            ShowWeather(lati, longi);
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
                            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LocationPermissioncode);

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
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LocationPermissioncode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==LocationPermissioncode)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }

    public void ShowWeather(String lati,String longi)
    {
        url="http://api.openweathermap.org/data/2.5/weather?lat="+lati+"&lon="+longi+"&appid=157f04f54cb3229c013c8b608c9adf57";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject;
                JSONArray jsonArray;
                try {

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

                Toast.makeText(getContext(),"Something went wrong. Please check the entered fields and make sure you have there's an active interenet connection.",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }
}
