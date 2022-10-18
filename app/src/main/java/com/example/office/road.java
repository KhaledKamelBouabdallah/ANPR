package com.example.office;

import static android.Manifest.permission.CAMERA;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class road extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE =1;
    Button btnDatePicker, btnTimePicker, detect;
    EditText txtDate, txtTime, Plat_number;
    private Bitmap imageBitmap;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Switch witch;

    private ImageView road;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road);

        Intent intent = getIntent();
        setTitle("Road");

        road = findViewById(R.id.road);
        road.setImageResource(R.drawable.roadsplit);


        Plat_number=(EditText)findViewById(R.id.Plat_number);
        detect=(Button)findViewById(R.id.detect);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);



        witch=(Switch)findViewById(R.id.switch1);



        txtTime.setHint("Select the end");
        txtDate.setHint("Select the beginning");
        Plat_number.setHint("Your plat number");

        btnDatePicker.setOnClickListener(this::onClick);
        txtDate.setOnClickListener(this::onClick);
        txtTime.setOnClickListener(this::onClick);

        if(!witch.isChecked()) txtTime.setEnabled(false);
        witch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!witch.isChecked()) txtTime.setEnabled(false);
                else txtTime.setEnabled(true);
            }
        });

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()){

                    System.out.println("heeeeeeeeeeeeeeeeeeeeeeeerrrrrrrrrrrrrreeeeee");
                    captureImage();

                }else {
                    requestPermission();
                }
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),payment.class);
                startActivity(intent);
            }
        });


    }


    public void onClick(View v) {

        if (v == btnDatePicker || v== txtDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            Calendar today = Calendar.getInstance();
            long now = today.getTimeInMillis();
            datePickerDialog.getDatePicker().setMinDate(now);
            datePickerDialog.show();
        }
        if (v == txtTime) {
            System.out.println(txtDate.getText().toString());
            if (txtDate.getText().toString().equals("")) {

                Toast.makeText(this, "Please set the beginning date first...", Toast.LENGTH_SHORT).show();
            } else {
                // Get Current Time
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtTime.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);

                String string_date = txtDate.getText().toString();

                SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date d = f.parse(string_date);
                    long milliseconds = d.getTime();
                    datePickerDialog.getDatePicker().setMinDate(milliseconds);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                datePickerDialog.show();
            }
        }



    }
    private boolean checkPermissions(){
        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(){
        int PERMISSION_CODE = 200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMISSION_CODE);
    }
    private void captureImage(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0){
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraPermission){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                captureImage();
            }else {
                Toast.makeText(this, "Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            //detect.setVisibility(View.VISIBLE);

            InputImage image = InputImage.fromBitmap(imageBitmap,0);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text text) {
                    StringBuilder result = new StringBuilder();
                    for (Text.TextBlock block: text.getTextBlocks()){
                        String blockText = block.getText();
                        Point[] blockCornerPoint = block.getCornerPoints();
                        Rect blockFrame = block.getBoundingBox();
                        for (Text.Line line : block.getLines()){
                            String lineText = line.getText();
                            Point[] lineCornerPoint = line.getCornerPoints();
                            Rect lineRect = line.getBoundingBox();
                            for (Text.Element element: line.getElements()){
                                String elementText = element.getText();
                                result.append(elementText);
                            }
                            Plat_number.setText(blockText);




                            // initializing our object
                            // class variable.

                          /*  AlertDialog.Builder matCheck = new AlertDialog.Builder(road.this);

                            //the space to type mat
                            EditText retreivedMatricule = new EditText(road.this);

                            //the dialog
                            retreivedMatricule.setHint("plat number");
                            retreivedMatricule.setText(blockText);
                            //the text to display
                            matCheck.setMessage("Verify your plat number");
                            //a dialog got two buttons:
                            // the positive button
                           /* matCheck.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //send to database
                                    // below line is used to get the
                                    // instance of our FIrebase database.


                                    //firebaseDatabase = FirebaseDatabase.getInstance();

                                    // below line is used to get reference for our database.


                                    String blockText = retreivedMatricule.getText().toString();

                                    // below line is for checking weather the
                                    // edittext fields are empty or not.
                                    if (TextUtils.isEmpty(blockText)) {

                                        // if the text fields are empty
                                        // then show the below message.
                                        Toast.makeText(road.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // else call the method to add
                                        // data to our database.
                                        /*addDatatoFirebase(blockText);
                                        Toast.makeText(city.this, "Please hold, door will open in seconds.", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(city.this, "Please hold, door will open in seconds.", Toast.LENGTH_SHORT).show();*/
                                   // }

                               // }
                            //});


                            // the negative button
                           /* matCheck.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            matCheck.setView(retreivedMatricule);
                            AlertDialog matDialog = matCheck.create();
                            matDialog.show();
                            break;*/
                        }
                        break;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(road.this, "Fail to detect text from image"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


}