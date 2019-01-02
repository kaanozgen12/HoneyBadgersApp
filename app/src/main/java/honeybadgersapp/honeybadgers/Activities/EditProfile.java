package honeybadgersapp.honeybadgers.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Adapters.Create_project_tags_adapter;
import RetrofitModels.ProfileObject;
import RetrofitModels.Tag_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfile extends AppCompatActivity {


    public static String URL= "http://18.130.93.29:8000/media/files/";
    private int PICK_IMAGE_REQUEST = 1;
    private EditProfile.ProfileSaveTask mAuthTask = null;
    // UI references.
    private String avatar_string;
    private EditText mBio;
    private EditText mEmail;
    private ImageView logo;
    private TextView mEditPhoto;

    String tags[];
    ArrayAdapter<String> TagAdapter = null;
    private AutoCompleteTextView autoCompleteTag;
    private  RecyclerView mRecylerView ;

    private TextView mSkillsTitle;
    //private Spinner mSpinner;
    ArrayList<Tag_Object> tag_list = new ArrayList<>();
    final Create_project_tags_adapter[] recyclerAdapter = {new Create_project_tags_adapter(this, tag_list)};


    private Button mSaveButton;
    private Uri file = null;
    Bitmap bitmap;
    String picturePath;
    Bitmap photo;
    String ba1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_profile);
        //Prevent keyboard from automatically opening
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        overridePendingTransition (0,0);
        mBio=findViewById(R.id.edit_profile_description);

        mRecylerView = findViewById(R.id.edit_profile_recycler_view);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecylerView.setLayoutManager(horizontalLayoutManager);
        mRecylerView.setAdapter(recyclerAdapter[0]);

        Call<List<Tag_Object>> call= RetrofitClient.getInstance().getApi().getTags();
        call.enqueue(new Callback<List<Tag_Object>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tag_Object>> call, @NonNull Response<List<Tag_Object>> response0) {
                if (response0.isSuccessful()){
                    tags = new String[response0.body().size()];
                    for (int x = 0 ; x< response0.body().size(); x++){
                        tags[x]=response0.body().get(x).getTitle();
                    }
                    TagAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.hint_completion_layout, R.id.tvHintCompletion, tags);
                    autoCompleteTag.setAdapter(TagAdapter);
                }
            }
            @Override
            public void onFailure(Call<List<Tag_Object>> call, Throwable t) {

            }
        });
        autoCompleteTag = findViewById(R.id.edit_profile_tags_edit);
        autoCompleteTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Call<ArrayList<Tag_Object>> call0= RetrofitClient.getInstance().getApi().getTagbyTitle((String)adapterView.getItemAtPosition(i));
                call0.enqueue(new Callback<ArrayList<Tag_Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Tag_Object>> call0, @NonNull Response<ArrayList<Tag_Object>> response0) {
                        if (response0.isSuccessful()){
                            Tag_Object temp = new Tag_Object(response0.body().get(0).getId(),response0.body().get(0).getTitle());
                            if(!tag_list.contains(temp)){
                                tag_list.add(temp);
                                Objects.requireNonNull(mRecylerView.getAdapter()).notifyDataSetChanged();
                                mRecylerView.smoothScrollToPosition(recyclerAdapter[0].getItemCount()-1);
                            }

                        }else{
                            Toast.makeText(EditProfile.this,"Create Tag Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ArrayList<Tag_Object>> call0, Throwable t) {
                        Toast.makeText(EditProfile.this,"Create Tag Failed Response from server",Toast.LENGTH_LONG).show();
                    }
                });
                autoCompleteTag.setText("");
                mSkillsTitle.clearFocus();
            }
        });
        autoCompleteTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){

                    Call<Tag_Object> call= RetrofitClient.getInstance().getApi().postTag("token "+LoginActivity.getCREDENTIALS()[0],autoCompleteTag.getText().toString());
                    call.enqueue(new Callback<Tag_Object>() {
                        @Override
                        public void onResponse(@NonNull Call<Tag_Object> call, @NonNull Response<Tag_Object> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(EditProfile.this,"Create Tag Successful",Toast.LENGTH_LONG).show();
                                tag_list.add(new Tag_Object(response.body().getId(),autoCompleteTag.getText().toString()));
                                autoCompleteTag.setText("");
                                Objects.requireNonNull(mRecylerView.getAdapter()).notifyDataSetChanged();
                            }else{
                                Toast.makeText(EditProfile.this,"Create Tag Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<Tag_Object> call, @NonNull Throwable t) {
                            Toast.makeText(EditProfile.this,"Create Tag Failed Response from server",Toast.LENGTH_LONG).show();
                        }
                    });

                    return true;
                }
                return false;
            }
        });

        mSkillsTitle= findViewById(R.id.edit_profile_skills_or_interests);
        mSkillsTitle.setText((LoginActivity.getCREDENTIALS()[3].equalsIgnoreCase("Freelancer"))?"Skills":"Interests");
        mEmail=findViewById(R.id.edit_profile_email);
        mEmail.setText(LoginActivity.getCREDENTIALS()[1]);
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

        Call<List<ProfileObject>> call;
        if(LoginActivity.getCREDENTIALS()[3].equalsIgnoreCase("freelancer")){
            call = RetrofitClient.getInstance().getApi().freelancerProfileGet("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[4]);

        }else{
            call= RetrofitClient.getInstance().getApi().clientProfileGet("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[4]);
        }
        call.enqueue(new Callback<List<ProfileObject>>() {
            @Override
            public void onResponse(Call<List<ProfileObject>> call, Response<List<ProfileObject>> response) {
                List<ProfileObject> editResponse = response.body();
                if (response.isSuccessful()&& editResponse.size()>0){
                    mBio.setText( editResponse.get(0).getBody());
                    mEmail.setText(LoginActivity.getCREDENTIALS()[1]);
                    new SendHttpRequestTask().execute(editResponse.get(0).getAvatar());
                    avatar_string= editResponse.get(0).getAvatar();
                    tag_list= turn_Integer_List_to_Tag_List(editResponse.get(0).getTags());
                    recyclerAdapter[0] = new Create_project_tags_adapter(getApplicationContext(), tag_list);
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    mRecylerView.setLayoutManager(horizontalLayoutManager);
                    mRecylerView.setAdapter(recyclerAdapter[0]);

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
        mBio.setError(null);

        // Store values at the time of the save attempt.
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
        if (TextUtils.isEmpty(bio)) {
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
            mAuthTask = new ProfileSaveTask( bio, ((BitmapDrawable) logo.getDrawable()).getBitmap() );
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

        private final String bio;
        private final Bitmap logo;

        ProfileSaveTask( String bio, Bitmap logo) {
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
                    File f = null;
                    try {
                        f = File.createTempFile("temp_logo", null, getApplicationContext().getCacheDir());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                   /* RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), f);

                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("image", f.getName(), requestFile);*/
                    // Create a request body with file and image media type


                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), f);
                    Log.d("MyTag","CONTENT TYPE: "+fileReqBody.contentType());
                    // Create MultipartBody.Part using file request-body,file name and part name
                    //MultipartBody.Part part = MultipartBody.Part.createFormData("image/*", f.getName(), fileReqBody);
                    RequestBody part = RequestBody.create(MediaType.parse("Image"),f);
                    RequestBody dsc =
                            RequestBody.create(MediaType.parse("text/plain"), bio);

                    //
                   /* RequestParams params = new RequestParams();

                        params.put("photo", bitmap);


                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post("http://18.130.93.29:8000/media/files/", params, new JsonHttpResponseHandler() {
                        ProgressDialog pd;
                        @Override
                        public void onStart() {
                            String uploadingMessage = "Uploading";
                            pd = new ProgressDialog(EditProfile.this);
                            pd.setTitle("Please Wait");
                            pd.setMessage(uploadingMessage);
                            pd.setIndeterminate(false);
                            pd.show();
                        }

                        @Override
                        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Log.d("MyTag","errrrooooor: "+throwable.toString());
                        }

                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                String status =  (String) response.get("status");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                String photoID =  (String) response.get("photoID");
                                Log.d("MyTag","photo I>D: ::: "+photoID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFinish() {
                            pd.dismiss();
                        }
                    });*/
                    //
                    Call<Void> call;
                    if(LoginActivity.getCREDENTIALS()[3].equalsIgnoreCase("freelancer")){
                        call = RetrofitClient.getInstance().getApi().freelancerProfileUpdate("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[5],part,dsc);

                        Log.d("MyTag",LoginActivity.getCREDENTIALS()[5]);

                    }else{
                        call = RetrofitClient.getInstance().getApi().clientProfileUpdate("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[5],part,dsc);
                        Log.d("MyTag",LoginActivity.getCREDENTIALS()[5]);
                    }

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()){
                                Log.d("MyTag","Geldim");
                                Toast.makeText(EditProfile.this,"SUCCESSFUL EDITTING",Toast.LENGTH_LONG).show();
                                finish();
                            }else{

                                Log.d("MyTag","----------  "+response.message().toString());
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("MyTag","Failed");
                        }
                    });

                }else{
                    Call<ProfileObject> call;
                    if(LoginActivity.getCREDENTIALS()[3].equalsIgnoreCase("freelancer")){
                        call = RetrofitClient.getInstance().getApi().freelancerProfileUpdateImageless("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[5],bio,turnTags_List_to_Integer_List(tag_list));
                        Log.d("MyTag",LoginActivity.getCREDENTIALS()[5]);

                    }else{
                        call= RetrofitClient.getInstance().getApi().clientProfileUpdateImageless("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[5],bio,turnTags_List_to_Integer_List(tag_list));
                        Log.d("MyTag",LoginActivity.getCREDENTIALS()[5]);
                    }
                    call.enqueue(new Callback<ProfileObject>() {
                        @Override
                        public void onResponse(Call<ProfileObject> call, Response<ProfileObject> response) {
                            if (response.isSuccessful()){
                                Log.d("MyTag","Geldim");
                                Toast.makeText(EditProfile.this,"SUCCESSFUL EDITTING",Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(EditProfile.this,"EMPTY !!  "+response.body(),Toast.LENGTH_LONG).show();
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
   /* private void upload() {
        // Image location URL
        Log.d("MyTag", "----------------" + file.getEncodedPath());

        // Image
        Bitmap bm = BitmapFactory.decodeFile(file.getPath());
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);

        Log.e("base64", "-----" + ba1);

        // Upload image to server
        new uploadToServer().execute();

    }

    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(EditProfile.this);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", logo.getDrawable().get));
            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(URL);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }*/

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
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                logo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public  ArrayList<Tag_Object> turn_Integer_List_to_Tag_List(final int[] s){
        final ArrayList<Tag_Object> temp= new ArrayList<>();
        Call<List<Tag_Object>> call= RetrofitClient.getInstance().getApi().getTags();
        call.enqueue(new Callback<List<Tag_Object>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tag_Object>> call, @NonNull Response<List<Tag_Object>> response0) {
                if (response0.isSuccessful()){
                    tags = new String[response0.body().size()];
                    for (int x = 0 ; x< response0.body().size(); x++){
                        tags[x]=response0.body().get(x).getTitle();
                    }
                    TagAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.hint_completion_layout, R.id.tvHintCompletion, tags);
                    autoCompleteTag.setAdapter(TagAdapter);

                    for (int i =0 ; i< s.length; i++){
                        temp.add(new Tag_Object(s[i],tags[s[i]-1]));
                        recyclerAdapter[0].notifyDataSetChanged();
                    }

                }
            }
            @Override
            public void onFailure(Call<List<Tag_Object>> call, Throwable t) {

            }
        });
        return temp;
    }
    public static int[] turnTags_List_to_Integer_List(ArrayList<Tag_Object> s){

        int[] temp= new int[s.size()];
        for (int i =0 ; i< s.size(); i++){
            temp[i]=s.get(i).getId();
        }
        return temp;
    }

}

