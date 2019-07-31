package heisenberg737.weatherman;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FiveDayForecastAdapter extends RecyclerView.Adapter<FiveDayForecastAdapter.fiveDayForecastViewHolder> {

    ArrayList<FiveDayForecastClass> arrayList=new ArrayList<>();

    public FiveDayForecastAdapter(ArrayList<FiveDayForecastClass> arrayList)
    {
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public fiveDayForecastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.five_day_forecast_single_row_item,viewGroup,false);
        fiveDayForecastViewHolder viewHolder=new fiveDayForecastViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull fiveDayForecastViewHolder viewHolder, int i) {

        viewHolder.windspeed.setText(arrayList.get(i).getWindspeed());
        viewHolder.datetime.setText(arrayList.get(i).getDatetime());
        viewHolder.temperature.setText(arrayList.get(i).getTemperature());
        viewHolder.humidity.setText(arrayList.get(i).getHumidity());
        viewHolder.description.setText(arrayList.get(i).getDescription());
        viewHolder.pressure.setText(arrayList.get(i).getPressure());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void swapData(ArrayList<FiveDayForecastClass> newList)
    {
        arrayList=newList;
        notifyDataSetChanged();
    }

    public class fiveDayForecastViewHolder extends RecyclerView.ViewHolder{

        TextView temperature,pressure,humidity,description,datetime,windspeed;

        public fiveDayForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            temperature=itemView.findViewById(R.id.five_day_forecast_temperature);
            pressure=itemView.findViewById(R.id.five_day_forecast_pressure);
            humidity=itemView.findViewById(R.id.five_day_forecast_humidity);
            description=itemView.findViewById(R.id.five_day_forecast_description);
            datetime=itemView.findViewById(R.id.five_day_forecast_date_time);
            windspeed=itemView.findViewById(R.id.five_day_forecast_wind_speed);

        }
    }

}
