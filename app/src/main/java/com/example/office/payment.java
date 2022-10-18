package com.example.office;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.office.databinding.ActivityPaymentBinding;

import java.util.Locale;

public class payment extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.walletnfcrel";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    String amount;
    String name = "Khaled Bouabdallah";
    String upiID= "khaledkamel";
    String transactionNote = "Pay";
    String status;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = binding.payment.getText().toString();
                if (!amount.isEmpty()){
                    uri = getUpiPayment(name, upiID, transactionNote, amount);
                    payWithGPay();
                }

            }
        });
    }

    private void payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        }else {
            Toast.makeText(this, "Please Install Gpay", Toast.LENGTH_SHORT).show();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            status = data.getStringExtra("Status").toLowerCase(Locale.ROOT);
        }
        if ((RESULT_OK == resultCode) && status.equals("success")){
            Toast.makeText(this, "Transaction successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        }
    }

    static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName,0);
            return true;
        }catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }

    protected static Uri getUpiPayment(String name, String upiId, String transaction, String amount){
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transaction)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("pa", "INR")
                .build();

    }
}