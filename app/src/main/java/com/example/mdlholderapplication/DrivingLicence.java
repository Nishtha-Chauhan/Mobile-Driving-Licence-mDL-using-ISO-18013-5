package com.example.mdlholderapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mdlholderapplication.ble.BLE_functions;
import com.example.mdlholderapplication.ble.GeneralResultCallback;

import net.glxn.qrgen.android.QRCode;

import java.util.Random;
import java.util.UUID;

public class DrivingLicence extends AppCompatActivity implements GeneralResultCallback {
    private static final int REQUEST_ENABLE_BT = 2;
    TextView tv, tv1, tv2, tv3, tv4, priv,mEditText;
    EditText ed, ed1, ed2, ed3;
    Button b,b1,b2,generate_QRCode;
    RadioButton rb1, rb2;
    ImageView iv1,qrCode;
    private BluetoothAdapter bluetoothAdapter;
    protected final static UUID uuid = UUID.fromString("31d65623-f644-11e9-aa28-37a0e6ae6869");
    protected final static UUID UUID_ENROLL_READ_CHAR = UUID.fromString("31D65624-F644-11E9-AA28-37A0E6AD0001");
    public static final String DATA = "0123456789ABCDEF";
    public static Random RANDOM = new Random();

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dl);
        tv = findViewById(R.id.textView1);
        tv1 = findViewById(R.id.textView2);
        tv2 = findViewById(R.id.textView3);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DrivingLicence.this, Pub_priv.class);
                startActivity(myIntent);
            }
        });
        tv3 = findViewById(R.id.textView4);
        tv4 = findViewById(R.id.textView5);

        ed = findViewById(R.id.editText1);
        ed1 = findViewById(R.id.editText2);
        ed2 = findViewById(R.id.editText3);
        ed3 = findViewById(R.id.editText4);
        priv = findViewById(R.id.star);

        iv1 = findViewById(R.id.imageView);

        rb1 = findViewById(R.id.radiobutton2);
        rb2 = findViewById(R.id.radiobutton3);

        qrCode = findViewById(R.id.qrcode);
        generate_QRCode = findViewById(R.id.generator);
        mEditText = findViewById(R.id.show_text);
        UUID[] uuids = {uuid};

        @SuppressLint("WrongConstant")
        SharedPreferences sh = getSharedPreferences("Main_Activity", Context.MODE_PRIVATE);
        String s1 = sh.getString("pin", null);
        //String passedArg = getIntent().getExtras().getString("pin");
        priv.setText(s1);

        b = findViewById(R.id.val);
        onBtnClick();

        b1 = findViewById(R.id.sd);

        b2 = findViewById(R.id.std);

        BLE_functions.getInstance().initialize(DrivingLicence.this, DrivingLicence.this);

        // Initializes Bluetooth adapter.
        @SuppressLint({"NewApi", "LocalSuppress"}) final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();


        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


        // final LeDeviceListAdapter leDeviceListAdapter;
        b1.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                BLE_functions.getInstance().startEnroll(DrivingLicence.this, "hi", "abc");

            }
        });


        b2.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                BLE_functions.getInstance().stopEnroll();


            }
        });

        iv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DrivingLicence.this, Settings.class);
                startActivity(intent);
            }
        });

        //qrcode generator
        //mEditText.setText(uuid);
        generate_QRCode.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               //random number generator for qrcode
               int l=4;
               mEditText.setText(randomString(l));
               String text=mEditText.getText().toString();

               //Shared Preference
               SharedPreferences sharedPref = getSharedPreferences("DL", Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPref.edit();

               editor.putString("random",text);
               boolean editorResult = editor.commit();


           /*    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
               try {
                   BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                   BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                   Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                   qrCode.setImageBitmap(bitmap);
               } catch (WriterException e) {
                   e.printStackTrace();
               }*/

               Bitmap myBitmap = QRCode.from(text).bitmap();
               qrCode.setImageBitmap(myBitmap);

           }
       });

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

/*
    protected void onStop()
    {
        super.onStop();
        BLE_functions.getInstance().stopEnroll();
    }
*/

    private void onBtnClick() {

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                priv.setVisibility((priv.getVisibility() == View.VISIBLE)
                        ? View.GONE : View.VISIBLE);

            }
        });

    }
    //private long mLastClickTime = 0;


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
       /* if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();*/

        switch (view.getId()) {
         /*   case R.id.radiobutton1:
                if (checked)
                    break;*/
            case R.id.radiobutton2:
                if (checked)
                    break;
            case R.id.radiobutton3:
                if (checked)
                    break;
            /*case R.id.radiobutton4:
                if (checked)
                    break;
            case R.id.radiobutton5:
                if (checked)
                    break;*/
        }
    }


    @Override
    public boolean onGenericResultCallback(byte[] result) {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(DrivingLicence.this, "callback received in Driving license activity", Toast.LENGTH_LONG).show();
           }
       });

        return false;
    }
}