package demo.app.adcharge.eu.sdkdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.app.adcharge.eu.sdkdemo.Utilites.Config;
import demo.app.adcharge.eu.sdkdemo.Utilites.Constants;
import demo.app.adcharge.eu.sdkdemo.Utilites.Helpers;
import demo.app.adcharge.eu.sdkdemo.background.VolleySingleton;
import eu.adcharge.api.ApiException;
import eu.adcharge.api.ApiValidationException;
import eu.adcharge.api.entities.User;
import eu.adcharge.sdk.logic.AdCharge;

import static android.view.KeyEvent.KEYCODE_DEL;
import static demo.app.adcharge.eu.sdkdemo.Utilites.Constants.TRAFFIC_SOURCE_KEY;

public class completeinfo_Verify_Activity extends AppCompatActivity {
    EditText[] otpETs = new EditText[6];
    Button bt_verify;
    String et_code;
    Intent intent;
    private static final Random generator = new Random();
    String verify_url;
    String fullname,username,password,email,phonenumber,gender,birthday;
    SharedPreferences sharedpreferences;
    TextView tryagain,tv_code;
    private static final AdCharge.Settings SETTINGS = new AdCharge.Settings();
    AdCharge adcharge;

    private String KEY_MODE ="mode";
    private String KEY_FNAME ="user_fullname";
    private String KEY_EMAIL = "user_email";
    private String KEY_MOBILE = "user_mobile";
    private String KEY_USERNAME ="user_username";
    private String KEY_ID= "device_id";
    private String KEY_PASS= "user_pass";
    private String KEY_BIRTHDAY= "user_dob";
    private String KEY_GENDER= "user_gender";
    String androidId = "";
    SweetAlertDialog pDialog;

