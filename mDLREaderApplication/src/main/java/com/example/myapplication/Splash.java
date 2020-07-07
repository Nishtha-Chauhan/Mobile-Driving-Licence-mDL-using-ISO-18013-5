package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    TextView t1;
    //ImageView splash;
    AnimationDrawable rocketAnimation;

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        //t1=findViewById(R.id.text);
        setContentView(R.layout.splashact);

        // Now get a handle to any View contained
        // within the main layout you are using
        View someView = findViewById(R.id.randomViewInMainLayout);

        // Find the root view
        View root = someView.getRootView();

        // Set the color
        root.setBackgroundColor(getResources().getColor(android.R.color.white));

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

  /*      Test test=new Test();
        Test.main(new String[]{});*/

  new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this,DataReq.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
