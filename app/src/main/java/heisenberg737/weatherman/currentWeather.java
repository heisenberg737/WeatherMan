package heisenberg737.weatherman;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class currentWeather extends Fragment implements View.OnClickListener {

    Button byCityName,byLatLon;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public currentWeather() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_current_weather, container, false);
        byCityName=view.findViewById(R.id.by_city_name);
        byLatLon=view.findViewById(R.id.by_lat_lon);
        byCityName.setOnClickListener(this);
        byLatLon.setOnClickListener(this);
        fragmentManager=getFragmentManager();


        return view;
    }

    @Override
    public void onClick(View v) {
         fragmentTransaction=fragmentManager.beginTransaction();
        if(v.getId()==R.id.by_city_name)
        {
            fragmentTransaction.replace(R.id.main_content,new currentWeatherByCityName(),null).addToBackStack(null).commit();
        }
        else if(v.getId()==R.id.by_lat_lon)
        {
            fragmentTransaction.replace(R.id.main_content,new currentWeatherByLatAndLon(),null).addToBackStack(null).commit();
        }


    }
}
