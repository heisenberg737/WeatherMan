package heisenberg737.weatherman;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public class WeatherMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String OWM_Url="http://tile.openweathermap.org/map/%s/%d/%d/%d.png?appid=157f04f54cb3229c013c8b608c9adf57";
    Spinner spinner;
    TileOverlay tileOverlay;
    String tileType="clouds";
    String lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        spinner=findViewById(R.id.tileType);

        String[] tileName=new String[]{"Clouds", "Temperature", "Precipitations", "Snow", "Rain", "Wind", "Sea level press."};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tileName);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0:
                        tileType="clouds";
                        break;
                    case 1:
                        tileType="temp";
                        break;
                    case 2:
                        tileType = "precipitation";
                        break;
                    case 3:
                        tileType = "snow";
                        break;
                    case 4:
                        tileType = "rain";
                        break;
                    case 5:
                        tileType = "wind";
                        break;
                    case 6:
                        tileType = "pressure";
                        break;
                }
                if(mMap!=null)
                {
                    setUpMap();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in NITT and move the camera


        LatLng NITT = new LatLng(10.77, 78.82);
        mMap.addMarker(new MarkerOptions().position(NITT).title("Marker at your coordinates"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(NITT));
//        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(createTileProvider()));
    }

    public void setUpMap()
    {   //tileOverlay.remove();
        tileOverlay=mMap.addTileOverlay(new TileOverlayOptions().tileProvider(createTileProvider()));
    }


    private TileProvider createTileProvider()
    {
        TileProvider tileProvider=new UrlTileProvider(256,256) {
            @Override
            public URL getTileUrl(int i, int i1, int i2) {

                String fUrl=String.format(OWM_Url,tileType==null?"clouds":tileType,i2,i,i1);
                URL url=null;

                try {
                    url=new URL(fUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                return url;
            }
        };

        if(tileOverlay!=null)
            tileOverlay.remove();

        return tileProvider;
    }

}
