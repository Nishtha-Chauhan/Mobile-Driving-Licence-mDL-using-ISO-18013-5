package com.example.mdlholderapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mdlholderapplication.ble.BLE_functions;
import com.example.mdlholderapplication.ble.GeneralResultCallback;

import java.util.Random;
import java.util.UUID;

public class QRCode extends AppCompatActivity implements GeneralResultCallback {
    private static final int REQUEST_ENABLE_BT = 2;
    TextView tv,tv1,mEditText;
    Button b1,b2,generate_QRCode;
    ImageView qrCode;
    public int x=0;
    private BluetoothAdapter bluetoothAdapter;
    protected final static UUID uuid = UUID.fromString("31d65623-f644-11e9-aa28-37a0e6ae6869");
    protected final static UUID UUID_ENROLL_READ_CHAR = UUID.fromString("31D65624-F644-11E9-AA28-37A0E6AD0001");
    public static final String DATA = "0123456789ABCDEF";
    public static Random RANDOM = new Random();
    CountDownTimer timer;
    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_gen);

        getSupportActionBar().setElevation(0);

       /* ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33daff")));
*/
        tv = findViewById(R.id.textView1);
        tv1=findViewById(R.id.textView2);
        //priv = findViewById(R.id.star);

        qrCode = findViewById(R.id.qrcode);
        generate_QRCode = findViewById(R.id.generator);
        mEditText = findViewById(R.id.show_text);
        UUID[] uuids = {uuid};

        b1 = findViewById(R.id.sd);

       // b2 = findViewById(R.id.std);

        BLE_functions.getInstance().initialize(QRCode.this, QRCode.this);

        // Initializes Bluetooth adapter.
        @SuppressLint({"NewApi", "LocalSuppress"}) final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();


        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        //qrcode generator
        //random number generator for qrcode
        int l=4;
        /* mEditText.setText(randomString(l));*/
        String text=randomString(l);

        //Shared Preference
        SharedPreferences sharedPref = getSharedPreferences("DL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("random",text);
        boolean editorResult = editor.commit();

        Bitmap myBitmap = net.glxn.qrgen.android.QRCode.from(text).bitmap();
        qrCode.setImageBitmap(myBitmap);

        //timer
         timer= new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv1.setText("Seconds Remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                tv1.setText("Share again");
            }

        };

        b1.setText("Start Sharing");

        b1.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v)
            {
                if(x==1)
                {
                    BLE_functions.getInstance().stopEnroll();
                    b1.setText("Start Sharing");
                    timer.cancel();
                    timer.onFinish();
                    x=0;
                }
                else {
                    BLE_functions.getInstance().startEnroll(QRCode.this, "hi", "abc");
                    timer.cancel();
                    timer.start();
                    b1.setText("Stop Sharing");
                    x = 1;
                }
            }
        });


  /*      b2.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                BLE_functions.getInstance().stopEnroll();


            }
        });*/

    }

    public static String randomString(int len){
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BLE_functions.getInstance().stopEnroll();

    }


    /*private void onBtnClick() {

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                priv.setVisibility((priv.getVisibility() == View.VISIBLE)
                        ? View.GONE : View.VISIBLE);

            }
        });

    }*/
    //private long mLastClickTime = 0;



    @Override
    public boolean onGenericResultCallback(byte[] result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(QRCode.this, "callback received in Driving license activity", Toast.LENGTH_LONG).show();
            }
        });

        return false;
    }

}
