package com.example.mdlholderapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Launcher extends AppCompatActivity {
    @SuppressLint("NewApi")
    TextView tv1, tv2, tv3, tv4,tv5;
    Button b1,b2;
    ImageView iv1,iv2,iv3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        tv1 = findViewById(R.id.lname);
        tv2 = findViewById(R.id.newu);
        tv3 = findViewById(R.id.regu);
        tv4 = findViewById(R.id.viewm);
        /*tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Launcher.this, ViewDL.class);
                startActivity(myIntent);
            }
        });*/
        tv5 = findViewById(R.id.abt);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.idemia.com/mobile-drivers-license"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        iv1 = findViewById(R.id.imageView1);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);


        b1 = findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Launcher.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
        b2 = findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Launcher.this, ViewDL.class);
                startActivity(myIntent);
            }
        });


    }

}
