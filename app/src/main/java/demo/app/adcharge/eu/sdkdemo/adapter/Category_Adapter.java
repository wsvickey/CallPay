package demo.app.adcharge.eu.sdkdemo.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;



import java.util.ArrayList;
import java.util.Locale;

import demo.app.adcharge.eu.sdkdemo.R;
import demo.app.adcharge.eu.sdkdemo.Utilites.Constants;
import demo.app.adcharge.eu.sdkdemo.pojo.category;


public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.MyViewHolder> {

    private LayoutInflater inflater;
    public static ArrayList<category> imageModelArrayList;
    private Context ctx;

    public Category_Adapter(Context ctx, ArrayList<category> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public Category_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cardview_category_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final Category_Adapter.MyViewHolder holder, int position) {




        holder.checkBox.setText(String.valueOf(imageModelArrayList.get(position).getAnimal()));
        holder.checkBox.setChecked(imageModelArrayList.get(position).getSelected());
        //holder.tvAnimal.setText(imageModelArrayList.get(position).getAnimal());

        // holder.checkBox.setTag(R.integer.btnplusview, convertView);
        String imgurl=Constants.IMAGES+String.valueOf(imageModelArrayList.get(position).getAnimal()).toLowerCase()+".png";
        Glide.with(ctx).load(imgurl)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.img);



        holder.checkBox.setTag(position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.checkBox.getTag();
             //   Toast.makeText(ctx, imageModelArrayList.get(pos).getAnimal() + " clicked!", Toast.LENGTH_SHORT).show();

                if (imageModelArrayList.get(pos).getSelected()) {
                    imageModelArrayList.get(pos).setSelected(false);
                  //  Toast.makeText(ctx, "unselected", Toast.LENGTH_SHORT).show();
                } else {
                    imageModelArrayList.get(pos).setSelected(true);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        protected CheckBox checkBox;
        private ImageView img;
      ProgressBar progressBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            progressBar= (ProgressBar) itemView.findViewById(R.id.progressBar);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb);
            img = (ImageView) itemView.findViewById(R.id.itemImage);
        }

    }
}