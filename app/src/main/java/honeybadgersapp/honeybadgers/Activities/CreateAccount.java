package honeybadgersapp.honeybadgers.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import RetrofitModels.LoginResponse;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class CreateAccount extends AppCompatActivity {

    Animation textanimation;


    private CreateAccount.UserLogUpTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textanimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.main_page_button_anim);

        setContentView(R.layout.create_account);
        // Set up the login form.
        mEmailView = findViewById(R.id.email_account_create);

        //Prevent keyboard from automatically opening
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mPasswordView = findViewById(R.id.password_account_create);
        mPasswordConfirmView  = findViewById(R.id.password_account_create_retype);

        Button mEmailSignUpButton =  findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               attemptSignUp();
            }
        });

        mEmailView.setAnimation(textanimation);
        mEmailView.startAnimation(textanimation);
        mPasswordView.setAnimation(textanimation);
        mPasswordView.startAnimation(textanimation);
        mPasswordConfirmView.setAnimation(textanimation);
        mPasswordConfirmView.startAnimation(textanimation);
        mEmailSignUpButton.setAnimation(textanimation);
        mEmailSignUpButton.startAnimation(textanimation);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordConfirm = mPasswordConfirmView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(passwordConfirm) || !passwordConfirm.equals(password)) {
            mPasswordConfirmView.setError(getString(R.string.error_password_mismatch));
            focusView = mPasswordConfirmView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLogUpTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@")&&email.contains(".com");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLogUpTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLogUpTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;


            Log.d("MyTag", "burdayım "+success);
            if (success) {
                Call<LoginResponse> call= RetrofitClient.getInstance().getApi().userRegister(mEmail," ",mPassword);
                Log.d("MyTag", "call: "+call.toString());
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        //LoginResponse loginResponse = response.body();
                        Log.d("MyTag", "response: "+response.message());
                        if (response.isSuccessful()){
                            Log.d("MyTag","Geldim");
                            Thread timer = new Thread(){
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    super.run();
                                }
                            };
                            timer.start();
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d("MyTag","Failed");
                    }
                });

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

