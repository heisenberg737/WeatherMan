package heisenberg737.weatherman;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AirPollutionValuesAdapter extends RecyclerView.Adapter<AirPollutionValuesAdapter.MyViewHolder> {

    ArrayList<AirPollutionValues> arrayList=new ArrayList<>();
    public AirPollutionValuesAdapter(ArrayList<AirPollutionValues> arrayList)
    {
      this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.air_pollution_list_single_row_item,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {

        viewHolder.value.setText(arrayList.get(i).getValue());
        viewHolder.pressure.setText(arrayList.get(i).getPressure());
        viewHolder.precision.setText(arrayList.get(i).getPrecision());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void swapData(ArrayList<AirPollutionValues> newList)
    {
        arrayList=newList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
         TextView precision,pressure,value;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            precision=itemView.findViewById(R.id.precision);
            pressure=itemView.findViewById(R.id.pressure);
            value=itemView.findViewById(R.id.value);
        }
    }

}
