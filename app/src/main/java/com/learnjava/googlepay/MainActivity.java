package com.learnjava.googlepay;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void paymentEducationcol(View view) {

        Intent intent = new Intent(this,paymentEducationCol.class);
        startActivity(intent);
    }

    public void paymentdegreecollege(View view) {

        Intent intent = new Intent(this,paymentdegreecollege.class);
        startActivity(intent);
    }

    public void paymentacademy(View view) {

        Intent intent= new Intent(this,paymentAcademy.class);
        startActivity(intent);
    }

    public void contactus(View view) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","kalkaprasad59@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }



    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onBackPressed() {

        new AlertDialog.Builder(this)
//                .setView(R.layout.alerdesign)
                .setView(findViewById(R.layout.alerdesign))
               .
                .setPositiveButton("", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        finish();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
