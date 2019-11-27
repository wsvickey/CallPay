package demo.app.adcharge.eu.sdkdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.app.adcharge.eu.sdkdemo.Utilites.Constants;
import demo.app.adcharge.eu.sdkdemo.Utilites.DatePicker;
import eu.adcharge.api.ApiException;
import eu.adcharge.api.ApiValidationException;
import eu.adcharge.api.entities.User;
import eu.adcharge.sdk.logic.AdCharge;

import static demo.app.adcharge.eu.sdkdemo.Utilites.Config.cekKoneksi;
import static demo.app.adcharge.eu.sdkdemo.Utilites.Constants.URL;

public class Complete_info_Activity extends AppCompatActivity {
    String androidId = "";
    private String KEY_MODE ="mode";
    private String KEY_FNAME ="user_fullname";
    private String KEY_EMAIL = "user_email";
    private String KEY_MOBILE = "user_mobile";
    private String KEY_USERNAME ="user_username";
    private String KEY_ID= "device_id";
    private String KEY_PASS= "user_pass";
    private String KEY_BIRTHDAY= "user_dob";
    private String KEY_GENDER= "user_gender";
    String password;


    private static final AdCharge.Settings SETTINGS = new AdCharge.Settings()
            // # Ask user for location
            .useLocation(true)
            //       or .useLocation(true)
            //
            // # Configuration of small banner
            .smallBanner(
                    new AdCharge.Settings.SmallBanner()
                            // # show or do not show small banner
                            .enable(true)
                            //       or .enable(false)
                            //
                            // # initial placement on screen
                            .initialPosition(AdCharge.Settings.SmallBanner.InitialPosition.MIDDLE)
                            //       or .initialPosition(AdCharge.Settings.SmallBanner.InitialPosition.TOP)
                            //       or .initialPosition(AdCharge.Settings.SmallBanner.InitialPosition.BOTTOM)
                            //
                            // # chose draggable area
                            .dragSensitiveArea(AdCharge.Settings.SmallBanner.DragSensitiveArea.HOLE_BANNER)
                            //       or .dragSensitiveArea(AdCharge.Settings.SmallBanner.DragSensitiveArea.DRAG_ICON)
                            //       or .dragSensitiveArea(AdCharge.Settings.SmallBanner.DragSensitiveArea.NONE)
                            //
                            // # display or hide 'drag icon'
                            .dragIconDisplayed(false)
                    //       or .dragIconDisplayed(true)
            ); // default settings

    CoordinatorLayout coordinatorLayout;
    AdCharge adcharge;
    String sdk_name,sdk_pass,sdk_dateofbirth,sdk_gender;
    SweetAlertDialog pDialog;
    EditText et_fullname,et_email,et_phone;
    Button ed_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        setContentView(R.layout.activity_complete_info_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //getSupportActionBar().hide();

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        password=getIntent().getStringExtra("pass");
        pDialog = new SweetAlertDialog(Complete_info_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        coordinatorLayout =(CoordinatorLayout) findViewById(R.id.coordinator);
        if (cekKoneksi(this)) {
            try {
                pDialog.show();
                adcharge = new AdCharge(URL, Complete_info_Activity.this, SETTINGS);
                new UserGetTask().execute();
            }

            catch (MalformedURLException e)

            {
                e.printStackTrace();
            }

        } else {
            //  Toast.makeText(context,"Check your Internet Connection",Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, R.string.internet_not_connect, Snackbar.LENGTH_LONG)
                    .setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "please turn on  Internet Connection!", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    });

            snackbar.show();
        }

        et_fullname=(EditText)findViewById(R.id.et_fullname);
        et_email=(EditText)findViewById(R.id.et_email);
        et_phone=(EditText)findViewById(R.id.field_phone_number);
        ed_save=(Button)findViewById(R.id.signup);

