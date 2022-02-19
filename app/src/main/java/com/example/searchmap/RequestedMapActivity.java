package com.example.searchmap;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RequestedMapActivity extends AppCompatActivity implements  OnMapReadyCallback, TaskLoadedCallback{
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection,next;
    private Polyline currentPolyline;
   // LatLng can,tlsr,clt,otplm,pkd,ptnm,chngn,kotm,etmnr,ekm;
    LatLng src,dst;
   // Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_actitvity);
        Bundle bundle = getIntent().getExtras();
        String from = bundle.getString("From");
        String dest = bundle.getString("Dest");
        Log.d("src",dest);
       // tlsr = new LatLng(11.7491,75.4890);
       // clt = new LatLng(11.2588,75.7804);
       // otplm = new LatLng(10.7767,76.3759);
//        Log.d("src",dest);
        src = geoToLatLng(from);
        dst = geoToLatLng(dest);
//
//
//
//
//        Log.d("src",src.toString());
//        geocoder = new Geocoder(this, Locale.getDefault());
//        double latitude = can.latitude;
//        double longitude = can.longitude;
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = geocoder.getFromLocation(latitude,longitude, 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        String city = addresses.get(0).getLocality();
//        Log.d("city",city);

        getDirection = (Button) findViewById(R.id.getRoute);
        next = (Button) findViewById(R.id.next);
        next.setVisibility(View.GONE);


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);
        place1= new MarkerOptions().position(src).title("From");
        place2= new MarkerOptions().position(dst).title("Dest");

        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getUrl(place1.getPosition(), place2.getPosition(),"driving");
                Log.d("URL",url);
                new FetchURL(RequestedMapActivity.this).execute(url,"driving");
                getDirection.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RideRequested.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(@NonNull LatLng latLng) {
//                Intent intent = new Intent(getApplicationContext(),CreateRequestActivity.class);
//                startActivity(intent);
//            }
//        });
        mMap.addMarker(place1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.addMarker(place2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dst, 8));
//        currentPolyline = mMap.addPolyline(new PolylineOptions().add(can,pkd).width(5).color(Color.RED));
    }
    @NonNull
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }


    public LatLng geoToLatLng(String location){
        Geocoder geocoder = new Geocoder(RequestedMapActivity.this);
        List<Address> addressList = null;
        try{
            addressList = geocoder.getFromLocationName(location,5);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        return  latLng;
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyline!=null){
            currentPolyline.remove();
        }

        currentPolyline= mMap.addPolyline((PolylineOptions) values[0]);
    }
}