    private String KEY_PHONE ="user_phone";
    private String KEY_MSG = "send_msg";

    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeinfo__verify_);
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        setContentView(R.layout.activity_verify_);
        try {
            adcharge = new AdCharge(Constants.URL, completeinfo_Verify_Activity.this, SETTINGS);


            // new SMSGatewayCenter();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        tryagain=(TextView) findViewById(R.id.tryagain);
        tv_code=(TextView) findViewById(R.id.code);
        intent = getIntent();
        fullname = intent.getStringExtra("fullname");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        email = intent.getStringExtra("email");
        phonenumber = intent.getStringExtra("phonenumber");
        gender= intent.getStringExtra("gender");
        birthday= intent.getStringExtra("birthday");


        otpETs[0] = findViewById(R.id.otpET1);
        otpETs[1] = findViewById(R.id.otpET2);
        otpETs[2] = findViewById(R.id.otpET3);
        otpETs[3] = findViewById(R.id.otpET4);
        otpETs[4] = findViewById(R.id.otpET5);
        otpETs[5] = findViewById(R.id.otpET6);
        otpETs[0].setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66) {
                    otpETs[1].requestFocus();
                }
                return false;
            }
        });

        otpETs[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpETs[1].requestFocus();
                }
                else if(s.length()==0)
                {
                    otpETs[0].requestFocus();
                }
            }
        });
        otpETs[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpETs[2].requestFocus();
                }
                else if(s.length()==0)
                {
                    otpETs[0].requestFocus();
                }
            }
        });
        otpETs[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpETs[3].requestFocus();
                }
                else if(s.length()==0)
                {
                    otpETs[1].requestFocus();
                }
            }
        });
        otpETs[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpETs[4].requestFocus();
                }
                else if(s.length()==0)
                {
                    otpETs[2].requestFocus();
                }
            }
        });
        otpETs[4].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    otpETs[5].requestFocus();
                }
                else if(s.length()==0)
                {
                    otpETs[3].requestFocus();
                }
            }
        });
        otpETs[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    //otpETs[5].requestFocus();
                }
                else if(s.length()==0)
                {
                    otpETs[4].requestFocus();
                }
            }
        });




        bt_verify=(Button)findViewById(R.id.verify) ;
        bt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new SweetAlertDialog(completeinfo_Verify_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                et_code = Helpers.rS(otpETs[0]) + Helpers.rS(otpETs[1]) +
                        Helpers.rS(otpETs[2]) + Helpers.rS(otpETs[3]) + Helpers.rS(otpETs[4])
                        + Helpers.rS(otpETs[5]);

                code =sharedpreferences.getString("code", null); // getting String
                if(et_code.equals(code))
                {
                    Toast.makeText(completeinfo_Verify_Activity.this, "Verified", Toast.LENGTH_SHORT).show();

                    registration_post(Constants.main_url);


                }
                else{
                    Toast.makeText(completeinfo_Verify_Activity.this, "Invalide Code ,Please try again ", Toast.LENGTH_SHORT).show();
                    pDialog.cancel();
                }
            }
        });

        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Verificaton();
                // Toast.makeText(Verify_Activity.this, code, Toast.LENGTH_SHORT).show();
            }
        });


        Verificaton();
    }


    public void Verificaton()
    {


        if (Config.cekKoneksi(completeinfo_Verify_Activity.this)) {
            String a=getRandomNumberString();
            tv_code.setText(a);
            sharedpreferences = getSharedPreferences("code", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("code", a);
            editor.commit();
            String Phonenumber=Constants.country_code +phonenumber;
            Toast.makeText(this, a, Toast.LENGTH_SHORT).show();

            smsgateway_post(a,Phonenumber);
            //  Toast.makeText(Verify_Activity.this, getRandomNumberString(), Toast.LENGTH_SHORT).show();
            //    verify_url= Constants.Verify_one+"923435959764"+Constants.Verify_two+"code123456"+Constants.Verify_end;
            //get_OTP(verify_url);
        } else {
            Toast.makeText(completeinfo_Verify_Activity.this,"Check your Internet Connection",Toast.LENGTH_LONG).show();

        }
    }
    private int checkWhoHasFocus() {
        for (int i = 0; i < otpETs.length; i++) {
            EditText tempET = otpETs[i];
            if (tempET.hasFocus()) {
                return i;
            }
        }
        return 123;
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == 7 || keyCode == 8 ||
                keyCode == 9 || keyCode == 10 ||
                keyCode == 11 || keyCode == 12 ||
                keyCode == 13 || keyCode == 14 ||
                keyCode == 15 || keyCode == 16 ||
                keyCode == 67) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KEYCODE_DEL) {
                    int index = checkWhoHasFocus();
                    if (index != 123) {
                        if (Helpers.rS(otpETs[index]).equals("")) {
                            if (index != 0) {
                                otpETs[index - 1].requestFocus();
                            }
                        } else {
                            return super.dispatchKeyEvent(event);
                        }
                    }
                } else {
                    int index = checkWhoHasFocus();
                    if (index != 123) {
                        if (Helpers.rS(otpETs[index]).equals("")) {
                            return super.dispatchKeyEvent(event);
                        } else {
                            if (index != 5) {
                                otpETs[index + 1].requestFocus();
                            }
                        }
                    }
                    return super.dispatchKeyEvent(event);
                }
            }
        } else {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }

    private void get_OTP(final String url) {

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("waqas", "onResponse: " + response);
                        //pDialog.setVisibility(View.GONE);
                        // hideDialog();

                        Toast.makeText(completeinfo_Verify_Activity.this, response, Toast.LENGTH_SHORT).show();




                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(completeinfo_Verify_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        VolleySingleton.getInstance(completeinfo_Verify_Activity.this).addToRequestQueue(stringRequest);

    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public void registration_post(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog


                        try {


                            JSONArray jsonArray = new JSONArray(s);
                            for (int n = 0; n < jsonArray.length(); n++) {
                                JSONObject claims = jsonArray.getJSONObject(n);

                                String status= claims.optString("status");
                                String msg= claims.optString("msg");

                                if(status.equals("true"))
                                {
                                    new LoginUserTask(username, password).execute();
                                    pDialog.cancel();
                                }
                            }


                        } catch (JSONException e) {
                            // Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_SHORT).show();
                            // showToast(coordinatorLayout,s);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        pDialog.cancel();

                        pDialog.cancel();
                        //Showing toast
                        Toast.makeText(completeinfo_Verify_Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();



                params.put(KEY_MODE, "Rx5rOn07H5d64KShGMh+iw==");
                params.put(KEY_FNAME,fullname);
                params.put(KEY_EMAIL,email);
                params.put(KEY_MOBILE,phonenumber);
                params.put(KEY_USERNAME, username);
                params.put(KEY_ID, androidId);
                params.put(KEY_PASS, password);
                params.put(KEY_BIRTHDAY, birthday);
                params.put(KEY_GENDER, gender);

                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(completeinfo_Verify_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    private class LoginUserTask extends AsyncTask<String, Void, Void> {
        private String username;
        private String password;
        private ApiValidationException validationException;



        LoginUserTask(String username, String password) {
            this.username = username;
            this.password = password;

        }
        @Override
        protected Void doInBackground(String... strings) {


            try {
                adcharge.login(username, password, TRAFFIC_SOURCE_KEY, completeinfo_Verify_Activity.this);

            } catch (IOException e) {
                pDialog.cancel();
                //  Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                pDialog.cancel();
                //  Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            } catch (ApiValidationException e) {
                validationException = e;//to be processed in ui thread
                //Toast.makeText(Login_Activity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (validationException != null) {
                for (ApiValidationException.Error fieldError : validationException.getFieldErrors()) {
                    String field = fieldError.getField();
                    String message = fieldError.getMessages().isEmpty() ? "" : fieldError.getMessages().get(0);
                    if ("username".equals(field)) {
                        // binding.login.setError(message);
                        Toast.makeText(completeinfo_Verify_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    } else if ("password".equals(field)) {
                        Toast.makeText(completeinfo_Verify_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    } else {
                        Toast.makeText(completeinfo_Verify_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }
                }
            } else {

                //   Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                if(adcharge.isLoggedIn())
                {

                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    Intent intent=new Intent(completeinfo_Verify_Activity.this,Category_Activity.class);
                    startActivity(intent);
                    completeinfo_Verify_Activity.this.finish();

                }


                pDialog.cancel();
            }
        }
    }

    public void smsgateway_post( String ce,String phne) {

        final String code=ce;
        final String phn=phne;
        pDialog = new SweetAlertDialog(completeinfo_Verify_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.main_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        // Toast.makeText(Verify_Activity.this, s, Toast.LENGTH_LONG).show();
//                        try {
//
//
//                            JSONArray jsonArray = new JSONArray(s);
//                            for (int n = 0; n < jsonArray.length(); n++) {
//                                JSONObject claims = jsonArray.getJSONObject(n);
//
//                                String status= claims.optString("status");
//                                String msg= claims.optString("msg");
//
//                                if(status.equals("true"))
//                                {
//
//                                    pDialog.cancel();
//                                }
//                            }
//
//
//                        } catch (JSONException e) {
//                            // Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_SHORT).show();
//                            // showToast(coordinatorLayout,s);
//                            e.printStackTrace();
//                        }
                        pDialog.cancel();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        pDialog.cancel();


                        //Showing toast
                        Toast.makeText(completeinfo_Verify_Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();


                params.put(KEY_MODE, "pRtS4oTznbnTlCjT7GWpCxLs+uRTErfL");
                params.put(KEY_PHONE,phn);
                params.put(KEY_MSG,code);

                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(completeinfo_Verify_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
