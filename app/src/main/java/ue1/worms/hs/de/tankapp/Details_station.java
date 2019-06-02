package ue1.worms.hs.de.tankapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.List;

public class Details_station extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context mContext;
    public TextView name;
    public TextView dieselPreis;
    public TextView e5Preis;
    public TextView e10Preis;
    public TextView place;
    public TextView street;
    public ImageView state;

    private String currentStationName;
    private String currentStationDiesel;
    private String currentStationE5;
    private String currentStationE10;
    private String currentStationPlace;
    private String currentStationStreet;
    private String currentLat;
    private String currentLng;
    private String currentState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_station);
        setTitle( "Tankstellendetails" );

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );


        name = findViewById(R.id.name);
        dieselPreis = findViewById( R.id.dieselPreis );
        e5Preis = findViewById( R.id.e5Preis );
        e10Preis = findViewById( R.id.e10Preis );
        street = findViewById( R.id.street );
        place = findViewById( R.id.place );
        state = findViewById( R.id.imageView2 );

        currentStationName = getIntent().getExtras().get("stationName").toString();
        currentStationDiesel = getIntent().getExtras().get("stationDiesel").toString();
        currentStationE5 = getIntent().getExtras().get("stationE5").toString();
        currentStationE10 = getIntent().getExtras().get("stationE10").toString();
        currentStationStreet = getIntent().getExtras().get("street").toString();
        currentStationPlace = getIntent().getExtras().get("place").toString();
        currentLat = getIntent().getExtras().get("lat").toString();
        currentLng = getIntent().getExtras().get("lng").toString();
        currentState = getIntent().getExtras().get("isOpen").toString();
        System.out.println( currentLat );
        System.out.println( currentLng );



        name.setText(currentStationName);
        dieselPreis.setText("Diesel"+"\n\n"+currentStationDiesel+" €");
        e5Preis.setText("Super E5"+"\n\n"+currentStationE5+ " €");
        e10Preis.setText("Super E10"+"\n\n"+currentStationE10+" €");
        street.setText( currentStationStreet);
        place.setText( currentStationPlace);

        setFontTextView(getApplicationContext(),e5Preis);
        setFontTextView(getApplicationContext(),e10Preis);
        setFontTextView(getApplicationContext(),dieselPreis);

    }
    public void setFontTextView(Context c, TextView name) {
        Typeface font = Typeface.createFromAsset(c.getAssets(),"fonts/digit.ttf");
        name.setTypeface(font);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        double lat = Double.parseDouble( currentLat );
        double lng = Double.parseDouble( currentLng );

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng( lat,lng );
        mMap.addMarker( new MarkerOptions().position( sydney ).title( "Marker in Sydney" ) );
        mMap.moveCamera( CameraUpdateFactory.newLatLng( sydney ) );
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(18.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);

        LatLng myPosition= new LatLng( 49.633231,8.343572);
       mMap.addMarker(new MarkerOptions().position(myPosition)
                .icon( BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera( CameraUpdateFactory.newLatLng( myPosition ) );
        CameraPosition cameraPosition1 = new CameraPosition.Builder().target(myPosition).zoom(17.0f).build();
        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newCameraPosition(cameraPosition1);
        mMap.animateCamera(cameraUpdate1);

    }

}
