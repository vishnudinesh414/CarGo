package com.example.searchmap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> mMarkerPoints;
    private LatLng mOrigin,mDestination,latLng,latLng2;
    SearchView source,destination,waypointSearch1,waypointSearch2,waypointSearch3;
    private Polyline mPolyline;
    Button createRide;


    HashMap<String,LatLng> hashMap = new HashMap<>();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
   DatabaseReference reference = database.getReference().child("Routes");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        source = findViewById(R.id.idSearchView1);
        destination = findViewById(R.id.idSearchView2);
        waypointSearch1=findViewById(R.id.idSearchView3);
        waypointSearch2=findViewById(R.id.idSearchView4);
        waypointSearch3=findViewById(R.id.idSearchView5);
        createRide=findViewById(R.id.button);
        mMarkerPoints = new ArrayList<>();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        source.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = source.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    hashMap.put("1",latLng);
                    mMarkerPoints.add(latLng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    source.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        destination.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = destination.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latLng2 = new LatLng(address.getLatitude(), address.getLongitude());
                    mMarkerPoints.add(latLng2);
                    mMap.addMarker(new MarkerOptions().position(latLng2).title(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 10));
                    drawRoute(mMarkerPoints);
                    destination.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




        waypointSearch1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = waypointSearch1.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMarkerPoints.add(latLng);
                    hashMap.put("2",latLng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));
                    drawRoute(mMarkerPoints);
                    waypointSearch1.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        waypointSearch2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = waypointSearch2.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMarkerPoints.add(latLng);
                    hashMap.put("3",latLng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));
                    drawRoute(mMarkerPoints);
                    waypointSearch2.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        waypointSearch3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = waypointSearch3.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMarkerPoints.add(latLng);
                    hashMap.put("4",latLng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));
                    drawRoute(mMarkerPoints);

                    if(mMarkerPoints.size()==5) {
                        waypointSearch3.setVisibility(View.GONE);
                        hashMap.put("5", latLng2);
                        reference.push().setValue(hashMap);
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        mapFragment.getMapAsync(this);

    }

    private void drawRoute(ArrayList<LatLng> mMarkerPoints) {
        if (mMarkerPoints.size() >= 2) {
            mOrigin = mMarkerPoints.get(0);
            mDestination = mMarkerPoints.get(1);
            DownloadTask downloadTask = new DownloadTask();
            String url = getDirectionsUrl(mOrigin, mDestination);
            downloadTask.execute(url);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        createRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMarkerPoints.size()==5) {

                    Intent inten = new Intent(getApplicationContext(), RideCreated.class);
                    startActivity(inten);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter all the waypoints",Toast.LENGTH_SHORT);
                }



                   // float[] results = new float[10];
                   // Location.distanceBetween(mOrigin.latitude,mOrigin.longitude,mDestination.latitude,mDestination.longitude,results);
                   // textView.setText("distance="+String.format("%.2f", results[0] / 1000) + "km");



                    // Start downloading json data from Google Directions API



                    //drawRoute();

            }
        });


    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Key
        String key = "key=" + getString(R.string.google_maps_key);

        //Waypoints
        String waypoints = "";
        for (int i = 2; i < mMarkerPoints.size(); i++) {
            LatLng point = (LatLng) mMarkerPoints.get(i);
            if (i == 2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }


        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;


            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
                    //if(j==0){
                        //distance=(String)point.get("distance");
                        //continue;
                    //}else if (j==1){
                        //duration=(String)point.get("duration");
                        //continue;
                   // }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                //textView.setText("Distance:"+distance + ", Duration:"+duration);
                mPolyline = mMap.addPolyline(lineOptions);
               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }


    }

//    private void datePicker(){
//
//        // Get Current Date
//        final Calendar c = Calendar.getInstance();
//
//
//        mYear = c.get(Calendar.YEAR);
//        mMonth = c.get(Calendar.MONTH);
//        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
//        mDay = c.get(Calendar.DAY_OF_MONTH);

//        if(mMonth==0){
//            month="Jan";
//        }
//        else if(mMonth==1){
//            month="Feb";
//        }
//        else if(mMonth==2){
//            month="Mar";
//        }
//        else if(mMonth==3){
//            month="Apr";
//        }
//        else if(mMonth==4){
//            month="May";
//        }
//        else if(mMonth==5){
//            month="Jun";
//        }
//        else if(mMonth==6){
//            month="Jul";
//        }
//        else if(mMonth==7){
//            month="Aug";
//        }
//        else if(mMonth==8){
//            month="Sep";
//        }
//        else if(mMonth==9){
//            month="Oct";
//        }
//        else if(mMonth==10){
//            month="Nov";
//        }
//        else if(mMonth==11){
//            month="Dec";
//        }

//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                        date_time = mDay + " " + mMonth;
//                        //*************Call Time Picker Here ********************
//                        timePicker();
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//    }
//
//    private void timePicker(){
//        // Get Current Time
//        final Calendar c = Calendar.getInstance();
//        mHour = c.get(Calendar.HOUR_OF_DAY);
//        mMinute = c.get(Calendar.MINUTE);
//
//        // Launch Time Picker Dialog
//        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
//                new TimePickerDialog.OnTimeSetListener() {
//
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                        mHour = hourOfDay;
//                        mMinute = minute;
//                        String timeSet = "";
//                        if (mHour > 12) {
//                            mHour -= 12;
//                            timeSet = "PM";
//                        } else if (mHour == 0) {
//                            mHour += 12;
//                            timeSet = "AM";
//                        } else if (mHour == 12){
//                            timeSet = "PM";
//                        }else{
//                            timeSet = "AM";
//                        }
//                        String min = "";
//                        if (mMinute < 10)
//                            min = "0" + mMinute ;
//                        else
//                            min = String.valueOf(mMinute);
//
//                        dateTime.setText(date_time+", "+mHour + ":" + mMinute+" "+timeSet);
//                    }
//                }, mHour, mMinute, false);
//        timePickerDialog.show();
//    }show

}