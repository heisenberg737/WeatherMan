package heisenberg737.weatherman;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ForecastReciever extends BroadcastReceiver {

    String url,coordLat,coordLon,description,dt_txt,hour="",desc;
    int hourOfDay;
    SharedPreferences sharedPreferences;

    public ForecastReciever(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1=new Intent(context,FiveDayForecastNotificationActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        hourOfDay= Calendar.HOUR_OF_DAY;

        PendingIntent pendingIntent=PendingIntent.getActivity(context,100,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        final Notification.Builder builder=new Notification.Builder(context);
            builder.setContentTitle("Forecast");

        sharedPreferences=context.getSharedPreferences("Location",Context.MODE_PRIVATE);
        coordLat=sharedPreferences.getString("Latitude","10.77");
        coordLon=sharedPreferences.getString("Longitude","78.82");

        DateFormat dateFormat=DateFormat.getTimeInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("utc"));
        String gmtTime=dateFormat.format(new Date());
        Log.d("Date",gmtTime);

        url="http://api.openweathermap.org/data/2.5/forecast?lat="+coordLat+"&lon="+coordLon+"&appid=157f04f54cb3229c013c8b608c9adf57";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray=response.getJSONArray("list");
                    for(int i=0;i<1;i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        JSONObject jsonObject1;
                        dt_txt=jsonObject.getString("dt_txt");
                        hour=hour+dt_txt.charAt(11)+dt_txt.charAt(12);



                        JSONArray jsonArray1=jsonObject.getJSONArray("weather");
                        jsonObject1=jsonArray1.getJSONObject(0);
                        description=jsonObject1.getString("description");




                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


        builder.setContentText("The weather forecast is ready. Tap to get the full forecast");
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_notification_icon);

        notificationManager.notify(100,builder.build());

    }
}