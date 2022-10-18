package com.example.office;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Markers {
    private ArrayList<MarkerOptions> cityMarker;
    private ArrayList<MarkerOptions> roadMarker;
    private ArrayList<MarkerOptions> parkingMarker;

    Markers(){
        cityMarker = new ArrayList<>();
        roadMarker = new ArrayList<>();
        parkingMarker = new ArrayList<>();

    }

    public void getMarkers(String name, GoogleMap map){
        if(name.equals("city")){
            //map.clear();
            cityMarker.add(new MarkerOptions().position(new LatLng(36.757645, 3.480383)).title("city 800"));
            cityMarker.add(new MarkerOptions().position(new LatLng(36.761194, 3.458354)).title("INGM"));
            cityMarker.add(new MarkerOptions().position(new LatLng(36.740336, 3.338516)).title("City 47 lodging"));

            addMarkers(cityMarker,map);
        }
        else if(name.equals("road")){

            addMarkers(roadMarker,map);
        }
        else if (name.equals("parking")){
            //map.clear();
            parkingMarker.add(new MarkerOptions().position(new LatLng(36.76019464220008, 3.0526901360473833)).title("Ali Mellah"));

            addMarkers(parkingMarker,map);
        }
    }

    private void addMarkers(ArrayList<MarkerOptions> marker, GoogleMap map){
        for(int i =0; i<marker.size();i++){
            map.addMarker(marker.get(i));
        }
    }


}