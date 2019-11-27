package demo.app.adcharge.eu.sdkdemo.First_Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
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

import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import demo.app.adcharge.eu.sdkdemo.Login_Activity;
import demo.app.adcharge.eu.sdkdemo.R;
import demo.app.adcharge.eu.sdkdemo.Splash_Screen;
import demo.app.adcharge.eu.sdkdemo.Utilites.Constants;

public class SplashScreen_Activity extends Activity {
    private final String TAG = "SplashScreen:";
    private String KEY_MODE ="mode";
    private String KEY_ID= "device_id";
    String androidId = "";
    String tvstatus;
    String idusername;
    boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        super.onCreate(savedInstanceState);

        setLanguage(this, "en");

        SharedPreferences settings = getSharedPreferences("firstTime", MODE_PRIVATE);
        firstTime = settings.getBoolean("first_launch", true); //0 is the default valu

        setupWindowAnimations();
        check_user();

    }

    private void setupWindowAnimations() {
        Explode slide = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slide = new Explode();
            slide.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            getWindow().setExitTransition(slide);
        }
    }

    @SuppressWarnings("deprecation")
    public Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    public void setSystemLocaleLegacy(Configuration config, Locale locale) {
        config.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void setSystemLocale(Configuration config, Locale locale) {
        config.setLocale(locale);
    }

    public void setLanguage(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setSystemLocale(config, locale);
        } else {
            setSystemLocaleLegacy(config, locale);
        }
        context.getApplicationContext().getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    private static final int RP_ACCESS_STORAGE = 2;

    public void cekStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            // cek apakah perlu menampilkan info kenapa membutuhkan access fine location
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                @SuppressLint("InlinedApi") String[] perm = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, perm,
                        RP_ACCESS_STORAGE);
            } else {
                // request permission untuk access fine location
                @SuppressLint("InlinedApi") String[] perm = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, perm,
                        RP_ACCESS_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RP_ACCESS_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.recreate();
                } else {
                    cekStoragePermission();
                }
                return;
        }
    }


    public void check_user() {

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
                                idusername= claims.optString("username");
                                SharedPreferences pref = getSharedPreferences("status", Context.MODE_PRIVATE);

// We need an editor object to make changes
                                SharedPreferences.Editor edit = pref.edit();

// Set/Store data
                                edit.putString("status", tvstatus);
                                edit.putString("idusername", idusername);
// Commit the changes
                                edit.commit();
                               // Toast.makeText(SplashScreen_Activity.this, idusername, Toast.LENGTH_LONG).show();
                                Intent intent ;
                                if(firstTime){
                                    intent = new Intent(SplashScreen_Activity.this, IntroActivity.class);
                                }else{
                                    intent = new Intent(SplashScreen_Activity.this, Login_Activity.class);

                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreen_Activity.this);
                                    startActivity(intent, options.toBundle());
                                } else {
                                    startActivity(intent);
                                }

                                finish();
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
                        Toast.makeText(SplashScreen_Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();



                params.put(KEY_MODE, "3U2npY5JpBGPtVFjAPBWWQ==");
                params.put(KEY_ID, androidId);


                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(SplashScreen_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}




