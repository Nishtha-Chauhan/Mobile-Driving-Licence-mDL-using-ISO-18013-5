package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class QRCodeReader extends AppCompatActivity {

    protected final static UUID uuid = UUID.fromString("31d65623-f644-11e9-aa28-37a0e6ae");
    UUID[] uuids = {uuid};
    private IntentIntegrator qrScan;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcodereader);

        getSupportActionBar().setElevation(0);
/*

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ec7063")));
*/

        //scanning the qrcode image
        //b1=findViewById(R.id.qr);
        qrScan = new IntentIntegrator(QRCodeReader.this);
        OnClickListener(QRCodeReader.this);
    }

    private void OnClickListener(QRCodeReader qrCodeReader) {
        qrScan.initiateScan();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
                this.finishAffinity();
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

                    SharedPreferences sharedPref1 = getSharedPreferences("UUID", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref1.edit();

                    editor.putString("uuid",s3);
                    boolean editorResult = editor.commit();

                    /*UUID ui=UUID.fromString(s3);
                    uuids = new UUID[]{ui};*/

                    Toast.makeText(this, "Scan Success", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(QRCodeReader.this, Scanner.class);
                    startActivity(myIntent);
                    QRCodeReader.this.finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

   /* @Override
    public void onClick(View v) {
            //initiating the qr code scan
            qrScan.initiateScan();
    }*/
}


