package demo.app.adcharge.eu.sdkdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
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
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.app.adcharge.eu.sdkdemo.Utilites.Constants;
import demo.app.adcharge.eu.sdkdemo.background.SMSGatewayCenter;

public class Splash_Screen extends Activity {
    private String KEY_MODE ="mode";
    private String KEY_ID= "device_id";
    String androidId = "";
    String tvstatus;
    String idusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
       // Toast.makeText(this, androidId, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_splash__screen);


         check_user();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Thread thread=new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    sleep(3*1000);
//                    Intent intent=new Intent(Splash_Screen.this, Login_Activity.class);
//                    startActivity(intent);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        thread.start();
//    }

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
                             //   Toast.makeText(Splash_Screen.this, idusername, Toast.LENGTH_LONG).show();
                                Thread thread=new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        try {
                                            sleep(3*1000);
                                            Intent intent=new Intent(Splash_Screen.this, Login_Activity.class);
                                            startActivity(intent);
                                            //Toast.makeText(Splash_Screen.this, idusername, Toast.LENGTH_LONG).show();
                                            finish();

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                thread.start();
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
                        Toast.makeText(Splash_Screen.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Splash_Screen.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
