package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ViewDoc extends AppCompatActivity {
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12,tv13,tv14,tv15,tv16,tv17,tv18,tv19,tv20,s;
    public static byte[] iv = new SecureRandom().generateSeed(16);


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "WrongViewCast", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdoc);

        getSupportActionBar().setElevation(0);

       /* ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ec7063")));*/

        tv1=findViewById(R.id.textView1);
        tv2=findViewById(R.id.textView2);
        tv3=findViewById(R.id.textView3);
        tv4=findViewById(R.id.textView4);
        tv5=findViewById(R.id.textView5);
        tv6=findViewById(R.id.textView6);
        tv7=findViewById(R.id.textView7);
        tv8=findViewById(R.id.textView8);
        tv15=findViewById(R.id.textView15);
        tv16=findViewById(R.id.textView16);
        tv19=findViewById(R.id.textView19);

        tv9=findViewById(R.id.textView9);
        tv10=findViewById(R.id.textView10);
        tv11=findViewById(R.id.textView11);
        tv12=findViewById(R.id.textView12);
        tv13=findViewById(R.id.textView13);
        tv14=findViewById(R.id.textView14);
        tv17=findViewById(R.id.textView17);
        tv18=findViewById(R.id.textView18);
        tv20=findViewById(R.id.textView20);


        s=findViewById(R.id.status);

        SharedPreferences sharedPref = getSharedPreferences("DLView", Context.MODE_PRIVATE);
        String value1 = sharedPref.getString("json","abcde");


        try {
            JSONObject obj = new JSONObject(value1);
            JSONObject obj1 = new JSONObject(obj.getString("mDL"));


            //ECDH Algorithm implementation

            // Initialize two key pairs
            KeyPair keyPairA=null;
            KeyPair keyPairB = null;
            try {
                keyPairA  = generateECKeys();
                keyPairB = generateECKeys();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Create two AES secret keys to encrypt/decrypt the message
            SecretKey secretKeyA = generateSharedSecret(keyPairA.getPrivate(),
                    keyPairB.getPublic());
            SecretKey secretKeyB = generateSharedSecret(keyPairB.getPrivate(),
                    keyPairA.getPublic());

            // Decrypt the message using 'secretKeyB'
            String decryptedPlainText = decryptString(secretKeyB, obj1.toString());
            System.out.println("Decrypted cipher text: " + decryptedPlainText);





            tv9.setText(obj1.getString("validity"));
            tv10.setText(obj1.getString("name"));
            tv11.setText(obj1.getString("parent"));
            tv12.setText(obj1.getString("address"));
            tv13.setText(obj1.getString("pin"));
            tv14.setText(obj1.getString("phno"));
            tv17.setText(obj1.getString("dlno"));
            tv18.setText(obj1.getString("issue"));
            tv20.setText(obj1.getString("dob"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        SharedPreferences sharedPref1 = getSharedPreferences("SignatureStat", Context.MODE_PRIVATE);
        String value2 = sharedPref1.getString("sig_stat","");
        boolean b = Boolean.parseBoolean(value2);
        if(b==true)
            s.setText("Protected Data");
        else if(b==false)
            s.setText("Not Protected Data");
        else
            s.setText("Default Value");
    }







    public KeyPair generateECKeys() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(256);
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }

    public SecretKey generateSharedSecret(PrivateKey privateKey,
                                          PublicKey publicKey) {
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(publicKey, true);

            SecretKey key = keyAgreement.generateSecret("AES");
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptString(SecretKey key, String cipherText) {
        try {
            Key decryptionKey = new SecretKeySpec(key.getEncoded(),
                    key.getAlgorithm());
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] cipherTextBytes = hexToBytes(cipherText);
            byte[] plainText;

            cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec);
            plainText = new byte[cipher.getOutputSize(cipherTextBytes.length)];
            int decryptLength = cipher.update(cipherTextBytes, 0,
                    cipherTextBytes.length, plainText, 0);
            decryptLength += cipher.doFinal(plainText, decryptLength);

            return new String(plainText, "UTF-8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException
                | ShortBufferException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }



    public String bytesToHex(byte[] data, int length) {
        String digits = "0123456789ABCDEF";
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i != length; i++) {
            int v = data[i] & 0xff;

            buffer.append(digits.charAt(v >> 4));
            buffer.append(digits.charAt(v & 0xf));
        }

        return buffer.toString();
    }

    public String bytesToHex(byte[] data) {
        return bytesToHex(data, data.length);
    }

    public static byte[] hexToBytes(String string) {
        int length = string.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(string.charAt(i), 16) << 4) + Character
                    .digit(string.charAt(i + 1), 16));
        }
        return data;
    }









}
