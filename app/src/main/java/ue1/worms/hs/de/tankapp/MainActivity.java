package ue1.worms.hs.de.tankapp;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.MANAGE_DOCUMENTS;
import static ue1.worms.hs.de.tankapp.R.menu.options_menu;

import android.support.v7.app.ActionBar;

public class MainActivity extends AppCompatActivity implements   GoogleApiClient.ConnectionCallbacks,  AdapterView.OnItemSelectedListener ,GoogleApiClient.OnConnectionFailedListener {

    private  ListView listView;
    private  ListView listView1;
    private  ListView listView2;
    private TabHost tabHost;
    private MapView mapView;
    private Spinner spinner;
    private GoogleMap mMap;
    private ActionBar toolbar;

    public String RADIUS;

    public ArrayList<Station> stationsList = new ArrayList<>();
    public ArrayList<Station> stationsList1 = new ArrayList<>();
    public ArrayList<Station> stationsList2 = new ArrayList<>();
    public ArrayList<Details> detailsList = new ArrayList<>();
    public ArrayList<LatLng> markers = new ArrayList<>();

    public  StationAdapter stationAdapter;

    private static final int RequestPermissionCode = 1;

    private  GoogleApiClient googleApiClient;

    private FusedLocationProviderClient fusedLocationProviderClient;

    public double latitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle( "Tankstellen" );

        listView = findViewById(R.id.listView);
        listView1 = findViewById(R.id.listView1);
        listView2 = findViewById(R.id.listView2);

        tabHost = findViewById( R.id.tabhost1 );
        tabHost.setup();

        TabHost.TabSpec specs = tabHost.newTabSpec("Tag1") .setContent( R.id.tab1 ).setIndicator( "Diesel" );
        tabHost.addTab( specs );

        specs = tabHost.newTabSpec("Tag2").setContent(R.id.tab2).setIndicator( "Super E5" );
        tabHost.addTab( specs );

        specs = tabHost.newTabSpec("Tag3").setContent(R.id.tab3).setIndicator( "Super E10" );
        tabHost.addTab( specs );


