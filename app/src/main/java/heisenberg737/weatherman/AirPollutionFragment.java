package heisenberg737.weatherman;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AirPollutionFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    Button CO,SO2,NO2,O3;
    TextView latitude,longitude,time,gasName;
    EditText getLatitude,getLongitude,selectDate;
    RecyclerView.LayoutManager layoutManager;
    String gas,longi,lati,date;
    AirPollutionValuesAdapter adapter;
    Handler handler=new Handler();
    ProgressBar progressBar;
    int progressStatus;

    ArrayList<AirPollutionValues> arrayList=new ArrayList<>();


    public AirPollutionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_air_pollution, container, false);

        CO=view.findViewById(R.id.co_level);
        O3=view.findViewById(R.id.o3_level);
        NO2=view.findViewById(R.id.no2_level);
        SO2=view.findViewById(R.id.so2_level);
        selectDate=view.findViewById(R.id.setDate);


        latitude=view.findViewById(R.id.air_pollution_latitude);
        longitude=view.findViewById(R.id.air_pollution_longitude);
        time=view.findViewById(R.id.air_pollution_time);
        gasName=view.findViewById(R.id.selectedGas);

        progressBar=view.findViewById(R.id.air_pollution_pb);

        getLatitude=view.findViewById(R.id.air_pollution_lat_entry);
        getLongitude=view.findViewById(R.id.air_pollution_lon_entry);


        CO.setOnClickListener(this);
        NO2.setOnClickListener(this);
        O3.setOnClickListener(this);
        SO2.setOnClickListener(this);
        selectDate.setOnClickListener(this);



        recyclerView=view.findViewById(R.id.air_pollution_list);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new AirPollutionValuesAdapter(arrayList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        lati=getLatitude.getText().toString();
        longi=getLongitude.getText().toString();
        date=selectDate.getText().toString();
        if(v.getId()==R.id.co_level)
        {
            gas="co";
        }
        else if(v.getId()==R.id.o3_level) {
            gas = "o3";
        }
        else if(v.getId()==R.id.so2_level)
        {
            gas="so2";
        }
        else if(v.getId()==R.id.no2_level)
        {
            gas="no2";
        }

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

        getData(gas,lati,longi,date);

    }
    public void getData(String gas,String lati,String longi,String date)
    {   gasName.setText(gas.toUpperCase());
        String url="http://api.openweathermap.org/pollution/v1/"+gas+"/"+lati+","+longi+"/"+date+"Z.json?appid=157f04f54cb3229c013c8b608c9adf57";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONObject jsonObject=response.getJSONObject("location");
                    latitude.setText(jsonObject.getString("latitude"));
                    longitude.setText(jsonObject.getString("longitude"));
                    time.setText(response.getString("time"));
                    JSONArray jsonArray=response.getJSONArray("data");
                    if(jsonArray.length()==0)
                    {
                        Toast.makeText(getContext(),"No Data Found. Please search using a different location or date or gas.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                      for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                         AirPollutionValues pollutionValues=new AirPollutionValues(jsonObject1.getString("precision"),jsonObject1.getString("pressure"),jsonObject1.getString("value"));
                         arrayList.add(pollutionValues);
                      }
                    }

//                    else
//                    {  String message=response.getString("message");
//                    if(message.equals("not found"))
//                    Toast.makeText(getContext(),"No Data Found. Please search using a different location or date.",Toast.LENGTH_SHORT).show();
//                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(getContext()==null)
                Toast.makeText(getContext(),"Something went wrong..",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        adapter.swapData(arrayList);
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }
}
