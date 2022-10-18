package com.example.office;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Data {
    Data(){}

    public void SetData(){

        Map<String, Object> spot = new HashMap<>();
        spot.put("name", "reghaia");
        spot.put("localization", new LatLng(33,36));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("spots").document("reghaia").set(spot).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("success");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("failed");                    }
                });
    }

    public void WriteData(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

    }

    public void GetMarkers(){

    }
}
