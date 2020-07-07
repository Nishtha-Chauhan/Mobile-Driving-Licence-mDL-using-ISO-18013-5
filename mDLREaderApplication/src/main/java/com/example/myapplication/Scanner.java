package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.encoder.QRCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.UUID;

import static android.util.Base64.NO_WRAP;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Scanner extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 2;
    TextView t1, t2, t3, t4, t5, t6, textViewName, disable;
    Button b1, b2, b3, buttonScan, verif;
    private IntentIntegrator qrScan;
    private static final long SCAN_PERIOD = 120000;
    private boolean mScanning;
    private Handler handler;
    protected final static UUID uuid = UUID.fromString("31d65623-f644-11e9-aa28-37a0e6ae");
    protected final static UUID UUID_ENROLL_READ_CHAR = UUID.fromString("31D65624-F644-11E9-AA28-37A0E6AD0001");
    private BluetoothManager bluetoothManager;
    private String bluetoothDeviceAddress;
    private BluetoothGatt bluetoothGatt;
    private int connectionState = STATE_DISCONNECTED;
    private String signature = null;
    ImageView qrCode;
    public int x = 0;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    private final int REQUEST_LOCATION_PERMISSION = 1;
    CountDownTimer timer;

    private String plainText;

    UUID[] uuids = {uuid};
    //UUID[] uuids;

    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);

        getSupportActionBar().setElevation(0);

        /*ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ec7063")));
*/
        t1 = findViewById(R.id.textView1);
        t2 = findViewById(R.id.textView2);
        t3 = findViewById(R.id.textView3);

        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);

        //qrCode=findViewById(R.id.qrcode_scan);

        //new UUID
        SharedPreferences sharedPref1 = getSharedPreferences("UUID", Context.MODE_PRIVATE);
        String value2 = sharedPref1.getString("uuid", "");
        UUID ui = UUID.fromString(value2);
        uuids = new UUID[]{ui};


        // Use this Test to determine whether BLE is supported on the device.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }


        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }

        //display a dialog requesting user permission to enable location
        if (ContextCompat.checkSelfPermission(Scanner.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Scanner.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(Scanner.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(Scanner.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

       /* //scanning the qrcode image
        qrScan = new IntentIntegrator(Scanner.this);
        b1.setOnClickListener(Scanner.this);
*/
        //timer
        timer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                t3.setText("Seconds Remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                t3.setText(" Scan again");
            }

        };

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Scanner.this, QRCode.class);
                startActivity(i);
            }
        });


        // final LeDeviceListAdapter leDeviceListAdapter;
        b2.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                String buttonText = b2.getText().toString();
                if (buttonText.equals("Start Scaning Device")) {
                    onstart();
                } else {
                    onstop();
                }
            }
        });

        //Scanner.this.finish();

    }


    public void onstart()
    {
        scanLeDevice(true);
        timer.cancel();
        timer.start();
        b2.setText("Stop Scanning Device");
    }

    public void onstop()
    {
        scanLeDevice(false);
        timer.cancel();
        timer.onFinish();
        b2.setText("Start Scaning Device");
    }

    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean verify(String privateSignature, PublicKey publicKey, String plainText) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF_8));
        byte[] signatureBytes = Base64.decode(privateSignature, NO_WRAP);
        //byte[] signatureBytes = Base64.getDecoder().decode(privateSignature);
        return publicSignature.verify(signatureBytes);
    }

  /*  @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean verify(String signature, PublicKey publicKey,String plainText) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF_8));
        byte[] signatureBytes = java.util.Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }*/


    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                } catch (JSONException e) {
                    //code to concatenate uuid and random string
                    String suuid = uuid.toString();
                    String a = result.getContents();
                    String s3=suuid.concat(a);
                    UUID ui=UUID.fromString(s3);
                    uuids = new UUID[]{ui};
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    private PublicKey getPublicKey() throws IOException, CertificateException {
        //READING PUBLIC KEY FROM CERTIFICATE
        InputStream stream = getAssets().open("cert.pem");
        CertificateFactory of = CertificateFactory.getInstance("X.509");
        X509Certificate clientcert = (X509Certificate) of.generateCertificate(stream);
        return clientcert.getPublicKey();
    }



 /*   public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }*/

    private void scanLeDevice(final boolean enable) {


        if (enable) {
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
            // Stops scanning after a pre-defined scan period.
          /*  new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("Mdl_Reader", "inside scanLeDevice");
                    mScanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;*/

            bluetoothAdapter.startLeScan(uuids, leScanCallback);
        } else {
            mScanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
        }

    }

    private BluetoothAdapter.LeScanCallback leScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("device", device.getAddress());
                            bluetoothGatt = device.connectGatt(Scanner.this, false, gattCallback);
                        }
                    });
                }
            };

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            Log.i("Mdl_Reader", "inside onConnectionStateChange" + status);
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("Mdl_Reader", "inside onConnectionStateChange " + "STATE_CONNECTED");
                gatt.discoverServices();
                //gatt.requestMtu(50);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                //connectionState = STATE_DISCONNECTED;
                Log.i("disconnect", "Disconnected from GATT server.");
                //broadcastUpdate(intentAction);
            }
            //super.onConnectionStateChange(gatt,status,newState);
        }


        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d("Mdl_Reader", "service discovered");
            gatt.readCharacteristic(gatt.getService(uuids[0]).getCharacteristic(UUID_ENROLL_READ_CHAR));
        }

        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status ==BluetoothGatt.GATT_SUCCESS ) {
                Log.i("Mdl_Reader", "inside onCharacteristicReadsuccess" + status);
                final byte[] charValue = characteristic.getValue();
                if (charValue == null) {

                    Log.i("Mdl_Reader", "inside onCharacteristicReadsuccess" + "VALUE_IS_NULL");
                } else {
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            //  t6.setText(BLE_functions.getInstance().convertBytesToHex(charValue));
                            try {
                                String jsonString = new String(charValue);
                                Log.i("mdl_reader", jsonString);
                                JSONObject obj2 = new JSONObject(jsonString);
                                //setting values to textviews
                                JSONObject obj3 = new JSONObject(obj2.getString("mDL"));
                                //t2.setText(obj3.getString("pin"));
                              /*  t2.setVisibility((t2.getVisibility() == View.VISIBLE)
                                        ? View.GONE : View.VISIBLE);*/


                                //Shared Preference
                                SharedPreferences sharedPref1 = getSharedPreferences("DLView", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref1.edit();
                                editor.putString("json", jsonString);
                                boolean editorResult = editor.commit();



                                /*t2.setText("View Driving Licence");
                                //signature verification
                                t2.setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onClick(View v) {*/
                                boolean isCorrect = false;
                                try {
                                    isCorrect = verify(signature, getPublicKey(), plainText);

                                    SharedPreferences sharedPref2 = getSharedPreferences("SignatureStat", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = sharedPref2.edit();

                                    editor.putString("sig_stat", String.valueOf(isCorrect));
                                    boolean editorResults = editor1.commit();
                                    Intent myIntent = new Intent(Scanner.this, ViewDoc.class);
                                    startActivity(myIntent);
                                    bluetoothAdapter.stopLeScan(leScanCallback);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                /*    }
                                });*/
                                //plainText=obj1.getString("pin");
                                plainText = obj3.toString();
                                Log.i("plaintext", plainText);
                                signature = obj2.getString("sign");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            } else {
                Log.i("Mdl_Reader", "inside onCharacteristicReadfailed" + status);
            }
        }

    };

}
