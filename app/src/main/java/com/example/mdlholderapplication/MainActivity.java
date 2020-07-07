package com.example.mdlholderapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    private TextInputLayout inputLayoutName, inputLayoutDob, inputLayoutAddress,inputLayoutNumber,inputLayoutDLNumber,inputLayoutPin, inputLayoutParent,inputLayoutIssue,inputLayoutValidity;
    Button button;
    EditText ed,ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8;
    public final static String name ="abc";
    public final static String dob ="15/04/1998";
    public final static String address ="xyz";
    public final static String phno ="9818103010";
    public final static String dlno ="def123";
    public final static String pin ="4444";
    public final static String parent ="def";
    public final static String issue ="15/04/2020";
    public final static String validity ="15/04/2021";
    public static final String MyPREFERENCES = "MyPrefs" ;
    private Context mContext = null;
    private JSONObject obj;

    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

      /*  ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33daff")));
*/
        tv=findViewById(R.id.textView);

        inputLayoutName = findViewById(R.id.input_layout_name);
        inputLayoutDob =  findViewById(R.id.input_layout_dob);
        inputLayoutParent =  findViewById(R.id.input_layout_parent);
        inputLayoutAddress = findViewById(R.id.input_layout_address);
        inputLayoutPin = findViewById(R.id.input_layout_pin);
        inputLayoutNumber =  findViewById(R.id.input_layout_number);
        inputLayoutDLNumber = findViewById(R.id.input_layout_dlnumber);
        inputLayoutIssue = findViewById(R.id.input_layout_issue);
        inputLayoutValidity = findViewById(R.id.input_layout_validity);

        ed=findViewById(R.id.input_name);
        ed1=findViewById(R.id.input_dob);
        ed2=findViewById(R.id.input_parent);
        ed3=findViewById(R.id.input_address);
        ed4=findViewById(R.id.input_pin);
        ed5=findViewById(R.id.input_number);
        ed6=findViewById(R.id.input_dlnumber);
        ed7=findViewById(R.id.input_issue);
        ed8=findViewById(R.id.input_validity);


        ed.addTextChangedListener(new MyTextWatcher(ed));
        ed1.addTextChangedListener(new MyTextWatcher(ed1));
        ed2.addTextChangedListener(new MyTextWatcher(ed2));
        ed3.addTextChangedListener(new MyTextWatcher(ed3));
        ed4.addTextChangedListener(new MyTextWatcher(ed4));
        ed5.addTextChangedListener(new MyTextWatcher(ed5));
        ed6.addTextChangedListener(new MyTextWatcher(ed6));
        ed7.addTextChangedListener(new MyTextWatcher(ed7));
        ed8.addTextChangedListener(new MyTextWatcher(ed8));


        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
            public void onClick(View v) {
                                          submitForm();
                if (ed.getText().toString().equalsIgnoreCase("")) {
                    ed.setError("enter a valid name");//it gives user to info message //use any one //
                }
                else if (ed1.getText().toString().equalsIgnoreCase("")) {
                    ed1.setError("enter a valid date of birth");
                }
                else if (ed2.getText().toString().equalsIgnoreCase("")) {
                    ed2.setError("enter a valid name for parent");
                }
                else if (ed3.getText().toString().equalsIgnoreCase("")) {
                    ed3.setError("enter a valid address");
                }
                else if (ed4.getText().toString().equalsIgnoreCase("")) {
                    ed4.setError("enter a valid postal pin");
                }
                else if (ed5.getText().toString().equalsIgnoreCase("")) {
                    ed5.setError("enter a valid phone no");
                }
                else if (ed6.getText().toString().equalsIgnoreCase("")) {
                    ed6.setError("enter a valid driving licence no");
                }
                else if (ed7.getText().toString().equalsIgnoreCase("")) {
                    ed7.setError("enter a valid issue date");
                }
                else if (ed8.getText().toString().equalsIgnoreCase("")) {
                    ed8.setError("enter a valid expiry date");
                }
                else {
                    //sendDataToServer();
                Intent intent = new Intent(MainActivity.this, ViewDL.class);

                intent.putExtra("name", ed.getText().toString());
                intent.putExtra("dob", ed1.getText().toString());
                intent.putExtra("parent", ed2.getText().toString());
                intent.putExtra("address", ed3.getText().toString());
                intent.putExtra("pin", ed4.getText().toString());
                intent.putExtra("phno", ed5.getText().toString());
                intent.putExtra("dlno", ed6.getText().toString());
                intent.putExtra("issue", ed7.getText().toString());
                intent.putExtra("validity", ed8.getText().toString());


                    //JSON Object
                    try {
                        obj = new JSONObject();
                        // Set the first name/pair
                        obj.put("name", ed.getText().toString());
                        obj.put("dob", ed1.getText().toString());
                        obj.put("parent", ed2.getText().toString());
                        obj.put("address", ed3.getText().toString());
                        obj.put("pin", ed4.getText().toString());
                        obj.put("phno", ed5.getText().toString());
                        obj.put("dlno", ed6.getText().toString());
                        obj.put("issue", ed7.getText().toString());
                        obj.put("validity", ed8.getText().toString());

                        Log.d("My App",obj.toString());

                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + obj + "\"");
                    }


               /* //Shared Preferences
                SharedPreferences sharedpreferences;
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("pin", "ed5.getText().toString");
                editor.commit();

                editor.putString("name", "ed.getText().toString");
                editor.commit();*/

               //Shared Preference
                    SharedPreferences sharedPref = getSharedPreferences("Main_Activity", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("obj", obj.toString());
                    editor.commit();
                startActivity(intent);

                   MainActivity.this.finish();
            }
            }
        }

        );
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateDob()) {
            return;
        }

        if (!validateParent()) {
            return;
        }

        if (!validateAddress()) {
            return;
        }

        if (!validatePin()) {
            return;
        }

        if (!validateNumber()) {
            return;
        }

        if (!validateDLNumber()) {
            return;
        }


        if (!validateIssue()) {
            return;
        }


        if (!validateValidity()) {
            return;
        }



        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }
    private boolean validateName() {
        if (ed.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(ed);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDob() {
        String dob = ed1.getText().toString().trim();

        if (dob.isEmpty()) {
            inputLayoutDob.setError(getString(R.string.err_msg_dob));
            requestFocus(ed1);
            return false;
        } else {
            inputLayoutDob.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateParent() {
        if (ed2.getText().toString().trim().isEmpty()) {
            inputLayoutParent.setError(getString(R.string.err_msg_parent));
            requestFocus(ed2);
            return false;
        } else {
            inputLayoutParent.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateAddress() {
        if (ed3.getText().toString().trim().isEmpty()) {
            inputLayoutAddress.setError(getString(R.string.err_msg_address));
            requestFocus(ed3);
            return false;
        } else {
            inputLayoutAddress.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePin() {
        if (ed4.getText().toString().trim().isEmpty()) {
            inputLayoutPin.setError(getString(R.string.err_msg_pin));
            requestFocus(ed4);
            return false;
        } else {
            inputLayoutPin.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNumber() {
        if (ed5.getText().toString().trim().isEmpty()) {
            inputLayoutNumber.setError(getString(R.string.err_msg_number));
            requestFocus(ed5);
            return false;
        } else {
            inputLayoutNumber.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDLNumber() {
        if (ed6.getText().toString().trim().isEmpty()) {
            inputLayoutDLNumber.setError(getString(R.string.err_msg_dlnumber));
            requestFocus(ed6);
            return false;
        } else {
            inputLayoutDLNumber.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateIssue() {
        String dob = ed7.getText().toString().trim();

        if (dob.isEmpty()) {
            inputLayoutIssue.setError(getString(R.string.err_msg_issue));
            requestFocus(ed7);
            return false;
        } else {
            inputLayoutIssue.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateValidity() {
        String dob = ed8.getText().toString().trim();

        if (dob.isEmpty()) {
            inputLayoutValidity.setError(getString(R.string.err_msg_validity));
            requestFocus(ed8);
            return false;
        } else {
            inputLayoutValidity.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_dob:
                    validateDob();
                    break;
                case R.id.input_parent:
                    validateParent();
                    break;
                case R.id.input_address:
                    validateAddress();
                    break;
                case R.id.input_pin:
                    validatePin();
                    break;
                case R.id.input_number:
                    validateNumber();
                    break;
                case R.id.input_dlnumber:
                    validateDLNumber();
                    break;
                case R.id.input_issue:
                    validateIssue();
                    break;
                case R.id.input_validity:
                    validateValidity();
                    break;
            }
        }
    }
}
