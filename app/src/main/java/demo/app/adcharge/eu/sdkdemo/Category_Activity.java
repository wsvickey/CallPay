package demo.app.adcharge.eu.sdkdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.app.adcharge.eu.sdkdemo.adapter.Category_Adapter;
import demo.app.adcharge.eu.sdkdemo.pojo.category;
import eu.adcharge.api.ApiException;
import eu.adcharge.api.ApiValidationException;
import eu.adcharge.api.entities.Interest;
import eu.adcharge.api.entities.User;
import eu.adcharge.sdk.logic.AdCharge;

import static demo.app.adcharge.eu.sdkdemo.Utilites.Constants.URL;

public class Category_Activity extends AppCompatActivity
{
    AdCharge adcharge;
    private static final AdCharge.Settings SETTINGS = new AdCharge.Settings(); // default settings


    SharedPreferences sharedpreferences;
    private ArrayList<category> modelArrayList;
    private Category_Adapter customAdapter;
    private Button btnselect, btndeselect, btnnext;
    List<Interest> Mylist ;
    List<Interest> Userlist ;
    Button Movedash;
    TextView aa;
    User user,user_remove;

    SweetAlertDialog pDialog;
    private RecyclerView product_recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        aa=(TextView)findViewById(R.id.aaa);
        Movedash=(Button)findViewById(R.id.move);
        user=new User();
        user_remove=new User();
        try {
            adcharge = new AdCharge(URL, Category_Activity.this, SETTINGS);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // new GetInterest().execute();

        Mylist = new ArrayList<Interest>();
        Userlist = new ArrayList<Interest>();
        product_recyclerView=(RecyclerView)findViewById(R.id.productList);
        new UserInterest().execute();


        Movedash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new SweetAlertDialog(Category_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#25326c"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
                for (int i = 0; i < Category_Adapter.imageModelArrayList.size(); i++){
                    //     aa.setText(aa.getText() + " " + Category_Adapter.modelArrayList.get(i).getAnimal());
                    // user_remove.getInterests().add(Category_Adapter.imageModelArrayList.get(i).getAnimal());
                    user.getInterests().remove(Category_Adapter.imageModelArrayList.get(i).getAnimal());


                }
                new UserRemoveTask().execute();

            }
        });




    }
    private ArrayList<category> getModel(boolean isSelect){
        ArrayList<category> list = new ArrayList<>();


        for (Interest interest : Mylist) {
            boolean found = false;
            category model = new category();

            for (Interest myintrest : Userlist) {
                if (interest == myintrest) {
                    found = true;

                    model.setSelected(found);
                    model.setAnimal(interest);
                    list.add(model);
                }

            }
            if (!found) {
                model.setSelected(found);
                model.setAnimal(interest);
                list.add(model);
            }


        }
        return list;
    }

    List<Interest> availableInterests;

    private class GetInterest extends AsyncTask<String, Void, List<Interest> > {

        @Override
        protected List<Interest> doInBackground(String... strings) {

            try {
                user=adcharge.getApiWrapper().getUserInfo();
                availableInterests = adcharge.getApiWrapper().getAvailableInterests();
//                for(Interest interest : availableInterests){
//                    Log.d("waqas",String.valueOf(interest));
//                    user.getInterests().add(interest);
//
//                }
            } catch (IOException e) {

            } catch (ApiException e) {

            } catch (ApiValidationException e) {
            }
            return availableInterests ;
        }

        @Override
        protected void onPostExecute(List<Interest> result) {
            //  Toast.makeText(Dashboard_ctivity.this, String.valueOf(result.getClicks()), Toast.LENGTH_SHORT).show();
            StringBuilder builder = new StringBuilder();
            for(Interest interest : result){
                //Log.d("waqas",String.valueOf(interest));

                Mylist.add(interest);

            }
            modelArrayList = getModel(false);

            //

            customAdapter = new Category_Adapter(Category_Activity.this,modelArrayList);
            product_recyclerView.setAdapter(customAdapter);
            product_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            int numberOfColumns = 2;
            product_recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), numberOfColumns));
            int spacing = 40; // 50px
            boolean includeEdge = false;
            product_recyclerView.addItemDecoration(new GridSpacingItemDecoration(numberOfColumns, spacing, includeEdge));



            super.onPostExecute(result);

        }
    }

    List<Interest> user_Interests;
    private class UserInterest extends AsyncTask<String, Void, List<Interest> > {

        @Override
        protected List<Interest> doInBackground(String... strings) {

            try {

                user_Interests = adcharge.getApiWrapper().getUserInfo().getInterests();
//                for(Interest interest : availableInterests){
//                    Log.d("waqas",String.valueOf(interest));
//                    user.getInterests().add(interest);
//
//                }
            } catch (IOException e) {

            } catch (ApiException e) {

            } catch (ApiValidationException e) {
            }
            return user_Interests ;
        }

        @Override
        protected void onPostExecute(List<Interest> result) {
            //  Toast.makeText(Dashboard_ctivity.this, String.valueOf(result.getClicks()), Toast.LENGTH_SHORT).show();
            StringBuilder builder = new StringBuilder();

            for(Interest interest : result){
                //Log.d("waqas",String.valueOf(interest));

                Userlist.add(interest);

            }
            //  modelArrayList = getModel(false);

            new GetInterest().execute();


            super.onPostExecute(result);

        }
    }
    private class UserRemoveTask extends AsyncTask<String, Void, Void> {
        private ApiValidationException validationException;

        @Override
        protected Void doInBackground(String... strings) {
            //  binding.setTasksInProgress(binding.getTasksInProgress() + 1);
            try {
                adcharge.getApiWrapper().saveUser(user);
                //sdk.getStatistics()
            } catch (IOException e) {

            } catch (ApiException e) {

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

                }
            } else {


                for (int i = 0; i < Category_Adapter.imageModelArrayList.size(); i++){
                    if(Category_Adapter.imageModelArrayList.get(i).getSelected()) {
                        //     aa.setText(aa.getText() + " " + Category_Adapter.modelArrayList.get(i).getAnimal());
                        user.getInterests().add(Category_Adapter.imageModelArrayList.get(i).getAnimal());

                    }
                }
                new UserSaveTask().execute();

            }

        }
    }


    private class UserSaveTask extends AsyncTask<String, Void, Void> {
        private ApiValidationException validationException;

        @Override
        protected Void doInBackground(String... strings) {
            //  binding.setTasksInProgress(binding.getTasksInProgress() + 1);
            try {
                adcharge.getApiWrapper().saveUser(user);
                //sdk.getStatistics()
            } catch (IOException e) {

            } catch (ApiException e) {

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

                }
            } else {

                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Category_Activity.this, Dashboard_Activity.class);
                startActivity(intent);
                Category_Activity.this.finish();
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_logout) {
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Category_Activity.this,Dashboard_Activity.class);
        startActivity(intent);
        Category_Activity.this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
