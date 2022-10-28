package com.example.office;

import static android.Manifest.permission.CAMERA;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class parking extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE =1;
    private int  currentId;
    private LinearLayout left, right;
    private ImageButton car;
    EditText number;
    Button detect, verify;
    private Bitmap imageBitmap;
    private int capacity = 15, //the max capacity of the parking lot
            freeSpace = 8;
    private boolean clicked= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        Intent intent = getIntent();
        String park_name = intent.getStringExtra("marker_name");
        setTitle(park_name);


        number=(EditText)findViewById(R.id.number);
        detect=(Button)findViewById(R.id.detectnumber);
        verify=(Button)findViewById(R.id.verify);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
//euiheuihuifheuifheuiz

        //connect to database and get the capacity of the parking + the number of available places
        int i, j = capacity - freeSpace;
        for(i = 0; i < capacity ; i++){ //create buttons according to park capacity
            car = new ImageButton(this);
            LinearLayout.LayoutParams layoutParams = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if(j!=0){
                car.setEnabled(false);
                car.setAlpha((float) 0.7);
                j--;
            }
            //int topMargin = i * 270;
           // layoutParams.setMargins(80, topMargin, 80, 0);

            car.setLayoutParams(layoutParams);
            car.setImageResource(R.drawable.ic_car_dark);

            currentId = i +1;
            car.setId(currentId);
            System.out.println("before on click"+currentId);
            if(i%2==0){
                right.addView(car);
            }
            else {
                left.addView(car);
            }
            car.setOnClickListener(this::onClick);
        }


        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clicked==false){
                    Toast.makeText(parking.this,"Select spot first", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (checkPermissions()){
                        captureImage();

                    }else {
                        requestPermission();
                    }
                }

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),payment.class);
                startActivity(intent);
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
                            number.setText(blockText);

                        }
                        break;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(parking.this, "Fail to detect text from image"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public void onClick(View view) {
        for(int i =0; i<capacity;i++){
            if(view.getId()==i+1){
                System.out.println("in on click"+currentId);
                view.setBackgroundColor(getResources().getColor(R.color.car));
                view.setClickable(false);
                clicked = true;
                for(int j =0; j<capacity;j++){
                    if(j!=i+1){
                        ImageButton button = findViewById(j+1);
                        button.setClickable(false);
                    }
                }
            }
        }

        Toast.makeText(parking.this, "Please enter/scan your plat number",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}