        ed_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new SweetAlertDialog(Complete_info_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();


                final String fulname = et_fullname.getText().toString().trim();
                final String eml = et_email.getText().toString().trim();
                final String phn = et_phone.getText().toString().trim();
                if(sdk_name ==null)
                {
                    new UserGetTask().execute();
                }
                if (TextUtils.isEmpty(fulname)) {
                    pDialog.dismiss();
                    et_fullname.setError("Please enter Full Name");

                    return;
                }

                if (TextUtils.isEmpty(eml)) {
                    pDialog.dismiss();
                    et_email.setError("Please enter Email");

                    return;
                }
                String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
                Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(eml);
                if (!matcher.matches()) {

                    et_email.setError("Invalid Email Address");

                }
                if (TextUtils.isEmpty(phn)||phn.length() < 10) {
                    pDialog.dismiss();
                    et_phone.setError("Please enter correct Phone number");

                    return;
                }



                if (cekKoneksi(Complete_info_Activity.this)) {
                   // registration_post();
                    Intent loginActivity = new Intent(getApplicationContext(), completeinfo_Verify_Activity.class);
                    loginActivity.putExtra("fullname",et_fullname.getText().toString());
                    loginActivity.putExtra("username",sdk_name);
                    loginActivity.putExtra("password",password);
                    loginActivity.putExtra("email",et_email.getText().toString());
                    loginActivity.putExtra("phonenumber","91"+et_phone.getText().toString());
                    loginActivity.putExtra("birthday",sdk_dateofbirth);
                    loginActivity.putExtra("gender",sdk_gender);
                    startActivity(loginActivity);
                    finish();

                    pDialog.cancel();
                } else {

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, R.string.internet_not_connect, Snackbar.LENGTH_LONG)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "please turn on  Internet Connection!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();
                                }
                            });

                    snackbar.show();
                }
            }
        });
    }
    private class UserGetTask extends AsyncTask<String, Void, User> {
        private ApiValidationException validationException;

        @Override
        protected User doInBackground(String... strings) {
            try {

                return adcharge.getApiWrapper().getUserInfo();
            } catch (IOException e) {
                //  binding.setError(e.getMessage());
            } catch (ApiException e) {
                // username.setText(e.getMessage());
            } catch (ApiValidationException e)
            {
                validationException = e;//to be processed in ui thread
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            if (validationException != null) {
                for (ApiValidationException.Error fieldError : validationException.getFieldErrors()) {
                    String field = fieldError.getField();
                    String message = fieldError.getMessages().isEmpty() ? "" : fieldError.getMessages().get(0);
                    // username.setText(field + ": " + message);
                }
            } else {


        sdk_name=user.getUsername();
       // sdk_dateofbirth=user.getBirthday().toString();
       // sdk_gender=user.getGender().toString();
                sdk_dateofbirth="02-01-1992";
               sdk_gender="Male";
        sdk_pass=user.getPassword();
                pDialog.cancel();

            }
        }
    }


    public void registration_post() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.main_url,
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
    Toast.makeText(Complete_info_Activity.this, "next_activity", Toast.LENGTH_LONG).show();

}
                            }


                        } catch (JSONException e) {
                            // Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_SHORT).show();
                            // showToast(coordinatorLayout,s);
                            e.printStackTrace();
                        }
                        pDialog.cancel();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        pDialog.cancel();


                        //Showing toast
                        Toast.makeText(Complete_info_Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();


                params.put(KEY_MODE, "Rx5rOn07H5d64KShGMh+iw==");
                params.put(KEY_FNAME,et_fullname.getText().toString());
                params.put(KEY_EMAIL,et_email.getText().toString());
                params.put(KEY_MOBILE,"91"+et_phone.getText().toString());
                params.put(KEY_USERNAME, sdk_name);
                params.put(KEY_ID, androidId+"wewew");
                params.put(KEY_PASS,password);
                params.put(KEY_BIRTHDAY,sdk_dateofbirth);
                params.put(KEY_GENDER,sdk_gender);
                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Complete_info_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
