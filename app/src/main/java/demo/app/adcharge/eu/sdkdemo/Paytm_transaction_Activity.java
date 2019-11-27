package demo.app.adcharge.eu.sdkdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.app.adcharge.eu.sdkdemo.Utilites.Constants;
import demo.app.adcharge.eu.sdkdemo.adapter.Adapter_transaction;
import demo.app.adcharge.eu.sdkdemo.pojo.transaction_list;

import static demo.app.adcharge.eu.sdkdemo.Utilites.Config.showToast;

public class Paytm_transaction_Activity extends AppCompatActivity {
    private String KEY_MODE ="mode";
    private String KEY_ID= "device_id";
    private String KEY_VIEW ="point_views";
    private String KEY_CLICK= "point_click";
    String androidId = "";
    Adapter_transaction adapter_transaction;
    CoordinatorLayout coordinatorLayout;
    SweetAlertDialog pDialog;
    List<transaction_list> mdata;
    private RecyclerView trans_recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        setContentView(R.layout.activity_paytm_transaction_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new SweetAlertDialog(Paytm_transaction_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#25326c"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        coordinatorLayout =(CoordinatorLayout) findViewById(R.id.coordinator);
        trans_recyclerView=(RecyclerView)findViewById(R.id.typeList);
      //  pDialog.cancel();
        transaction_list();
    }
    public void transaction_list() {
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.main_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        // Toast.makeText(Dashboard_Activity.this, s, Toast.LENGTH_LONG).show();

                        try {
                            //converting response to json object


                            JSONArray jsonArray = new JSONArray(s);
                            mdata=new ArrayList<>();
                            for (int n = 0; n < jsonArray.length(); n++) {
                                JSONObject claims = jsonArray.getJSONObject(n);

                                String status= claims.optString("status");
                                JSONArray jArray3 = claims.getJSONArray("listofarray");
                                for(int i = 0; i < jArray3.length(); i++)
                                {
                                    JSONObject object3 = jArray3.getJSONObject(i);
                                    transaction_list  transactionList=new transaction_list();

                                    transactionList.transacrion_date= object3.optString("transacrion_date");
                                    transactionList.transaction_order_number= object3.optString("transaction_order_number");
                                    transactionList.transaction_amount= object3.optString("transaction_amount");
                                    transactionList.transaction_status= object3.optString("transaction_status_name");
                                    mdata.add(transactionList);
                                }

                                adapter_transaction = new Adapter_transaction(Paytm_transaction_Activity.this, mdata);
                                trans_recyclerView.setAdapter(adapter_transaction);
                                trans_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                                pDialog.cancel();
                            }


                        } catch (JSONException e) {
                            Toast.makeText(Paytm_transaction_Activity.this, s, Toast.LENGTH_SHORT).show();
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
                      //  Toast.makeText(Paytm_transaction_Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String, String> params = new Hashtable<String, String>();

                params.put(KEY_MODE, "K33Qc6E+vELaOuGDOFKQqa1shA6neh1o");
                params.put(KEY_ID, androidId);





                return params;
                //returning parameters

            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Paytm_transaction_Activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
