package com.example.mdlholderapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashAct extends AppCompatActivity {

    TextView t1;
    //ImageView splash;
    AnimationDrawable rocketAnimation;

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        //animation
            /*ImageView image = (ImageView)findViewById(R.id.splashscreen);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myanimation);
            image.startAnimation(animation);
*/
    /*    final ImageView splash = findViewById(R.id.splashscreen);
        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);

// Start animating the image
        splash.startAnimation(anim);

// Later.. stop the animation
        splash.setAnimation(null);*/


        /*i1.setBackgroundResource(R.drawable.logo);
        rocketAnimation = (AnimationDrawable) i1.getBackground();

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rocketAnimation.start();
            }
        });*/

        //t1=findViewById(R.id.text);
        setContentView(R.layout.splash);

        // Now get a handle to any View contained
        // within the main layout you are using
        View someView = findViewById(R.id.randomViewInMainLayout);

        // Find the root view
        View root = someView.getRootView();

        // Set the color
        root.setBackgroundColor(getResources().getColor(android.R.color.white));

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                SharedPreferences sharedPref = getSharedPreferences("Main_Activity", Context.MODE_PRIVATE);
                String val = sharedPref.getString("obj", "*121#");
                Intent mainIntent;
                if(val=="*121#")
                    mainIntent = new Intent(SplashAct.this,MainActivity.class);
                else
                    mainIntent = new Intent(SplashAct.this,ViewDL.class);
                SplashAct.this.startActivity(mainIntent);
                SplashAct.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
