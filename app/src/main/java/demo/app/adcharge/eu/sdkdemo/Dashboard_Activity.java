package demo.app.adcharge.eu.sdkdemo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.app.adcharge.eu.sdkdemo.Utilites.Constants;
import demo.app.adcharge.eu.sdkdemo.Utilites.SessionHolder;
import eu.adcharge.api.ApiException;
import eu.adcharge.api.ApiValidationException;
import eu.adcharge.api.BannersPreloadPolicy;
import eu.adcharge.api.GeoPosition;
import eu.adcharge.api.NoAdvertisementFoundException;
import eu.adcharge.api.entities.AdSession;
import eu.adcharge.api.entities.Statistics;
import eu.adcharge.api.entities.User;
import eu.adcharge.sdk.logic.AdCharge;

import static demo.app.adcharge.eu.sdkdemo.Utilites.Config.cekKoneksi;
import static demo.app.adcharge.eu.sdkdemo.Utilites.Config.showToast;
import static demo.app.adcharge.eu.sdkdemo.Utilites.Constants.URL;
import static demo.app.adcharge.eu.sdkdemo.Utilites.Constants.main_url;

public class Dashboard_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
   // URL
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

     User user;
     SweetAlertDialog pDialog,warDailog ;
    TextView tv_points;
    AdCharge adcharge;
   // Date currentTime;
    Date startdate,enddate;
    Statistics statistics;
    Button bt_category;
    NavigationView navigationView;
    ImageButton redeam_50,redeam_100;
    Double view,click,bonuse,impression,balance;
    CoordinatorLayout coordinatorLayout;
    private String KEY_MODE ="mode";
    private String KEY_ID= "device_id";
    private String KEY_VIEW ="point_views";
    private String KEY_CLICK= "point_click";
    private String KEY_IMPRESSION ="point_impression";
    private String KEY_BALANCE= "point_balance";
    private String KEY_BONUSE= "point_bonus";
    private String KEY_AMOUNT= "post_amount";
    private String KEY_POINTS= "callpay_points";

    Double rupees;

    String androidId = "";
    String min_limit="50",max_limit="100",remaining_point,total_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_dashboard_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        pDialog = new SweetAlertDialog(Dashboard_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#25326c"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

// We need an editor object to make changes
        SharedPreferences.Editor edit = pref.edit();

// Set/Store data
        edit.putString("logout", "false");

// Commit the changes
        edit.commit();
        tv_points=(TextView)findViewById(R.id.tv_balance);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        coordinatorLayout =(CoordinatorLayout) findViewById(R.id.coordinator);
        if (cekKoneksi(this)) {
            try {
                adcharge = new AdCharge(URL, Dashboard_Activity.this, SETTINGS);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            pDialog = new SweetAlertDialog(Dashboard_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#25326c"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            trans_limit_post();
            new StaisticTask().execute();

//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    // this code will be executed after 2 seconds
//                    new StaisticTask().execute();
//                }
//            }, 30000);
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
        bt_category=(Button) findViewById(R.id.manage);
        navigationView.setNavigationItemSelectedListener(this);
        redeam_50=(ImageButton)findViewById(R.id.redeam_50);
        redeam_100=(ImageButton)findViewById(R.id.redeam_100);
        redeam_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if (cekKoneksi(Dashboard_Activity.this)) {
                    if(rupees>=Integer.valueOf(min_limit))
                    {
                    warDailog = new SweetAlertDialog(Dashboard_Activity.this, SweetAlertDialog.WARNING_TYPE);
                    warDailog.getProgressHelper().setBarColor(Color.parseColor("#3AB9eB"));
                    warDailog.setContentText("Do you want to redeem 50 Rupees ?");
                    warDailog.setConfirmText("No");
                    warDailog.setCancelText("Yes");
                    warDailog.setCancelable(true);

                    warDailog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {


                            sDialog.cancel();

                        }
                    });
                    warDailog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            pDialog = new SweetAlertDialog(Dashboard_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#25326c"));
                            pDialog.setTitleText("Loading");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            payment_process_post(min_limit, remaining_point);


                        }
                    });

                    warDailog.show();

                    Button btn = (Button) warDailog.findViewById(R.id.confirm_button);
                    btn.setBackgroundColor(ContextCompat.getColor(Dashboard_Activity.this, R.color.colorPrimaryDark));

                }else {
                        //  Toast.makeText(context,"Check your Internet Connection",Toast.LENGTH_LONG).show();
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Request amount is less than minimum redeem limit", Snackbar.LENGTH_LONG);
                        snackbar.show();
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
            }
        });
        redeam_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cekKoneksi(Dashboard_Activity.this)) {



                    if(rupees>=Integer.valueOf(max_limit))
                    {
                           warDailog = new SweetAlertDialog(Dashboard_Activity.this, SweetAlertDialog.WARNING_TYPE);
                        warDailog.getProgressHelper().setBarColor(Color.parseColor("#3AB9eB"));
                        warDailog.setContentText("Do you want to redeem 100 Rupees ?");
                        warDailog.setConfirmText("No");
                        warDailog.setCancelText("Yes");
                        warDailog.setCancelable(true);

                        warDailog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {


                                sDialog.cancel();

                            }
                        });
                        warDailog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                pDialog = new SweetAlertDialog(Dashboard_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#25326c"));
                                pDialog.setTitleText("Loading");
                                pDialog.setCancelable(false);
                                pDialog.show();
                                payment_process_post(max_limit,remaining_point);


                            }
                        });

                        warDailog.show();

                        Button btn = (Button) warDailog.findViewById(R.id.confirm_button);
                        btn.setBackgroundColor(ContextCompat.getColor(Dashboard_Activity.this,R.color.colorPrimaryDark));

                    }else {
                        //  Toast.makeText(context,"Check your Internet Connection",Toast.LENGTH_LONG).show();
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Request amount is less than minimum redeem limit", Snackbar.LENGTH_LONG);
                        snackbar.show();
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
            }
        });
        bt_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_Activity.this, Category_Activity.class);
                startActivity(intent);
                finish();

              //  new AdSessionGetTask().execute();
            }
        });



        navpic();
    }


    Button bt_profile;
    TextView tv_name;
    private void navpic(){

        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_app_);
        bt_profile = (Button)hView.findViewById(R.id.profile);
        tv_name = (TextView)hView.findViewById(R.id.name);

        bt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard_Activity.this, My_Profile_Activity.class);
                startActivity(intent);

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

                tv_name.setText(user.getUsername());




            }
        }
}


    private class StaisticTask extends AsyncTask<String, Void, Statistics> {

        @Override
        protected Statistics doInBackground(String... strings) {

            try {

                statistics = new Statistics();
                //statistics=adCharge.getStatistics(startdate,enddate);
                String dateStr = "2019-05-19";
                String endStr = "2019-20-19";

                Date now = new Date();
                Date past24hours = new Date(now.getTime() - 24 * 60 * 60 * 1000);
                SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    startdate = curFormater.parse(dateStr);
                    enddate = curFormater.parse(endStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                statistics = adcharge.getApiWrapper().getStatistics(past24hours, now);
                //  id.setText(String.valueOf(statistics));
                //  Toast.makeText(MainActivity.this,String.valueOf(statistics), Toast.LENGTH_SHORT).show();


            } catch (IOException e) {

            } catch (ApiException e) {

            } catch (ApiValidationException e) {
            }
            return statistics;
        }

        @Override
        protected void onPostExecute(Statistics result) {
            //  Toast.makeText(Dashboard_ctivity.this, String.valueOf(result.getClicks()), Toast.LENGTH_SHORT).show();
            //tv_click.setText(String.valueOf(result.getClicks()));
            pDialog.cancel();

          //  point=Double.valueOf(result.getBalance());

            points_post(String.valueOf(result.getClicks()),String.valueOf(result.getViews()),String.valueOf(result.getBalance()),String.valueOf(result.getActions()),String.valueOf(result.getBonuses()));
           // Toast.makeText(Dashboard_Activity.this, String.valueOf(result.getBonuses()), Toast.LENGTH_SHORT).show();

            super.onPostExecute(result);


        }
    }
    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard_Activity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transaction_, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_trans) {
//            Intent profileActivity = new Intent(getApplicationContext(), Dashboard_Activity.class);
////            profileActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            startActivity(profileActivity);
////            finish();

            pDialog = new SweetAlertDialog(Dashboard_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#25326c"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            new StaisticTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_wallet) {
//            // Handle the camera action
//        } else
            if (id == R.id.nav_transaction) {
            Intent profileActivity = new Intent(getApplicationContext(), Paytm_transaction_Activity.class);
            profileActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profileActivity);
        }
            else if (id == R.id.nav_logout) {
            SweetAlertDialog   warDailog = new SweetAlertDialog(Dashboard_Activity.this, SweetAlertDialog.WARNING_TYPE);
            warDailog.getProgressHelper().setBarColor(Color.parseColor("#3AB9eB"));
            warDailog.setTitleText("Select Logout Option");
            warDailog.setContentText("Show me Ads After Logout!");
            warDailog.setConfirmText("No");
            warDailog.setCancelText("Yes");
            warDailog.setCancelable(true);

            warDailog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {

                    adcharge.logout();
                    SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
// We need an editor object to make changes
                    SharedPreferences.Editor edit = pref.edit();
// Set/Store data
                    edit.putString("logout", "true");
// Commit the changes
                    edit.commit();
                    Intent profileActivity = new Intent(getApplicationContext(), Splash_Screen.class);
                    profileActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(profileActivity);
                    finish();
                    sDialog.dismissWithAnimation();
                }
            });
            warDailog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {

                    SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
// We need an editor object to make changes
                    SharedPreferences.Editor edit = pref.edit();
// Set/Store data
                    edit.putString("logout", "true");
// Commit the changes
                    edit.commit();
                    Intent profileActivity = new Intent(getApplicationContext(), Splash_Screen.class);
                    profileActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(profileActivity);
                    finish();
                    sDialog.dismissWithAnimation();
                }
            });

            warDailog.show();

            Button btn = (Button) warDailog.findViewById(R.id.confirm_button);
            btn.setBackgroundColor(ContextCompat.getColor(Dashboard_Activity.this,R.color.colorPrimaryDark));

            }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    private class AdSessionGetTask extends AsyncTask<String, Void, AdSession>

    {
        private NoAdvertisementFoundException noAdvertisementFoundException;

        @Override
        protected AdSession doInBackground(String... strings) {
            try {
                return adcharge.getApiWrapper().getAdvert(new GeoPosition(33.5473873, 73.1231582), BannersPreloadPolicy.BOTH_BANNERS_PRELOAD);
            } catch (IOException e) {
                //binding.setError(e.getMessage());
            } catch (ApiException e) {
                // binding.setError(e.getMessage());
            } catch (ApiValidationException e) {
                //  showToast(view,e.getMessage());
            } catch (NoAdvertisementFoundException e) {
                noAdvertisementFoundException = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(AdSession session) {
            super.onPostExecute(session);

            if (noAdvertisementFoundException != null) {
                //   binding.setError(noAdvertisementFoundException.getMessage());
                Toast.makeText(getApplicationContext(), noAdvertisementFoundException.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (session != null) {
                Toast.makeText(getApplicationContext(), "Session is preloaded", Toast.LENGTH_SHORT).show();
                SessionHolder.save(session);
//                Intent incomingCallActivity = new Intent(getApplicationContext(), IncomingCallActivity.class);
//                incomingCallActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                incomingCallActivity.putExtra("session", session.getSession_id());
//                startActivity(incomingCallActivity);
            }
            // binding.setTasksInProgress(binding.getTasksInProgress() - 1);
        }

    }

    public void trans_limit_post() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.main_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                       // Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_LONG).show();

                        try {
                            //converting response to json object

                            JSONArray jsonArray = new JSONArray(s);
                            for (int n = 0; n < jsonArray.length(); n++) {
                                JSONObject claims = jsonArray.getJSONObject(n);

                                min_limit= claims.optString("min_limit");
                                max_limit= claims.optString("max_limit");
                             //   Toast.makeText(Dashboard_Activity.this, min_limit +max_limit , Toast.LENGTH_SHORT).show();


                            }


                        } catch (JSONException e) {
                            Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Dashboard_Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();

                params.put(KEY_MODE, "03+ujAdEGJrckZcvwWAB4ZdM28yfVXwd");
                params.put(KEY_ID, androidId);



                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void points_post(final String pclick, final String pview, final String pbalance, final String pimpression, final String pbonuse) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.main_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        // Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_LONG).show();

                        try {
                            //converting response to json object

                            JSONArray jsonArray = new JSONArray(s);
                            for (int n = 0; n < jsonArray.length(); n++) {
                                JSONObject claims = jsonArray.getJSONObject(n);


                                remaining_point= claims.optString("remaining_point");
                                total_amount= claims.optString("total_amount");

                                   //Toast.makeText(Dashboard_Activity.this, remaining_point, Toast.LENGTH_SHORT).show();
                                 rupees=Double.valueOf(total_amount);
                                tv_points.setText(total_amount);
                                if(rupees>=Integer.valueOf(min_limit))
                                {
                                    redeam_50.setBackgroundResource(R.drawable.redeam_50);
                                }

                              if(rupees >= Integer.valueOf(max_limit))
                                {
                                    redeam_50.setBackgroundResource(R.drawable.redeam_50);
                                    redeam_100.setBackgroundResource(R.drawable.redeam_100);
                                }

                            }


                        } catch (JSONException e) {
                            Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(Dashboard_Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();

                params.put(KEY_MODE, "jTXzUcgL0GC1tOAQV6iG0M9Hj9eiIOM0");
                params.put(KEY_ID, androidId);
                params.put(KEY_CLICK, pclick);
                params.put(KEY_BALANCE, pbalance);
                params.put(KEY_IMPRESSION, pimpression);
                params.put(KEY_VIEW, pview);
                params.put(KEY_BONUSE, pbonuse);



                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void payment_process_post(final String amount, final String remaining) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.main_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        // Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_LONG).show();

                        try {
                            //converting response to json object
                            warDailog.show();
                            JSONArray jsonArray = new JSONArray(s);
                            for (int n = 0; n < jsonArray.length(); n++) {
                                JSONObject claims = jsonArray.getJSONObject(n);

                               String status= claims.optString("status");
                                String msg= claims.optString("msg");
                                total_amount= claims.optString("total_amount");

                                //Toast.makeText(Dashboard_Activity.this, remaining_point, Toast.LENGTH_SHORT).show();
                                tv_points.setText(total_amount);
                               // remaining_point= claims.optString("remaining_point");
                             //   Toast.makeText(Dashboard_Activity.this, status+msg, Toast.LENGTH_SHORT).show();
                               // Double rupees=Double.valueOf(remaining_point)/100;
                              //tv_points.setText(String.valueOf(rupees));

                                showToast(coordinatorLayout,msg);
                                pDialog.cancel();
                                warDailog.cancel();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();

                params.put(KEY_MODE, "mVuWPfBiAQMlYjetHt7SEPz5jWaQeJtBmXah10ybUpw=");
                params.put(KEY_ID, androidId);
                params.put(KEY_AMOUNT, amount);
                params.put(KEY_POINTS, remaining);




                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
