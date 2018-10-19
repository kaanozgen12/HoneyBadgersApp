package honeybadgersapp.honeybadgers.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import honeybadgersapp.honeybadgers.R;

/**
 * A login screen that offers login via email/password.
 */
public class EditProfile extends AppCompatActivity {


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private EditProfile.ProfileSaveTask mAuthTask = null;
    // UI references.
    private EditText mName;
    private EditText mBio;
    private DatePicker mBirthday;
    private EditText mEmail;
    private TextView mEditPhoto;
    private Button mSaveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_profile);
        //Prevent keyboard from automatically opening
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mName =  findViewById(R.id.edit_profile_name);
        mBio=findViewById(R.id.edit_profile_bio);
        mBirthday = findViewById(R.id.edit_profile_birthday);
        mEmail=findViewById(R.id.edit_profile_email);
        mEditPhoto=findViewById(R.id.edit_photo_photo_button);

        fillUpProfilePage();
        mSaveButton = findViewById(R.id.edit_profile_save_button);
        mSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptProfileSave();
            }
        });


    }

    private void fillUpProfilePage() {
        mEmail.setText("deneme@hotmail.com");


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
        String birthday= mBirthday.toString();
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
            mAuthTask = new ProfileSaveTask(name, birthday, bio);
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
        private final String birthday;
        private final String bio;

        ProfileSaveTask(String name, String birthday, String bio) {
            this.name = name;
            this.birthday = birthday;
            this.bio= bio;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            /*
            Unnecessary I guess
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                finish();

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