        tabHost.setOnTabChangedListener( new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(("Tag2").equals( tabId )){
                    stationAdapter = new StationAdapter( MainActivity.this, stationsList1);
                    stationAdapter.notifyDataSetChanged();
                    listView1.setAdapter( stationAdapter );

                }
                if(("Tag3").equals( tabId )){
                    stationAdapter = new StationAdapter( MainActivity.this, stationsList2);
                    stationAdapter.notifyDataSetChanged();
                    listView2.setAdapter( stationAdapter );
                }
            }
        } );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
            }
        });



        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                setAndFetchLocation(location);
                            }
                        }
                    });
        }
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("MainActivity", "Connection suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("MainActivity", "Connection failed: " + connectionResult.getErrorCode());

    }

    private void setAndFetchLocation(Location location) {
        FetchDataFromApi fetchDataFromApi = new FetchDataFromApi(location);
        fetchDataFromApi.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }


    public class FetchDataFromApi extends AsyncTask<Void, Void, Void> {

        public String jsonData = "";

        Location mLoc;
        public int i;
        private Spinner spinner1;
        private Spinner spinner2;

        public FetchDataFromApi(){}

        public FetchDataFromApi(Location location) {
            mLoc = location;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute( aVoid );
            stationAdapter = new StationAdapter( MainActivity.this, stationsList );
            stationAdapter.notifyDataSetChanged();
            listView.setAdapter( stationAdapter );


            listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    String currentStationName = stationsList.get( position ).getBrand();
                    String currentStationStreet = stationsList.get( position ).getStreet();
                    String currentStationPlace = stationsList.get( position ).getPlace();
                    String currentStationDiesel = detailsList.get( position ).getDiesel();
                    String currentStationE5 = detailsList.get( position ).getE_5();
                    String currentStationE10 = detailsList.get( position ).getE_10();
                    String currentLat = stationsList.get(position).getLat();
                    String currentLng = stationsList.get(position).getLng();
                    String currentState = stationsList.get( position ).getIsOpen(  );



                    Intent stationsDetailsIntent = new Intent( getApplicationContext(), Details_station.class );
                    stationsDetailsIntent.putExtra( "stationName", currentStationName );
                    stationsDetailsIntent.putExtra( "place", currentStationPlace );
                    stationsDetailsIntent.putExtra( "street", currentStationStreet );
                    stationsDetailsIntent.putExtra( "stationDiesel", currentStationDiesel );
                    stationsDetailsIntent.putExtra( "stationE5", currentStationE5 );
                    stationsDetailsIntent.putExtra( "stationE10", currentStationE10 );
                    stationsDetailsIntent.putExtra( "lat",currentLat );
                    stationsDetailsIntent.putExtra( "lng",currentLng );
                    stationsDetailsIntent.putExtra( "isOpen",currentState );
                    startActivity( stationsDetailsIntent );
                }
            } );

            listView1.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    String currentStationName = stationsList.get( position ).getBrand();
                    String currentStationStreet = stationsList.get( position ).getStreet();
                    String currentStationPlace = stationsList.get( position ).getPlace();
                    String currentStationDiesel = detailsList.get( position ).getDiesel();
                    String currentStationE5 = detailsList.get( position ).getE_5();
                    String currentStationE10 = detailsList.get( position ).getE_10();
                    String currentLat = stationsList.get(position).getLat();
                    String currentLng = stationsList.get(position).getLng();
                    String currentState = stationsList.get( position ).getIsOpen(  );


                    Intent stationsDetailsIntent = new Intent( getApplicationContext(), Details_station.class );
                    stationsDetailsIntent.putExtra( "stationName", currentStationName );
                    stationsDetailsIntent.putExtra( "place", currentStationPlace );
                    stationsDetailsIntent.putExtra( "street", currentStationStreet );
                    stationsDetailsIntent.putExtra( "stationDiesel", currentStationDiesel );
                    stationsDetailsIntent.putExtra( "stationE5", currentStationE5 );
                    stationsDetailsIntent.putExtra( "stationE10", currentStationE10 );
                    stationsDetailsIntent.putExtra( "lat",currentLat );
                    stationsDetailsIntent.putExtra( "lng",currentLng );
                    stationsDetailsIntent.putExtra( "isOpen",currentState );
                    startActivity( stationsDetailsIntent );
                }

            } );

            listView2.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    String currentStationName = stationsList.get( position ).getBrand();
                    String currentStationStreet = stationsList.get( position ).getStreet();
                    String currentStationPlace = stationsList.get( position ).getPlace();
                    String currentStationDiesel = detailsList.get( position ).getDiesel();
                    String currentStationE5 = detailsList.get( position ).getE_5();
                    String currentStationE10 = detailsList.get( position ).getE_10();
                    String currentLat = stationsList.get(position).getLat();
                    String currentLng = stationsList.get(position).getLng();
                    String currentState = stationsList.get( position ).getIsOpen(  );



                    Intent stationsDetailsIntent = new Intent( getApplicationContext(), Details_station.class );
                    stationsDetailsIntent.putExtra( "stationName", currentStationName );
                    stationsDetailsIntent.putExtra( "place", currentStationPlace );
                    stationsDetailsIntent.putExtra( "street", currentStationStreet );
                    stationsDetailsIntent.putExtra( "stationDiesel", currentStationDiesel );
                    stationsDetailsIntent.putExtra( "stationE5", currentStationE5 );
                    stationsDetailsIntent.putExtra( "stationE10", currentStationE10 );
                    stationsDetailsIntent.putExtra( "lat",currentLat );
                    stationsDetailsIntent.putExtra( "lng",currentLng );
                    stationsDetailsIntent.putExtra( "isOpen",currentState );
                    startActivity( stationsDetailsIntent );
                }

            } );
        }
            @Override
            protected Void doInBackground (Void...voids){
                double latitude = mLoc.getLatitude();
                double longitude = mLoc.getLongitude();

                String lat = Double.toString( latitude );
                String lng = Double.toString( longitude );
                String BASE_URL = "https://creativecommons.tankerkoenig.de/json/list.php?";
                String LAT = "lat=49.633231&";
                String LONG = "lng=8.343572&";
                String RAD = "rad=3";
                String SORT = "&sort=dist&type=all&";

                String API_KEY = "apikey=735f247b-a452-e447-07b8-0fb2db574c9c";

                String URL = BASE_URL+LAT+LONG+RAD+SORT+API_KEY;
            try {
                URL url = new URL(URL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( inputStream ) );

                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    jsonData = jsonData + line;
                }
                JSONObject jsonObject = new JSONObject( (jsonData) );
                JSONArray stations = jsonObject.getJSONArray( "stations" );

                for ( i = 0; i < stations.length(); i++) {
                    JSONObject c = stations.getJSONObject( i );

                    Station st = new Station();
                    Station st1 = new Station();
                    Station st2 = new Station( );
                    Details dt = new Details();

                    st.setBrand( c.getString( "brand" ) );
                    st.setStreet( c.getString( "street" ) + " " + c.getString( "houseNumber" ) );
                    st.setPlace( c.getString( "postCode" ) + " " + c.getString( "place" ).toUpperCase());
                    st.setDist( c.getString( "dist" ) + " Km entfernt" );
                    st.setdPreis(c.getString( "diesel")+"\nEUR/l");
                    st.setLat( c.getString( "lat" ));
                    st.setLng( c.getString( "lng" ));
                    st.setIsOpen( c.getString( "isOpen" ));

                    st1.setBrand( c.getString( "brand" ) );
                    st1.setStreet( c.getString( "street" ) + " " + c.getString( "houseNumber" ) );
                    st1.setPlace( c.getString( "postCode" ) + " " + c.getString( "place" ).toUpperCase());
                    st1.setDist( c.getString( "dist" ) + " Km entfernt" );
                    st1.setdPreis(c.getString( "e5")+"\nEUR/l");

                    st2.setBrand( c.getString( "brand" ) );
                    st2.setStreet( c.getString( "street" ) + " " + c.getString( "houseNumber" ) );
                    st2.setPlace( c.getString( "postCode" ) + " " + c.getString( "place" ).toUpperCase());
                    st2.setDist( c.getString( "dist" ) + " Km entfernt" );
                    st2.setdPreis(c.getString( "e10")+"\nEUR/l");

                    dt.setName( c.getString( "name" ));
                    dt.setDiesel( c.getString( "diesel" ));
                    dt.setE_5( c.getString( "e5" ));
                    dt.setE_10( c.getString( "e10" ));


                    stationsList.add( st );
                    stationsList1.add(st1);
                    stationsList2.add(st2);

                    detailsList.add( dt );
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
            }

    }

}