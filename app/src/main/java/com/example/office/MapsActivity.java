package com.example.office;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import com.example.office.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    String map_type;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Intent intentP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get the extra sent from previous activity
        intentP = getIntent();
        map_type = intentP.getStringExtra("map_type");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        SearchView searchView;
        searchView = findViewById(R.id.idSearchView);
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("Search")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address = addressList.get(0);

                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // on below line we are adding marker to that position.
                    //mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
        //if (city == 1) {
        // Add a marker in ingm and move the camera
        LatLng ingm = new LatLng(36.761194, 3.458354);
        LatLng city800 = new LatLng(36.757645, 3.480383);
            /*mMap.addMarker(new MarkerOptions().position(ingm).title("ingm"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ingm));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ingm, 12));


            mMap.addMarker(new MarkerOptions().position(city800).title("city 800"));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ingm, 8));

        Markers markers = new Markers();
        markers.getMarkers(map_type, mMap);

        //to add click listner for ingm
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {


                Intent intent = new Intent();

                intent.putExtra("marker_name", marker.getTitle());
                if (map_type.equals("city")) {
                    intent.setClass(getApplicationContext(), city.class);
                    startActivity(intent);
                }
                else if (map_type.equals("parking")){
                    intent.setClass(getApplicationContext(), parking.class);
                    startActivity(intent);
                }
                else if (map_type.equals("road")){
                    intent.setClass(getApplicationContext(), road.class);
                    startActivity(intent);
                }

                startActivity(intent);


                    /*if (marker.getPosition().equals(city800)) {
                        Intent intent = new Intent();
                        String text = "City 800";
                        intent.putExtra("marker_name", text);
                        intent.setClass(getApplicationContext(), city.class);
                        startActivity(intent);
                    }*/

                return false;
            }
        });

    }
    //}
}