package demo.app.adcharge.eu.sdkdemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.app.adcharge.eu.sdkdemo.Utilites.Config;
import demo.app.adcharge.eu.sdkdemo.Utilites.Constants;
import demo.app.adcharge.eu.sdkdemo.Utilites.DatePicker;
import demo.app.adcharge.eu.sdkdemo.Utilites.DatePickerFragment;

import eu.adcharge.api.ApiException;
import eu.adcharge.api.ApiValidationException;
import eu.adcharge.api.entities.Gender;
import eu.adcharge.api.entities.User;
import eu.adcharge.sdk.logic.AdCharge;

import static demo.app.adcharge.eu.sdkdemo.Utilites.Config.cekKoneksi;
import static demo.app.adcharge.eu.sdkdemo.Utilites.Config.showToast;
import static demo.app.adcharge.eu.sdkdemo.Utilites.Constants.URL;

public class My_Profile_Activity extends AppCompatActivity  implements DatePicker.OnDateSelectedListener {
    EditText edname,edpssword,edgender;
    private RadioGroup radioGenderGroup;
    RadioButton radioImButton,radiomaleButton,radioFemaleButton;
    EditText datepicker;
    Button registration;
    String username,password,gender,birthday;
    private AdCharge sdk;
    User user;
    private String KEY_MODE ="mode";
    private String KEY_ID= "device_id";
    private String KEY_PASS ="user_pass";
    Date date;
    int age;
    int i= 0;
    String androidId = "";
    SimpleDateFormat format;
    SweetAlertDialog pDialog,warDailog;
    private static final AdCharge.Settings SETTINGS = new AdCharge.Settings(); // default settings
    CoordinatorLayout coordinatorLayout;
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePicker.newInstance(user.getBirthday(), this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
        i=1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__profile_);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        user=new User();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout =(CoordinatorLayout) findViewById(R.id.coordinator);
        pDialog = new SweetAlertDialog(My_Profile_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#25326c"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        if (cekKoneksi(this)) {
            try {
                sdk = new eu.adcharge.sdk.logic.AdCharge(URL, My_Profile_Activity.this, SETTINGS);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            pDialog.show();
            format = new SimpleDateFormat("yyyy-MM-dd");

           new UserGetTask().execute();
        }

        else

        {
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

        radioGenderGroup=(RadioGroup)findViewById(R.id.R_im);
        registration=(Button)findViewById(R.id.save);
        datepicker=(EditText)findViewById(R.id.datepicker);
        edname=(EditText)findViewById(R.id.et_username) ;
        edpssword=(EditText)findViewById(R.id.et_cpassword) ;
        edgender=(EditText)findViewById(R.id.et_cgender) ;
        radiomaleButton = (RadioButton) findViewById(R.id.User);
        radioFemaleButton = (RadioButton) findViewById(R.id.Worker);

        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        radioGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                if(radioButton.getText().equals("Female")){
                     user.setGender(Gender.FEMALE);
                    edgender.setText("Female");
                }else if (radioButton.getText().equals("Male")){
                    user.setGender(Gender.MALE);
                    edgender.setText("Male");
                }
            }
        });
        Date  d;
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        username=edname.getText().toString();
                        password=edpssword.getText().toString();
                        birthday=datepicker.getText().toString();

if(i==1){
    if (TextUtils.isEmpty(birthday) || age < 14 ) {
        pDialog.dismiss();
        Toast.makeText(My_Profile_Activity.this, "Age: 14 years or above are eligible", Toast.LENGTH_SHORT).show();
        Config.hideKeyboard(My_Profile_Activity.this);
        // Age: 14 years or above are eligible
        //Age is exceeding the limit of 100 years .Please Enter Correct Age
        return;
    }

    if (TextUtils.isEmpty(birthday) || age > 100 ) {
        pDialog.dismiss();
        Toast.makeText(My_Profile_Activity.this, "Age is exceeding the limit of 100 years .Please Enter Correct Age", Toast.LENGTH_SHORT).show();
        Config.hideKeyboard(My_Profile_Activity.this);
        // Age: 14 years or above are eligible
        //Age is exceeding the limit of 100 years .Please Enter Correct Age
        return;
    }
}


