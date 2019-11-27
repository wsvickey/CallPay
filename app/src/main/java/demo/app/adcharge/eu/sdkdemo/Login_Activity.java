package demo.app.adcharge.eu.sdkdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
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
import eu.adcharge.api.ApiException;
import eu.adcharge.api.ApiValidationException;
import eu.adcharge.sdk.logic.AdCharge;

import static android.Manifest.permission_group.LOCATION;
import static demo.app.adcharge.eu.sdkdemo.Utilites.Config.showToast;
import static demo.app.adcharge.eu.sdkdemo.Utilites.Constants.TRAFFIC_SOURCE_KEY;
import static demo.app.adcharge.eu.sdkdemo.Utilites.Constants.URL;

public class Login_Activity extends Activity {
    Button Signin;
    EditText et_username,et_password;
    AdCharge adcharge;
    private String KEY_MODE ="mode";
    private String KEY_ID= "device_id";
    private String KEY_USERNAME= "username";
    TextView tv_Signup,text;
    SweetAlertDialog pDialog;
    static final Integer LOCATION = 0x1;
    static final Integer CALL = 0x2;
    private static final AdCharge.Settings SETTINGS = new AdCharge.Settings(); // default settings
    TextInputLayout pass_InputLayout;
    String sture="true";
    String sfalse="false";
    String androidId = "";
    String tvstatus;
    String name,pass,idusername;
    String status;
    TextView tv_forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login_);
        SharedPreferences settings = getSharedPreferences("firstTime", MODE_PRIVATE);
        boolean firstTime = settings.getBoolean("first_launch", true); //0 is the default valu

        if (Config.cekKoneksi(Login_Activity.this)) {

            try {
                adcharge = new AdCharge(URL, Login_Activity.this, SETTINGS);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(Login_Activity.this,"Check your Internet Connection",Toast.LENGTH_LONG).show();

        }

        if (firstTime) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("first_launch", false);
            editor.apply();
        }

        SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String logname = pref.getString("logout", "");
        if(sfalse.equals(logname))
        {
            // Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
            if(adcharge.isLoggedIn())
            {
                Intent intent=new Intent(Login_Activity.this,Splash_Act.class);
                startActivity(intent);
                Login_Activity.this.finish();
            }
            else{

                //Toast.makeText(Login_Activity.this, "Please Login ", Toast.LENGTH_SHORT).show();

            }
        }
        else if(sture.equals(logname)){
            Toast.makeText(Login_Activity.this, "Please Login ", Toast.LENGTH_SHORT).show();
        }
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);

        pass_InputLayout=(TextInputLayout) findViewById(R.id.password);
        tv_forget=(TextView) findViewById(R.id.forget);
        //isPermissionGranted();

        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Login_Activity.this, getRandomNumberString(), Toast.LENGTH_SHORT).show();
                pDialog = new SweetAlertDialog(Login_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#25326c"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                name = et_username.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    pDialog.dismiss();
                    et_username.setError("Please enter Username");

                    return;
                }

                forget_API();
            }

        });

        text=(TextView) findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Login_Activity.this, getRandomNumberString(), Toast.LENGTH_SHORT).show();
            }
        });





























































































        SharedPreferences stt = getSharedPreferences("status", MODE_PRIVATE);
        status = stt.getString("status", ""); //0 is the default valu
        idusername= stt.getString("idusername", "");
        //  Toast.makeText(this, idusername+status, Toast.LENGTH_SHORT).show();
        et_username=(EditText)findViewById(R.id.et_username);
        et_password=(EditText)findViewById(R.id.ed_pass);
        tv_Signup=(TextView) findViewById(R.id.signup);
        Signin=(Button)findViewById(R.id.login);
        if(status.equals("true"))
        {
            tv_Signup.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            et_username.setText(idusername);
            //   text.setText(androidId);
        }
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermission(Manifest.permission.CALL_PHONE,CALL);
                pDialog = new SweetAlertDialog(Login_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                name = et_username.getText().toString().trim();
                pass = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    pDialog.dismiss();
                    et_username.setError("Please enter Username");

                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    pDialog.dismiss();
                    // et_password.setError("You must have 8 characters in your password");
                    Toast.makeText(Login_Activity.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                    //  pass_InputLayout.setPasswordVisibilityToggleEnabled(false);
                    return;
                }

                if (Config.cekKoneksi(Login_Activity.this)) {

                    if(idusername.equals(name))
                    {
                        Login_with_API();
                    }
                    else{
                        Toast.makeText(Login_Activity.this,"This username doesn't matched with register mobile.",Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }

                } else {
                    Toast.makeText(Login_Activity.this,"Check your Internet Connection",Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }



            }
        });

        tv_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermission(Manifest.permission.CALL_PHONE,CALL);
                Intent intent=new Intent(Login_Activity.this,Register_Activity.class);
                startActivity(intent);
                //  Login_Activity.this.finish();
            }
        });
    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Login_Activity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login_Activity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Login_Activity.this, new String[]{permission}, requestCode);



            } else {

                ActivityCompat.requestPermissions(Login_Activity.this, new String[]{permission}, requestCode);
            }
        } else {

            // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }
    private class LoginUserTask extends AsyncTask<String, Void, Void> {
        private String username;
        private String password;
        private ApiValidationException validationException;


        public LoginUserTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Void doInBackground(String... strings) {


            try {
                adcharge.login(username, password, TRAFFIC_SOURCE_KEY, Login_Activity.this);

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
                        Toast.makeText(Login_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    } else if ("password".equals(field)) {
                        Toast.makeText(Login_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    } else {
                        Toast.makeText(Login_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }

                }

            } else {

                //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                if(adcharge.isLoggedIn())
                {
                    pDialog.dismiss();
                    Intent intent=new Intent(Login_Activity.this, Dashboard_Activity.class);
                    startActivity(intent);
                    Login_Activity.this.finish();


                }


                pDialog.cancel();
            }
        }
    }
    private class Completeinfo_loginUserTask extends AsyncTask<String, Void, Void> {
        private String username;
        private String password;
        private ApiValidationException validationException;


        public Completeinfo_loginUserTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Void doInBackground(String... strings) {


            try {
                adcharge.login(username, password, TRAFFIC_SOURCE_KEY, Login_Activity.this);

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
                pDialog.dismiss();
                for (ApiValidationException.Error fieldError : validationException.getFieldErrors()) {
                    String field = fieldError.getField();
                    String message = fieldError.getMessages().isEmpty() ? "" : fieldError.getMessages().get(0);
                    if ("username".equals(field)) {
                        // binding.login.setError(message);
                        Toast.makeText(Login_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    } else if ("password".equals(field)) {
                        Toast.makeText(Login_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    } else {
                        Toast.makeText(Login_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }
                }
            }

            else

            {

                if(adcharge.isLoggedIn())
                {
                    pDialog.dismiss();
                    Intent intent=new Intent(Login_Activity.this, Complete_info_Activity.class);
                    intent.putExtra("pass",password);
                    startActivity(intent);
                    finish();



                }


                pDialog.cancel();
            }
        }
    }
    public void Login_with_API() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.main_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        //   Toast.makeText(Login_Activity.this, s, Toast.LENGTH_LONG).show();

                        try {


                            JSONArray jsonArray = new JSONArray(s);
                            for (int n = 0; n < jsonArray.length(); n++) {
                                JSONObject claims = jsonArray.getJSONObject(n);

                                tvstatus= claims.optString("status");
                                String msg= claims.optString("msg");
                                // Toast.makeText(Login_Activity.this, tvstatus, Toast.LENGTH_LONG).show();

                                if(tvstatus.equals("true"))
                                {
                                    new LoginUserTask(name, pass).execute();
                                    //new Completeinfo_loginUserTask(name, pass).execute();
                                }
                                else if(tvstatus.equals("false"))
                                {

                                    new Completeinfo_loginUserTask(name, pass).execute();
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

                        //Showing toast
                        Toast.makeText(Login_Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();



                params.put(KEY_MODE, "Z8OK3erPnyIXb3JbGrM7FkUrq82NrvKT");
                params.put(KEY_ID, androidId);
                params.put(KEY_USERNAME, et_username.getText().toString().trim());


                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Login_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void forget_API() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.main_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        //   Toast.makeText(Login_Activity.this, s, Toast.LENGTH_LONG).show();

                        try {

                            pDialog.dismiss();
                            JSONArray jsonArray = new JSONArray(s);
                            for (int n = 0; n < jsonArray.length(); n++) {
                                JSONObject claims = jsonArray.getJSONObject(n);

                                tvstatus= claims.optString("status");
                                String msg= claims.optString("msg");
                                String userpass= claims.optString("userpass");
                                String username= claims.optString("username");

                                if (name.equals(username))
                                {
                                    SweetAlertDialog ppDialog= new SweetAlertDialog(Login_Activity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                                    ppDialog.setTitleText(userpass);
                                    ppDialog.setContentText("Check Your Password");
                                    ppDialog.setCustomImage(R.drawable.ic_lock_outline_black_24dp);
                                    ppDialog.show();
                                    Button btn = (Button) ppDialog.findViewById(R.id.confirm_button);
                                    btn.setBackgroundColor(ContextCompat.getColor(Login_Activity.this,R.color.colorPrimary));
                                }

                                else
                                {
                                    Toast.makeText(Login_Activity.this,"Please Enter Correct Username", Toast.LENGTH_LONG).show();
                                }

//                                if(tvstatus.equals("true"))
//                                {
//                                    new LoginUserTask(name, pass).execute();
//                                    //new Completeinfo_loginUserTask(name, pass).execute();
//                                }
//                                else if(tvstatus.equals("false"))
//                                {
//
//                                    new Completeinfo_loginUserTask(name, pass).execute();
//                                }

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

                        //Showing toast
                        Toast.makeText(Login_Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();



                params.put(KEY_MODE, "ol8zT8gezOfL3lLBQdJISa0534E8UMUI");
                params.put(KEY_ID, androidId);
                // params.put(KEY_USERNAME, et_username.getText().toString().trim());


                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Login_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



}
