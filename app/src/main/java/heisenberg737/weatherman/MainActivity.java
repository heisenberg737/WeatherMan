package heisenberg737.weatherman;

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
        fragmentTransaction1.replace(R.id.main_content,new introFragment(),null).addToBackStack(null).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment=new introFragment();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                switch (menuItem.getItemId())
                {
                    case R.id.current_weather:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        fragment=new currentWeather();
                        toolbar.setTitle("Current Weather");
                        getSupportFragmentManager().popBackStack();
                        break;
                    case R.id.air_pollution:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("Air Pollution");
                        fragment=new AirPollution();
                        getSupportFragmentManager().popBackStack();
                        break;
                    case R.id.uv_index:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("UV Index");
                        fragment=new UVIndex();
                        getSupportFragmentManager().popBackStack();
                        break;
                    case R.id.five_day_forecast:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("UV Index");
                        fragment=new fiveDayForecast();
                        getSupportFragmentManager().popBackStack();
                        break;

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
