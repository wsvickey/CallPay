package demo.app.adcharge.eu.sdkdemo.First_Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.model.SliderPage;

import demo.app.adcharge.eu.sdkdemo.Login_Activity;
import demo.app.adcharge.eu.sdkdemo.R;

public class IntroActivity extends AppIntro {

    @SuppressLint("ResourceAsColor")
    @Override
    public void init(Bundle savedInstanceState) {
        //   super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        addSlide(IntroFragment.newInstance(R.layout.main_intro_1,this));
        addSlide(IntroFragment.newInstance(R.layout.main_intro_2,this));
        addSlide(IntroFragment.newInstance(R.layout.main_intro_3,this));
        addSlide(IntroFragment.newInstance(R.layout.main_intro_4,this));
        addSlide(IntroFragment.newInstance(R.layout.main_intro_5,this));

        // Show and Hide Skip and Done buttons
        showStatusBar(true);
//nextButton.setBackgroundColor(R.color.white);

        showSkipButton(false);
        // setIndicatorColor(R.color.colorPrimaryDark,R.color.white);
    //   setNavBarColor("#FF0000");
      //  nextButton.setBackgroundResource(R.drawable.ic_add_circle_black_24dp);
        //setFeatureDrawableResource(R.color.white,R.color.colorPrimaryDark);
        setZoomAnimation();
         askForPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
         askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
         askForPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 4);
       // permissionToDrawOverlays();

    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent i = new Intent(getApplicationContext(), Login_Activity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.

    }



}
