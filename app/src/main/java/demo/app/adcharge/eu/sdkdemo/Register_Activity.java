package demo.app.adcharge.eu.sdkdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.app.adcharge.eu.sdkdemo.Utilites.Config;
import demo.app.adcharge.eu.sdkdemo.Utilites.Constants;
import demo.app.adcharge.eu.sdkdemo.Utilites.DatePicker;
import demo.app.adcharge.eu.sdkdemo.Utilites.SessionHolder;
import demo.app.adcharge.eu.sdkdemo.background.VolleySingleton;
import eu.adcharge.api.ApiException;
import eu.adcharge.api.ApiValidationException;
import eu.adcharge.api.BannersPreloadPolicy;
import eu.adcharge.api.GeoPosition;
import eu.adcharge.api.NoAdvertisementFoundException;
import eu.adcharge.api.entities.AdSession;
import eu.adcharge.api.entities.Gender;
import eu.adcharge.api.entities.User;
import eu.adcharge.sdk.logic.AdCharge;

import static demo.app.adcharge.eu.sdkdemo.Utilites.Config.hideKeyboard;

public class Register_Activity extends AppCompatActivity implements DatePicker.OnDateSelectedListener{
    EditText et_username,et_password,et_fullname,et_email,et_phone,datepicker;
    Button Signup;
    SweetAlertDialog pDialog;
    TextView signin;
    AdCharge adcharge;
    String birthday,sgender;
    User toBeRegistered;
    public static User user;
    String androidId = "";
    private static final AdCharge.Settings SETTINGS = new AdCharge.Settings(); // default settings
    private RadioGroup radioGenderGroup;
    RadioButton radioImButton,radiomaleButton,radioFemaleButton;
    static final Integer LOCATION = 0x1;
    private String KEY_MODE ="mode";
    private String KEY_FNAME ="user_fullname";
    private String KEY_EMAIL = "user_email";
    private String KEY_MOBILE = "user_mobile";
    private String KEY_USERNAME ="user_username";
    private String KEY_ID= "device_id";
    private String KEY_PASS= "user_pass";
    private String KEY_BIRTHDAY= "user_dob";
    private String KEY_GENDER= "user_gender";
    Date date_birthday;
    int age;
    TextInputLayout pass_InputLayout;
    public void showDatePickerDialog() {
        DialogFragment newFragment = DatePicker.newInstance(toBeRegistered.getBirthday(), this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        user=new User();
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        setContentView(R.layout.activity_register_);
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
        getSupportActionBar().hide();


        try
        {
            adcharge = new AdCharge(Constants.URL, Register_Activity.this, SETTINGS);
            toBeRegistered = new User();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Toast.makeText(this, androidId, Toast.LENGTH_SHORT).show();

        signin=(TextView)findViewById(R.id.signin);
        Signup=(Button)findViewById(R.id.signup);
        et_fullname=(EditText)findViewById(R.id.et_fullname);
        et_username=(EditText)findViewById(R.id.et_username);
        et_password=(EditText)findViewById(R.id.ed_pass);
        et_email=(EditText)findViewById(R.id.et_email);
        et_phone=(EditText)findViewById(R.id.field_phone_number);
        datepicker=(EditText)findViewById(R.id.datepicker);
        radioGenderGroup=(RadioGroup)findViewById(R.id.R_im);
        radiomaleButton = (RadioButton) findViewById(R.id.User);
        radioFemaleButton = (RadioButton) findViewById(R.id.Worker);
        pass_InputLayout=(TextInputLayout) findViewById(R.id.password);
        ImageView datepckr=(ImageView)findViewById(R.id.iv_date);
        datepckr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });


        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(Register_Activity.this);
                showDatePickerDialog();

            }
        });


        Signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pDialog = new SweetAlertDialog(Register_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();



                final String fulname = et_fullname.getText().toString().trim();
                final String name = et_username.getText().toString().trim();
                final String pass = et_password.getText().toString().trim();
                final String eml = et_email.getText().toString().trim();
                final String phn = et_phone.getText().toString().trim();
                birthday=datepicker.getText().toString().trim();
                if (TextUtils.isEmpty(fulname)) {
                    pDialog.dismiss();
                    et_fullname.setError("Please enter Full Name");
                    Config.hideKeyboard(Register_Activity.this);
                    Toast.makeText(Register_Activity.this, "Please enter Full Name", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    pDialog.dismiss();
                    et_username.setError("Please enter Username");
                    Config.hideKeyboard(Register_Activity.this);
                    Toast.makeText(Register_Activity.this, "Please enter UserName", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pass)|| pass.length() < 8 || pass.length() > 40) {
                    pDialog.dismiss();
                    et_password.setError("You must have 8 characters in your password");
                    pass_InputLayout.setPasswordVisibilityToggleEnabled(false);
                    Config.hideKeyboard(Register_Activity.this);
                    Toast.makeText(Register_Activity.this, "You must have 8 characters in your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(String.valueOf(date_birthday))) {
                    pDialog.dismiss();

                    Toast.makeText(Register_Activity.this, "Please Select Date of Birth", Toast.LENGTH_SHORT).show();
                    return;
                }

                //int age= Integer.parseInt(Config.calculateAge(date_birthday));
                // Toast.makeText(Register_Activity.this, Config.calculateAge(date_birthday), Toast.LENGTH_SHORT).show();

                if (TextUtils.isEmpty(birthday) || age < 14 ) {
                    pDialog.dismiss();
                    Toast.makeText(Register_Activity.this, "Age: 14 years or above are eligible", Toast.LENGTH_SHORT).show();
                    Config.hideKeyboard(Register_Activity.this);
                    // Age: 14 years or above are eligible
                    //Age is exceeding the limit of 100 years .Please Enter Correct Age
                    return;
                }


                if (TextUtils.isEmpty(eml)) {
                    pDialog.dismiss();
                    et_email.setError("Please enter Email");
                    Config.hideKeyboard(Register_Activity.this);
                    Toast.makeText(Register_Activity.this, "Please enter Email", Toast.LENGTH_SHORT).show();

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
                    Config.hideKeyboard(Register_Activity.this);
                    Toast.makeText(Register_Activity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();

                }


                if (TextUtils.isEmpty(phn)||phn.length() < 10) {
                    pDialog.dismiss();
                    et_phone.setError("Please enter correct Mobile Number");
                    Config.hideKeyboard(Register_Activity.this);
                    Toast.makeText(Register_Activity.this, "Please enter correct Mobile Number", Toast.LENGTH_SHORT).show();

                    return;
                }
                int selectedId = radioGenderGroup.getCheckedRadioButtonId();
                if (selectedId  != -1) {
                    //email is empty
                    radioImButton = (RadioButton) findViewById(selectedId);


                    if( radiomaleButton.isChecked())
                    {

                        sgender="1";
                        // gender1=Gender.MALE;
                        toBeRegistered.setGender(Gender.MALE);
                        user.setGender(Gender.MALE);
                    }
                    else if(radioFemaleButton.isChecked())
                    {
                        // gender1=Gender.FEMALE;
                        sgender="2";
                        toBeRegistered.setGender(Gender.FEMALE);
                        user.setGender(Gender.FEMALE);
                    }
                }
                else
                {
                    pDialog.dismiss();
                    Config.hideKeyboard(Register_Activity.this);
                    Toast.makeText(Register_Activity.this, "Please Select gender", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (birthday.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})")) {
//                    datepicker.setError("Invalide Date Format ,Try again (DD-MM-YYYY)");
//
//                } else {
//                    // Valid date format
//                }

                if(age > 100)
                {
                    pDialog.dismiss();

                    Toast.makeText(Register_Activity.this, "Age is exceeding the limit of 100 years .Please Enter Correct Age", Toast.LENGTH_SHORT).show();
                    // Config.hideKeyboard(Register_Activity.this);
                    //return;
                }else{
                    new RegisterUserTask(name, pass).execute();
                }


                //   Toast.makeText(Register_Activity.this, String.valueOf(toBeRegistered.getGender()), Toast.LENGTH_SHORT).show();
                //registration_post();

            }
        });

        signin.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Register_Activity.this, Login_Activity.class);
                startActivity(intent);
            }
        });

    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Register_Activity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Register_Activity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Register_Activity.this, new String[]{permission}, requestCode);



            } else {

                ActivityCompat.requestPermissions(Register_Activity.this, new String[]{permission}, requestCode);
            }
        } else {

            // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSelected(Date date) {
        datepicker.setText(formatDate(date));
        // birthday=String.valueOf(date);
        date_birthday=date;
        toBeRegistered.setBirthday(date);
        user.setBirthday(date);

        age= Integer.parseInt(Config.calculateAge(date));
    }
    public static String formatDate(Date date) {
        if (date == null) {
            return "undefined";
        }
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    private class RegisterUserTask extends AsyncTask<String, Void, Void> {
        private String username;
        private String password;
        private Gender gender;
        private Date date;
        private ApiValidationException validationException;


        public RegisterUserTask(String username, String password) {
            this.username = username;
            this.password = password;

            this.date = date;
        }

        @Override
        protected Void doInBackground(String... strings) {

            toBeRegistered.setUsername(username);
            toBeRegistered.setPassword(password);
            toBeRegistered.setBirthday(toBeRegistered.getBirthday());
            toBeRegistered.setGender(toBeRegistered.getGender());


            try {
                adcharge.registerSubscriberUser(toBeRegistered, Constants.TRAFFIC_SOURCE_KEY);

            } catch (IOException e) {
                pDialog.cancel();
                //  Toast.makeText(Register_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                pDialog.cancel();
                //  Toast.makeText(Register_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            } catch (ApiValidationException e) {
                validationException = e;//to be processed in ui thread
                //  Toast.makeText(Register_Activity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(Register_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    } else if ("password".equals(field)) {
                        Toast.makeText(Register_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    } else {
                        Toast.makeText(Register_Activity.this,message, Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }
                }
            } else {
                Intent loginActivity = new Intent(getApplicationContext(), Verify_Activity.class);
                loginActivity.putExtra("fullname",et_fullname.getText().toString());
                loginActivity.putExtra("username",et_username.getText().toString());
                loginActivity.putExtra("password",et_password.getText().toString());
                loginActivity.putExtra("email",et_email.getText().toString());
                loginActivity.putExtra("phonenumber",et_phone.getText().toString());
                loginActivity.putExtra("birthday",birthday);
                loginActivity.putExtra("gender",sgender);
                startActivity(loginActivity);
                finish();
                //new UserSaveTask().execute();

            }
        }
    }

    private class UserSaveTask extends AsyncTask<String, Void, Void> {
        private ApiValidationException validationException;

        @Override
        protected Void doInBackground(String... strings) {
            // binding.setTasksInProgress(binding.getTasksInProgress() + 1);
            try {
                adcharge.getApiWrapper().saveUser(user);
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
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                Intent loginActivity = new Intent(getApplicationContext(), Verify_Activity.class);
                loginActivity.putExtra("fullname",et_fullname.getText().toString());
                loginActivity.putExtra("username",et_username.getText().toString());
                loginActivity.putExtra("password",et_password.getText().toString());
                loginActivity.putExtra("email",et_email.getText().toString());
                loginActivity.putExtra("phonenumber",et_phone.getText().toString());
                loginActivity.putExtra("birthday",birthday);
                loginActivity.putExtra("gender",sgender);
                loginActivity.putExtra("birthday2",user.getBirthday());
                loginActivity.putExtra("gender2",user.getBirthday());
                startActivity(loginActivity);
                finish();

                pDialog.cancel();
            }
            //  binding.setTasksInProgress(binding.getTasksInProgress() - 1);
        }
    }


}