//                        if (TextUtils.isEmpty(gender)) {
//                            //email is empty
//                            warDailog.dismiss();
//                            edgender.setError("Please pick gender");
//                            //  Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
//                            return;
//                          }

         if (password.matches(""))
                {
                      } else
                {

                    user.setPassword(password);
                }

                //  Toast.makeText(My_Profile_Activity.this, user.getBirthday().toString() , Toast.LENGTH_SHORT).show();

                       // pDialog.show();

 warDailog = new SweetAlertDialog(My_Profile_Activity.this, SweetAlertDialog.WARNING_TYPE);
                warDailog.getProgressHelper().setBarColor(Color.parseColor("#3AB9eB"));
                warDailog.setTitleText("Are you sure?");
                warDailog.setContentText("You won't be able to recover previous info");
                warDailog.setConfirmText("Yes, save it!");
                warDailog.setCancelable(true);
                warDailog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                            new UserSaveTask().execute();


                        sDialog.dismissWithAnimation();
                    }
                });

                warDailog.show();

                Button btn = (Button) warDailog.findViewById(R.id.confirm_button);
                btn.setBackgroundColor(ContextCompat.getColor(My_Profile_Activity.this,R.color.colorPrimaryDark));


            }
        });

    }

    private class UserGetTask extends AsyncTask<String, Void, User> {
        private ApiValidationException validationException;

        @Override
        protected User doInBackground(String... strings) {
            try {
                //   binding.setTasksInProgress(binding.getTasksInProgress() + 1);
                return sdk.getApiWrapper().getUserInfo();
            } catch (IOException e) {
                //  binding.setError(e.getMessage());
                // showToast()
            } catch (ApiException e) {
                //  binding.setError(e.getMessage());
            } catch (ApiValidationException e) {
                validationException = e;//to be processed in ui thread
            }
            return null;
        }

        @Override
        protected void onPostExecute(User usr) {
            pDialog.cancel();
            if (validationException != null) {
                for (ApiValidationException.Error fieldError : validationException.getFieldErrors()) {
                    String field = fieldError.getField();
                    String message = fieldError.getMessages().isEmpty() ? "" : fieldError.getMessages().get(0);
                    //  binding.setError(field + ": " + message);
                }
            } else {
                user=usr;
                username=user.getUsername();
                edname.setText(username);
                edpssword.setText(user.getPassword());
                datepicker.setText(formatDate(user.getBirthday()));
                if(user.getGender()== Gender.MALE)
                {
                    radiomaleButton.setChecked(true);
                    edgender.setText("Male");
                }else
                {
                    edgender.setText("Female");
                    radioFemaleButton.setChecked(true);
                }
            }
            // binding.setTasksInProgress(binding.getTasksInProgress() - 1);
        }
    }

    @Override
    public void onDateSelected(Date date) {
        //  Toast.makeText(Profile_Activity.this, String.valueOf(date), Toast.LENGTH_SHORT).show();

        datepicker.setText(formatDate(date));
        user.setBirthday(date);
        age= Integer.parseInt(Config.calculateAge(date));
    }


    public static String formatDate(Date date) {
        if (date == null) {
            return "undefined";
        }
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }


    private class UserSaveTask extends AsyncTask<String, Void, Void> {
        private ApiValidationException validationException;

        @Override
        protected Void doInBackground(String... strings) {
            // binding.setTasksInProgress(binding.getTasksInProgress() + 1);
            try {
                sdk.getApiWrapper().saveUser(user);
                //sdk.getStatistics()
            } catch (IOException e) {
                // binding.setError(e.getMessage());
            } catch (ApiException e) {
                // binding.setError(e.getMessage());
            } catch (ApiValidationException e) {
                validationException = e;//to be processed in ui thread
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
                    // binding.setError(field + ": " + message);

                }
            } else {

                if (password.matches(""))
                {
                } else
                {

                    ChangePass_Api(password);
                }

            }
            //  binding.setTasksInProgress(binding.getTasksInProgress() - 1);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    public void ChangePass_Api(final String pass) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.main_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        // Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                        try {
                            //converting response to json object

                            JSONArray jsonArray = new JSONArray(s);
                            for (int n = 0; n < jsonArray.length(); n++) {
                                JSONObject claims = jsonArray.getJSONObject(n);

                                String status= claims.optString("status");
                                pDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            // Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_SHORT).show();
                            showToast(coordinatorLayout,s);
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
                        showToast(coordinatorLayout,volleyError.getMessage());

                        //Showing toast
                        // Toast.makeText(Dashboard_Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();

                params.put(KEY_MODE, "ifEP7G7KJq2s6qkG9mxAEZ2XVVqIzOBB");
                params.put(KEY_ID, androidId);
                params.put(KEY_PASS, pass);




                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(My_Profile_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
