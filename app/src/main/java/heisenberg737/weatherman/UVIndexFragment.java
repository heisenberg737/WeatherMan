package heisenberg737.weatherman;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
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

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class UVIndexFragment extends Fragment implements View.OnClickListener{
    TextView latitude,longitude,value,date_iso;
    EditText lat,lon;
    Button getLocation,showUVIndex;
    String lati,longi;
    FusedLocationProviderClient fusedLocationProviderClient;
    int LOCATION_PERMISSION_INDEX=1;
    int progressStatus=1;
    ProgressBar progressBar;
    Handler handler=new Handler();


    public UVIndexFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_uvindex, container, false);
        lat=view.findViewById(R.id.uv_index_lati);
        lon=view.findViewById(R.id.uv_index_longi);
        getLocation=view.findViewById(R.id.uv_index_get_location);
        showUVIndex=view.findViewById(R.id.show_uv_index);
        latitude=view.findViewById(R.id.uv_index_latitude);
        longitude=view.findViewById(R.id.uv_index_longitude);
        value=view.findViewById(R.id.uv_index);
        date_iso=view.findViewById(R.id.uv_index_date_iso);

        progressBar=view.findViewById(R.id.uv_index_pb);

        getLocation.setOnClickListener(this);
        showUVIndex.setOnClickListener(this);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());
        return view;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.show_uv_index)
        {
            lati=lat.getText().toString();
            longi=lon.getText().toString();
            if(TextUtils.isEmpty(lati))
                lat.setError("Enter a valid latitude");
            else if(TextUtils.isEmpty(longi))
                lon.setError("Enter a valid longitude");
            else
            {
                progressStatus=0;
                progressBar.setVisibility(View.VISIBLE);

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

                getUVIndex(lati,longi);
            }
        }
        else if(v.getId()==R.id.getLocation)
        {
               if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
               {
                   fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                       @Override
                       public void onSuccess(Location location) {
                           if(location!=null)
                           {
                               lati= String.valueOf(location.getLatitude());
                               longi=String.valueOf(location.getLongitude());

                               progressStatus=0;
                               progressBar.setVisibility(View.VISIBLE);

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

                               getUVIndex(lati,longi);

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
                            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_INDEX);

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
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_INDEX);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==LOCATION_PERMISSION_INDEX)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }



    public void getUVIndex(String lati,String longi)
    {
        String url="http://api.openweathermap.org/data/2.5/uvi?appid=157f04f54cb3229c013c8b608c9adf57&lat="+lati+"&lon="+longi;

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    latitude.setText(response.getString("lat"));
                    longitude.setText(response.getString("lon"));
                    value.setText(response.getString("value"));
                    date_iso.setText(response.getString("date_iso"));
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
