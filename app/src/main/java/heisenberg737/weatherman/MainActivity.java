package heisenberg737.weatherman;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity  {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    FragmentManager fragmentManager,fragmentManager1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_icon);

        drawerLayout=findViewById(R.id.intro);
        navigationView=findViewById(R.id.navigation);
        fragmentManager=getSupportFragmentManager();
        fragmentManager1=getSupportFragmentManager();

        if(findViewById(R.id.intro)!=null)
        {
               if(savedInstanceState!=null)
                   return;
        }

        FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
        fragmentTransaction1.replace(R.id.main_content,new IntroFragment(),null).addToBackStack(null).commit();

        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Intent intent=new Intent(getApplicationContext(),ForecastReciever.class);

        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment=new IntroFragment();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                switch (menuItem.getItemId())
                {
                    case R.id.current_weather:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        fragment=new CurrentWeatherFragment();
                        toolbar.setTitle("Current Weather");
                        getSupportFragmentManager().popBackStack();
                        break;
                    case R.id.air_pollution:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("Air Pollution");
                        fragment=new AirPollutionFragment();
                        getSupportFragmentManager().popBackStack();
                        break;
                    case R.id.uv_index:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("UV Index");
                        fragment=new UVIndexFragment();
                        getSupportFragmentManager().popBackStack();
                        break;
                    case R.id.five_day_forecast:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("5 days/3 hour Forecast ");
                        fragment=new FiveDayForecastFragment();
                        getSupportFragmentManager().popBackStack();
                        break;
                    case R.id.see_weather_map:
                        Intent intent1=new Intent(getApplicationContext(),WeatherMapsActivity.class);
                        startActivity(intent1);

                }
                fragmentTransaction.replace(R.id.main_content,fragment,null).commit();


                return false;
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
