package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView tv,tv1,tv2,tv3,tv4,tv5,tv6;
    Button button;
    EditText ed,ed1,ed2,ed3,ed4,ed5;
    public final static String name ="abc";
    public final static String age ="20";
    public final static String address ="xyz";
    public final static String phno ="9818103010";
    public final static String password ="4444";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

/*        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ec7063")));*/

        tv=findViewById(R.id.textView);
        tv1=findViewById(R.id.textView1);
        tv2=findViewById(R.id.textView2);
        tv3=findViewById(R.id.textView3);
        tv4=findViewById(R.id.textView4);
        tv6=findViewById(R.id.textView6);


        ed=findViewById(R.id.editText1);
        ed1=findViewById(R.id.editText2);
        ed2=findViewById(R.id.editText3);
        ed3=findViewById(R.id.editText4);
        ed5=findViewById(R.id.editText6);

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed.getText().toString().equalsIgnoreCase("")) {
                    ed.setError("enter a valid name");//it gives user to info message //use any one //
                }
                else if (ed1.getText().toString().equalsIgnoreCase("")) {
                    ed1.setError("enter a valid age");
                }
                else if (ed2.getText().toString().equalsIgnoreCase("")) {
                    ed2.setError("enter a valid address");
                }
                else if (ed3.getText().toString().equalsIgnoreCase("")) {
                    ed3.setError("enter a valid phone no");
                }
                else if (ed5.getText().toString().equalsIgnoreCase("")) {
                    ed5.setError("enter a valid password");
                }
                else {
                    //sendDataToServer();
                    Intent intent = new Intent(MainActivity.this, Transfer.class);

                    intent.putExtra("name", ed.getText().toString());
                    intent.putExtra("age", ed1.getText().toString());
                    intent.putExtra("address", ed2.getText().toString());
                    intent.putExtra("phno", ed3.getText().toString());
                    intent.putExtra("password", ed5.getText().toString());
                    startActivity(intent);
                }
            }
                                  }

        );
    }
}
