package honeybadgersapp.honeybadgers.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import RetrofitModels.ProfileObject;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfile extends AppCompatActivity {



    private EditProfile.ProfileSaveTask mAuthTask = null;
    // UI references.
    private EditText mName;
    private EditText mBio;
    private EditText mEmail;
    private TextView mEditPhoto;
    private Button mSaveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_profile);
        //Prevent keyboard from automatically opening
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mName =  findViewById(R.id.edit_profile_name);
        mBio=findViewById(R.id.edit_profile_description);
        mEmail=findViewById(R.id.edit_profile_email);
        mEditPhoto=findViewById(R.id.edit_photo_photo_button);
        mSaveButton = findViewById(R.id.edit_profile_save_button);
        mSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptProfileSave();
            }
        });


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        Call<List<ProfileObject>> call= RetrofitClient.getInstance().getApi().userProfileGet("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[4]);
        call.enqueue(new Callback<List<ProfileObject>>() {
            @Override
            public void onResponse(Call<List<ProfileObject>> call, Response<List<ProfileObject>> response) {
                List<ProfileObject> editResponse = response.body();
                if (response.isSuccessful()){
                    mName.setText( editResponse.get(0).getName());
                    mBio.setText( editResponse.get(0).getBody());
                    mEmail.setText(LoginActivity.getCREDENTIALS()[1]);
                    LoginActivity.getCREDENTIALS()[5]=""+editResponse.get(0).getId();
                    Log.d("MyTag","success profile load");
                    Toast.makeText(EditProfile.this,"SUCCESS LOAD PROFILE !!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProfileObject>> call, Throwable t) {
                Log.d("MyTag","Failed");
                Toast.makeText(EditProfile.this,"FAILED TO LOAD PROFILE !!",Toast.LENGTH_LONG).show();
            }
        });

    }



    /**
     * Attempts to save the profile form
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual save attempt is made.
     */
    private void attemptProfileSave() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mName.setError(null);
        mBio.setError(null);

        // Store values at the time of the save attempt.
        String name = mName.getText().toString();
        String bio= mBio.getText().toString();

        boolean cancel = false;
        View focusView = null;

        /*
        //TODO: Make this portion available incase we allow the user to change his password
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }*/

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }else if (TextUtils.isEmpty(bio)) {
            mBio.setError(getString(R.string.error_field_required));
            Log.d("MyTag","BİO: "+bio);
            focusView = mBio;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt save and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // maybe Show a progress spinner , and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new ProfileSaveTask(name, bio);
            mAuthTask.execute((Void) null);
        }
    }

  /*  private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@")&&email.contains(".com");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }*/


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ProfileSaveTask extends AsyncTask<Void, Void, Boolean> {

        private final String name;
        private final String bio;

        ProfileSaveTask(String name, String bio) {
            this.name = name;
            this.bio= bio;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {

                Call<ProfileObject> call= RetrofitClient.getInstance().getApi().userProfileUpdate("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[5],name,null,bio);
                call.enqueue(new Callback<ProfileObject>() {
                    @Override
                    public void onResponse(Call<ProfileObject> call, Response<ProfileObject> response) {
                        if (response.isSuccessful()){
                            Log.d("MyTag","Geldim");
                            Toast.makeText(EditProfile.this,"SUCCESSFUL EDITTING",Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(EditProfile.this,"EMPTY !!",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileObject> call, Throwable t) {
                        Log.d("MyTag","Failed");
                    }
                });

            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

