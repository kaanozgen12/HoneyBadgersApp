package honeybadgersapp.honeybadgers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import honeybadgersapp.honeybadgers.R;

public class SplashActivity extends AppCompatActivity {
    ImageView logo;
    Animation frombottom;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        frombottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.frombottom_splash_logo);
        logo = findViewById(R.id.splash_logo);
        logo.startAnimation(frombottom);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                    super.run();
                }catch (InterruptedException e){

                    e.printStackTrace();
                }
            }
        };

        timer.start();

    }
}
