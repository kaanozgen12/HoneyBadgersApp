package honeybadgersapp.honeybadgers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import honeybadgersapp.honeybadgers.R;

public class InitialLogin extends AppCompatActivity {

    Animation fadinganimation;
    private LinearLayout form;
    private RadioButton freelancer;
    private RadioButton client;
    private Button okbutton;
    private TextView welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        overridePendingTransition (0,0);
        setContentView(R.layout.first_register_popup);
        fadinganimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear);
        form= findViewById(R.id.first_register_popup_form);
        freelancer = findViewById(R.id.radioFreelancer);
        freelancer.toggle();
        client  = findViewById(R.id.radioClient);
        okbutton = findViewById(R.id.first_register_button);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attemptToProceed()){
                    Thread login = new Thread(){
                        @Override
                        public void run() {
                            try {
                                sleep(2000);
                                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                                if(freelancer.isChecked()){
                                    intent.putExtra("Role","Freelancer");
                                    LoginActivity.getCREDENTIALS()[3]="Freelancer";
                                }
                                else if(client.isChecked()){
                                    intent.putExtra("Role","Client");
                                    LoginActivity.getCREDENTIALS()[3]="Client";
                                }
                                startActivity(intent);
                                finish();
                                super.run();
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    };
                    login.start();
                }
            }
        });
        welcome  = findViewById(R.id.welcoming_message);



        welcome.setAnimation(fadinganimation);
        welcome.setVisibility(View.VISIBLE);
        welcome.startAnimation(fadinganimation);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                welcome.setVisibility(View.GONE);
                form.setVisibility(View.VISIBLE);
                form.setAnimation(fadinganimation);
                form.startAnimation(fadinganimation);
            }
        }, 2300);




    }
    private boolean attemptToProceed() {
        return (freelancer.isChecked()||client.isChecked())&&(!(freelancer.isChecked()&&client.isChecked()));
    }
}