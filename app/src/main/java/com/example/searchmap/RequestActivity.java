package com.example.searchmap;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class RequestActivity extends AppCompatActivity implements ListViewClickInterface{
    ArrayList<String> inner ;
    ArrayList<String> outer = new ArrayList<>();
    Geocoder geocoder;
    ArrayList<String> routeKeys = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_request);
        ListView listView = findViewById(R.id.layout1);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Routes");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public synchronized void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.d("key1", String.valueOf(dataSnapshot));
                    inner = new ArrayList<>();
                    String upload = dataSnapshot.getKey();
                    routeKeys.add(upload);
                    Log.d("upload", upload);
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        RootLoc rootLoc = dataSnapshot1.getValue(RootLoc.class);
                        Double latitude = rootLoc.getLatitude();
                        Double longitude = rootLoc.getLongitude();
                        geocoder = new Geocoder(RequestActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("Locationss", addresses.get(0).getLocality());
                        inner.add(addresses.get(0).getLocality());
                        Log.d("inner", inner.toString());
                    }
                    String s=inner.get(0)+"-"+inner.get(inner.size()-1);
                    outer.add(s);
                    Log.d("outer", outer.toString());
//
                }
                RequestAdapter requestAdapter = new RequestAdapter(RequestActivity.this,R.layout.root_list,outer,routeKeys,RequestActivity.this);
                listView.setAdapter(requestAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    @Override
    public void onItemClick(int position,String key) {
        Toast.makeText(this,key,Toast.LENGTH_LONG).show();
        Intent reqIntent = new Intent(getApplicationContext(), DropDownListActivity.class);
        reqIntent.putExtra("key",key);
        startActivity(reqIntent);
    }
}