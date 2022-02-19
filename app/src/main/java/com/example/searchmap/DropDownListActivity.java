package com.example.searchmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DropDownListActivity extends AppCompatActivity implements DropDownClickInterface {
    Geocoder geocoder;
    List<String> inner;
//    ArrayList<String> inner ;
    List<String> innerDummy;
    String tempRemovedItem ;
    String item1 ;
    String item2 ;
    Spinner spinner1 ;
    Spinner spinner2 ;
    DropDownListAdapter adapter1;
    DropDownListAdapter adapter2;
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_list);

        spinner1 =(Spinner) findViewById(R.id.coursesspinner);
        spinner2 =(Spinner) findViewById(R.id.coursesspinner2);
        inner = new ArrayList<String>();
        Button button = findViewById(R.id.button);
        Bundle keyBundle = getIntent().getExtras();


        String key = keyBundle.getString("key");
//        inner = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Routes").child(key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RootLoc rootLoc = dataSnapshot.getValue(RootLoc.class);
                    Double latitude = rootLoc.getLatitude();
                    Double longitude = rootLoc.getLongitude();
                    geocoder = new Geocoder(DropDownListActivity.this, Locale.getDefault());
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
                innerDummy = inner;
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(DropDownListActivity.this, R.layout.dropdown_list1, inner);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(DropDownListActivity.this, R.layout.dropdown_list1, innerDummy);

//                adapter1 = new DropDownListAdapter(DropDownListActivity.this,R.layout.dropdown_list1,inner,DropDownListActivity.this);
//                adapter2  = new DropDownListAdapter(DropDownListActivity.this,R.layout.dropdown_list1,innerDummy,DropDownListActivity.this);
                spinner1.setAdapter(adapter1);
                spinner2.setAdapter(adapter2);
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        item1 = adapterView.getSelectedItem().toString();
                        Toast.makeText(DropDownListActivity.this,item1,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        item2 = adapterView.getSelectedItem().toString();
                        Toast.makeText(DropDownListActivity.this,item2,Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), RequestedMapActivity.class);
                        intent.putExtra("From",item1);
                        intent.putExtra("Dest",item2);
                        startActivity(intent);
                        finish();

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();

    }


}