package com.example.office;

import static android.Manifest.permission.CAMERA;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class city extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE =1;
    ActivityResultLauncher<Intent> mGetContent;
    private ImageButton camera,imgFile;
    private TextView resultTV;
    private Bitmap imageBitmap;
    private ImageView background;


    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference reff;

    // creating a variable for
    // our object class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_city);
        setTitle("City checkin");

        Intent intent = getIntent();


        String marker_name = intent.getStringExtra("marker_name");
        TextView textView = findViewById(R.id.textView);
        textView.setText(marker_name);
        passCheck(marker_name);

        background = findViewById(R.id.background);
        background.setImageResource(R.drawable.city_bg);


        camera = findViewById(R.id.camera);
        resultTV = findViewById(R.id.result);
        imgFile = findViewById(R.id.camera2);


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissions()){

                    captureImage();
                }else {
                    requestPermission();
                }
            }
        });
       imgFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });


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

    private void passCheck(String marker_name){
        // creation of dialog
        // the beginning of the dialog
        MaterialAlertDialogBuilder city = new MaterialAlertDialogBuilder(this);
        //AlertDialog.Builder city = new AlertDialog.Builder(this);
        //the space to type password
        EditText password = new EditText(this);
        //code_pass changes depending on the marker, we test with it if the enterd pass is correct
        String code_pass;
        if (marker_name.equals("ingm"))
            code_pass = "0000";
        else
            code_pass = "1234";
        //the dialog
        password.setHint("password");
        password.setImeOptions(1);
        //the text to display
        city.setMessage("Enter the city key").setCancelable(false);
        //a dialog got two buttons:
        // the positive button
        city.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        // the negative button
        city.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        city.setView(password);
        AlertDialog dialog = city.create();
        dialog.show();
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        // to test if the password is correct or not
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                // the password is correct
                if (password.getText().toString().equals(code_pass)) {
                    Toast.makeText(getBaseContext(), "success", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                // the password is incorrect
                else {
                    password.setText("");
                    Toast.makeText(com.example.office.city.this, "Password wrong\n Please re-type the password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    int SELECT_PICTURE = 200;

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
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
                            //resultTV.setText(blockText);


                            // initializing our object
                            // class variable.

                            AlertDialog.Builder matCheck = new AlertDialog.Builder(city.this);

                            //the space to type mat
                            EditText retreivedMatricule = new EditText(city.this);

                            //the dialog
                            retreivedMatricule.setHint("plat number");
                            retreivedMatricule.setText(blockText);
                            //the text to display
                            matCheck.setMessage("Verify your plat number");
                            //a dialog got two buttons:
                            // the positive button
                            matCheck.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //send to database
                                     // below line is used to get the
                                    // instance of our FIrebase database.
                                    firebaseDatabase = FirebaseDatabase.getInstance();

                                    // below line is used to get reference for our database.


                                    String blockText = retreivedMatricule.getText().toString();

                                    // below line is for checking weather the
                                    // edittext fields are empty or not.
                                    if (TextUtils.isEmpty(blockText)) {

                                        // if the text fields are empty
                                        // then show the below message.
                                        Toast.makeText(city.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // else call the method to add
                                        // data to our database.
                                        addDatatoFirebase(blockText);
                                        Toast.makeText(city.this, "Please hold, door will open in seconds.", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(city.this, "Please hold, door will open in seconds.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                            // the negative button
                            matCheck.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            matCheck.setView(retreivedMatricule);
                            AlertDialog matDialog = matCheck.create();
                            matDialog.show();
                            break;
                        }
                        break;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(city.this, "Fail to detect text from image"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
    private void startcropAvtivity(){

    }
    private void addDatatoFirebase(String matricule) {


        // we are use add value event listener method
        // which is called with database reference.
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                String data = new String(matricule);
                reff.setValue(data);

                // after adding this data we are showing toast message.
                Toast.makeText(city.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(city.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}