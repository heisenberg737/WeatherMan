package heisenberg737.weatherman;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiveDayForecastFragment extends Fragment implements View.OnClickListener {

    Button byCity,byCoordinates;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public FiveDayForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_five_day_forecast, container, false);

        byCity=view.findViewById(R.id.five_day_forecast_by_city);
        byCoordinates=view.findViewById(R.id.five_day_forecast_by_coordinates);

        byCity.setOnClickListener(this);
        byCoordinates.setOnClickListener(this);

        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();

        return view;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.five_day_forecast_by_city)
        {
            fragmentTransaction.replace(R.id.main_content,new FiveDayForecastByCityFragment(),null).addToBackStack(null).commit();
        }
        else if(v.getId()==R.id.five_day_forecast_by_coordinates)
        {
            fragmentTransaction.replace(R.id.main_content,new FiveDayForecastByCoordFragment(),null).addToBackStack(null).commit();
        }

    }
}
