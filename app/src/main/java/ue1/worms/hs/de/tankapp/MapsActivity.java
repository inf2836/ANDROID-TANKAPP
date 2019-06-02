package ue1.worms.hs.de.tankapp;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_maps );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );
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

        LatLng current = new LatLng( 49.6322937, 8.341961 );
        LatLng current1 = new LatLng( 49.6319122, 8.347308 );
        LatLng current2 = new LatLng( 49.628697, 8.351773 );
        LatLng current3 = new LatLng( 49.62674, 8.360257 );
        LatLng current4 = new LatLng( 49.6461563, 8.349439 );
        LatLng current5 = new LatLng( 49.6435, 8.36274 );
        LatLng current6 = new LatLng( 49.642791, 8.363613 );
        LatLng current7 = new LatLng( 49.631354, 8.368857 );
        LatLng current8 = new LatLng( 49.61554, 8.33991 );
        LatLng current9 = new LatLng( 49.616992, 8.356652 );

        ArrayList<LatLng> latLng = new ArrayList<>();
        latLng.add( current );
        latLng.add( current1 );
        latLng.add( current2 );
        latLng.add( current3 );
        latLng.add( current4 );
        latLng.add( current5 );
        latLng.add( current6 );
        latLng.add( current7 );
        latLng.add( current8 );
        latLng.add( current9 );

        LatLng myPosition= new LatLng( 49.633231,8.343572);
        mMap.addMarker(new MarkerOptions().position(myPosition)
                .icon( BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera( CameraUpdateFactory.newLatLng( myPosition ) );
        CameraPosition cameraPosition1 = new CameraPosition.Builder().target(myPosition).zoom(17.0f).build();
        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newCameraPosition(cameraPosition1);
        mMap.animateCamera(cameraUpdate1);
        // Add a marker in Sydney and move the camera
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0 ; i < latLng.size();i++) {
            LatLng id = latLng.get( i );
            Marker marker = mMap.addMarker( new MarkerOptions().position( id).title( "Station" ) );
            builder.include(marker.getPosition());
            mMap.moveCamera( CameraUpdateFactory.newLatLng( id ) );
            CameraPosition cameraPosition = new CameraPosition.Builder().target( id ).zoom( 14.0f ).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition( cameraPosition );
            mMap.moveCamera( cameraUpdate );
        }
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }
}
