package demo.app.adcharge.eu.sdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_Act extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

                Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3*1000);
                    Intent intent=new Intent(Splash_Act.this, Dashboard_Activity.class);
                    startActivity(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
