package honeybadgersapp.honeybadgers.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import RetrofitModels.ProfileObject;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Show_Profile extends AppCompatActivity {

    public static String user_id;
    public static String user_email;
    // UI references.
    private TextView mName;
    private TextView mBio;
    private TextView mEmail;
    private ImageView mPhoto;
    private RatingBar mRating;
    private Button mChangeProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.see_profile);
        mName =  findViewById(R.id.show_profile_name);
        mBio=findViewById(R.id.show_profile_description_edit);
        mEmail=findViewById(R.id.show_profile_email);
        mPhoto=findViewById(R.id.show_profile_photo);
        mRating= findViewById(R.id.show_profile_rating_bar);
        mChangeProfile = findViewById(R.id.see_profile_change_profile);



    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        Call<List<ProfileObject>> call= RetrofitClient.getInstance().getApi().userProfileGet("token "+LoginActivity.getCREDENTIALS()[0],user_id);
        call.enqueue(new Callback<List<ProfileObject>>() {
            @Override
            public void onResponse(Call<List<ProfileObject>> call, Response<List<ProfileObject>> response) {
                List<ProfileObject> editResponse = response.body();
                if (response.isSuccessful()){
                    mName.setText( editResponse.get(0).getName());
                    mBio.setText( editResponse.get(0).getBody());
                    mEmail.setText(user_email);
                    //mPhoto.setBackground();
                    mRating.setRating(editResponse.get(0).getRating());
                }
            }

            @Override
            public void onFailure(Call<List<ProfileObject>> call, Throwable t) {
                Log.d("MyTag","Failed");
            }
        });

    }

}


