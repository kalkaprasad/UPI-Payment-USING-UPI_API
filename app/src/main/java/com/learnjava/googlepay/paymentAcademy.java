package com.learnjava.googlepay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class paymentAcademy extends AppCompatActivity {

    EditText name,email,phone,address;
    Button pay_botton;
    final int UPI_PAYMENT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_academy);

        name=findViewById(R.id.acd_name);
        email=findViewById(R.id.acd_email);
        phone=findViewById(R.id.acd_phone);
        address=findViewById(R.id.acd_address);
        pay_botton=findViewById(R.id.acd_pay);

        pay_botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting the values from the EditTexts
                if (TextUtils.isEmpty(name.getText().toString().trim())){
                    Toast.makeText(paymentAcademy.this," Name is Empty", Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(email.getText().toString().trim())){
                    Toast.makeText(paymentAcademy.this," Email ID is Empty", Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(phone.getText().toString().trim())){
                    Toast.makeText(paymentAcademy.this," Phone is Empty", Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(address.getText().toString().trim())){
                    Toast.makeText(paymentAcademy.this," Address is Empty", Toast.LENGTH_SHORT).show();
                }else {

                    payUsingUpi(name.getText().toString());
                }
            }
        });
    }
    void payUsingUpi(  String name) {
        Log.e("main ", "name "+name);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "anykor6-3@oksbi")  // write Your UPI ID where you want to get the Payment
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", "Academy College fee") // write your college name..
                .appendQueryParameter("am", "1")
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(paymentAcademy.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }


    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(paymentAcademy.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(paymentAcademy.this, "Transaction successful."+approvalRefNo, Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);

                // write your Api for send the data into the Mysql Server..


                Send_data_to_server(approvalRefNo);


            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(paymentAcademy.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                Toast.makeText(paymentAcademy.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);

            }
        } else {
            Log.e("UPI", "Internet issue: ");

            Toast.makeText(paymentAcademy.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    // this method are created for the send all payment details into the  Mysql Server..
    private void Send_data_to_server(String paymentrefno) {

        String payrefno=paymentrefno; // this is the payment refno.

        // name , email,phone,address,course name, payment refid, ammount,these field are  important to send into mysql database..


    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


}
