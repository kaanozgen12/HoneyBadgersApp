package honeybadgersapp.honeybadgers.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import RetrofitModels.ProfileObject;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfile extends AppCompatActivity {


    private int PICK_IMAGE_REQUEST = 1;
    private EditProfile.ProfileSaveTask mAuthTask = null;
    // UI references.
    private String avatar_string;
    private EditText mName;
    private EditText mBio;
    private EditText mEmail;
    private ImageView logo;
    private TextView mEditPhoto;
    private Button mSaveButton;
    private Uri file = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_profile);
        //Prevent keyboard from automatically opening
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        overridePendingTransition (0,0);
        mName =  findViewById(R.id.edit_profile_name);
        mBio=findViewById(R.id.edit_profile_description);
        mEmail=findViewById(R.id.edit_profile_email);
        mEditPhoto=findViewById(R.id.edit_photo_photo_button);
        mEditPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });
        logo=findViewById(R.id.edit_photo_photo);
        mSaveButton = findViewById(R.id.edit_profile_save_button);
        mSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptProfileSave();
            }
        });
        new SendHttpRequestTask().execute(avatar_string);

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
                    new SendHttpRequestTask().execute(editResponse.get(0).getAvatar());
                    avatar_string= editResponse.get(0).getAvatar();
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

    @Override
    protected void onResume() {
        super.onResume();

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
            mAuthTask = new ProfileSaveTask(name, bio, ((BitmapDrawable) logo.getDrawable()).getBitmap() );
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
        private final Bitmap logo;

        ProfileSaveTask(String name, String bio, Bitmap logo) {
            this.name = name;
            this.bio= bio;
            this.logo= logo;
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
                if(file!=null){
                    //create a file to write bitmap data
                    File f = new File(getBaseContext().getCacheDir(), LoginActivity.getCREDENTIALS()[4]+"_logo");
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    //write the bytes in file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("upload", f.getName(), reqFile);


                    Call<ResponseBody> call2= RetrofitClient.getInstance().getApi().postImage(body);
                    call2.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                Call<ProfileObject> call= RetrofitClient.getInstance().getApi().userProfileUpdate("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[5],name,LoginActivity.getCREDENTIALS()[4]+"_logo",bio);
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

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call2, Throwable t) {
                            Log.d("MyTag","Failed");
                        }
                    });

                }else{
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
                }




               /* File file = new File(imageUri.getPath());
                RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), firstNameField.getText().toString());
                Call<User> call = client.editUser(getToken(), fbody, name, id);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(retrofit.Response<User> response, Retrofit retrofit) {
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });*/


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

    public class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (Exception e){
                //
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result!=null)
            logo.setImageBitmap(result);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            file = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);

                logo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

