package com.example.mdlholderapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ViewDL extends AppCompatActivity {
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12,tv13,tv14,tv15,tv16,tv17,tv18,tv19,tv20;
    Button button1;
    KeyPair pair = null;
    String signature = null;
    public String plainText;
    private static String privateKeyString;
    private static String publicKeyString;
    private static String message;

    private PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        InputStream is = getAssets().open("pkcs8_key");
        byte[] fileBytes = new byte[is.available()];
        is.read(fileBytes);
        is.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(fileBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private PublicKey getPublicKey() throws IOException, CertificateException {
        //READING PUBLIC KEY FROM CERTIFICATE
        InputStream stream = getAssets().open("cert.pem");
        CertificateFactory of = CertificateFactory.getInstance("X.509");
        X509Certificate clientcert = (X509Certificate) of.generateCertificate(stream);

        return clientcert.getPublicKey();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String sign(PrivateKey privateKey,String plainText) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));
        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean verify(String signature, PublicKey publicKey,String plainText) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdl);

        getSupportActionBar().setElevation(0);

      /*  ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33daff")));
*/
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

        button1=findViewById(R.id.button);

        tv9=findViewById(R.id.textView9);
        tv10=findViewById(R.id.textView10);
        tv11=findViewById(R.id.textView11);
        tv12=findViewById(R.id.textView12);
        tv13=findViewById(R.id.textView13);
        tv14=findViewById(R.id.textView14);
        tv17=findViewById(R.id.textView17);
        tv18=findViewById(R.id.textView18);
        tv20=findViewById(R.id.textView20);

        //signature generation

            SharedPreferences sharedPref2 = getSharedPreferences("Main_Activity", Context.MODE_PRIVATE);
            message = sharedPref2.getString("obj", "1234");
            Log.i("json",message);
            plainText = message;

                    try {
                        signature = sign(getPrivateKey(),plainText);
                      /*  if(signature!=null)
                            t3.setText(signature);*/

                        //Shared Preference
                        SharedPreferences sharedPref1 = getSharedPreferences("Signature", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref1.edit();

                        editor.putString("digi_sign", signature);
                        boolean editorResult = editor.commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    try {
                        verify(signature, getPublicKey(),plainText);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //System.out.println("Signature correct: " + isCorrect);


        try {
            JSONObject obj = new JSONObject(message);
            tv9.setText(obj.getString("validity"));
            tv10.setText(obj.getString("name"));
            tv11.setText(obj.getString("parent"));
            tv12.setText(obj.getString("address"));
            tv13.setText(obj.getString("pin"));
            tv14.setText(obj.getString("phno"));
            tv17.setText(obj.getString("dlno"));
            tv18.setText(obj.getString("issue"));
            tv20.setText(obj.getString("dob"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //function for encryption of data
      /*  GenerateRsaKeyPair();
        ClientEncrypt();*/

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewDL.this, QRCode.class);
                startActivity(intent);
            }
        });

    }

    //key pair generation function
   /* public void GenerateRsaKeyPair() {
        try {

            // 1. generate public key and private key
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024); // key length
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            privateKeyString = android.util.Base64.encodeToString(keyPair.getPrivate().getEncoded(), android.util.Base64.DEFAULT);
            publicKeyString = android.util.Base64.encodeToString(keyPair.getPublic().getEncoded(), android.util.Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void ClientEncrypt() {
        try {


            // 1. generate secret key using AES
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // AES is currently available in three key sizes: 128, 192 and 256 bits.The design and strength of all key lengths of the AES algorithm are sufficient to protect classified information up to the SECRET level
            SecretKey secretKey = keyGenerator.generateKey();

            // 2. get string which needs to be encrypted
            String text = message;

            // 3. encrypt string using secret key
            byte[] raw = secretKey.getEncoded();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            String cipherTextString = Utility.convertBytesToHex(cipher.doFinal(text.getBytes(Charset.forName("UTF-8"))));

            // 4. get public key
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(android.util.Base64.decode(publicKeyString, android.util.Base64.DEFAULT));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicSpec);

            // 6. encrypt secret key using public key
            Cipher cipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            cipher2.init(Cipher.ENCRYPT_MODE, publicKey);
            String encryptedSecretKey =Utility.convertBytesToHex(cipher2.doFinal(secretKey.getEncoded()));

            //Shared pref to ble callback
            //Shared Preference
            SharedPreferences sharedPref = getSharedPreferences("Encryption", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("obje", cipherTextString);
            editor.putString("seckey", encryptedSecretKey);
            editor.commit();


        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }*/


}
