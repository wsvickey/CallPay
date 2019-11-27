package demo.app.adcharge.eu.sdkdemo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import demo.app.adcharge.eu.sdkdemo.R;
import demo.app.adcharge.eu.sdkdemo.Utilites.Constants;
import demo.app.adcharge.eu.sdkdemo.pojo.transaction_list;

public class Adapter_transaction extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<transaction_list> data= Collections.emptyList();


    // create constructor to innitilize context and data sent from MainActivity
    public Adapter_transaction(Context context, List<transaction_list> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;

    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.cardview_transaction_item, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        final transaction_list current=data.get(position);

        myHolder.tv_date.setText(current.getTransacrion_date());
        myHolder.tv_amount.setText(current.getTransaction_amount());
        myHolder.tv_status.setText(current.getTransaction_status());

//        String url=Constants.IMAGES + current.getImage();
//        Glide.with(context).load( url)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(R.mipmap.ic_launcher)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        myHolder.progressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        myHolder.progressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
//                .into(myHolder.imgCategory);

        myHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                }


        });


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setmfilter(List<transaction_list> list)
    {
        data=new ArrayList<>();
        data.addAll(list);
       // notifyDataSetChanged();
    }
    class MyHolder extends RecyclerView.ViewHolder{


        private String KEY_UID ="c_id";
        CardView card_view;
        TextView tv_date ,tv_amount,tv_status;

        int num;
        // create constructor to get widget reference
        public MyHolder(final View view) {
            super(view);

            tv_date = (TextView) view.findViewById(R.id.txtdate);
            tv_amount= (TextView) view.findViewById(R.id.txtamount);
            tv_status= (TextView) view.findViewById(R.id.txtstatus);

            card_view = (CardView) view.findViewById(R.id.cardView);




        }

        }
public void delete(final Context context, final String id, final int i)
{

//    bt_delete.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//             String deleteCart_URL = Constants.CART_DELETE +id;
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteCart_URL,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            //Disimissing the progress dialog
//                            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
//                           data.remove(i);
//                           notifyDataSetChanged();
//                          //  Cart_Activity.checkcart_item();
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            //Dismissing the progress dialog
//                            // loading.dismiss();
//
//                            Toast.makeText(context, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
//                        }
//                    })
//            {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    //Converting Bitmap to String
//
//
//                    Map<String, String> params = new Hashtable<String, String>();
//
//
//                    params.put(KEY_UID, id);
//
//
//                    return params;
//
//                }
//            };
//
//            //Creating a Request Queue
//            RequestQueue requestQueue = Volley.newRequestQueue(context);
//
//            //Adding request to the queue
//            requestQueue.add(stringRequest);
//        }
//    });


}


}