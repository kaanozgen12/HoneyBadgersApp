package honeybadgersapp.honeybadgers.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import Adapters.Create_project_tags_adapter;
import Fragments.MapAdapter;
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProject extends MapsActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private CreateProject.saveProjectTask mAuthTask = null;
    String tags[];
    ArrayAdapter<String> TagAdapter = null;
    private AutoCompleteTextView autoCompleteTag;
    private ScrollView scrollview;
    private EditText mTitle;
    private EditText mBody;
    private TimePicker mDeadlineTime;
    private DatePicker mDeadlineDate;
    private EditText mBudgetMin;
    private EditText mBudgetMax;
    private Button mButton;
    private TextView mSkillsTitle;
    private Spinner mSpinner;
    private CheckBox map_activator;
    private GoogleMap mMap;
    private View mapView;
    private String category = "";
    private Marker m;
    private View.OnTouchListener mListener;
    final int[] marker_count = {0};
    ArrayList<Tag_Object> tag_list = new ArrayList<>();
    final Create_project_tags_adapter[] recyclerAdapter = {new Create_project_tags_adapter(this, tag_list)};



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.create_project);
        //Prevent keyboard from automatically opening
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final RecyclerView mRecylerView = findViewById(R.id.create_project_recyclerview);
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


        // TagAdapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, tags);
        autoCompleteTag = findViewById(R.id.create_project_tags_edit);
        map_activator = findViewById(R.id.create_project_map_switch);
        scrollview= findViewById(R.id.create_project_scroll_view);
        mapView = findViewById(R.id.create_project_map_edit);
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
                            Toast.makeText(CreateProject.this,"Create Tag Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ArrayList<Tag_Object>> call0, Throwable t) {
                        Toast.makeText(CreateProject.this,"Create Tag Failed Response from server",Toast.LENGTH_LONG).show();
                    }
                });
                autoCompleteTag.setText("");
                mSkillsTitle.clearFocus();
            }
        });

        MapAdapter mapFragment = (MapAdapter) getSupportFragmentManager().findFragmentById(R.id.create_project_map_edit);
        mapFragment.getMapAsync(this);
        map_activator.setChecked(false);
        mapView.setVisibility(View.GONE);


        mapFragment.setListener(new MapAdapter.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollview.requestDisallowInterceptTouchEvent(true);
            }
        });



        map_activator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (map_activator.isChecked()){
                    mapView.setVisibility(View.VISIBLE);
                    int[] cords = new int[2];
                    scrollview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    },400);
                }else {
                    mapView.setVisibility(View.GONE);
                }
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
                                Toast.makeText(CreateProject.this,"Create Tag Successful",Toast.LENGTH_LONG).show();
                                tag_list.add(new Tag_Object(response.body().getId(),autoCompleteTag.getText().toString()));
                                autoCompleteTag.setText("");
                                Objects.requireNonNull(mRecylerView.getAdapter()).notifyDataSetChanged();
                            }else{
                                Toast.makeText(CreateProject.this,"Create Tag Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<Tag_Object> call, @NonNull Throwable t) {
                            Toast.makeText(CreateProject.this,"Create Tag Failed Response from server",Toast.LENGTH_LONG).show();
                        }
                    });

                    return true;
                }
                return false;
            }
        });
        mSpinner = findViewById(R.id.create_project_category_edit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = ""+(i+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category = "1";
            }
        });
        mTitle = findViewById(R.id.create_project_title_edit);
        mBody = findViewById(R.id.create_project_description_edit);
        mDeadlineDate = findViewById(R.id.date_picker);
        mDeadlineTime = findViewById(R.id.time_picker);
        mBudgetMin = findViewById(R.id.create_project_budget_min_edit);
        mBudgetMax = findViewById(R.id.create_project_budget_max_edit);
        mButton = findViewById(R.id.create_project_button);
        mSkillsTitle = findViewById(R.id.create_project_skills_title);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreate();
            }
        });


        if(!getIntent().getExtras().getString("project_id").equalsIgnoreCase("-1")){
            Call<ProjectObject> call2= RetrofitClient.getInstance().getApi().getProjectbyId(Integer.parseInt(getIntent().getExtras().getString("project_id")));
            call2.enqueue(new Callback<ProjectObject>() {
                @Override
                public void onResponse(@NonNull Call<ProjectObject> call2, @NonNull Response<ProjectObject> response) {
                    if (response.isSuccessful()){
                        mTitle.setText(response.body().getTitle());
                        mBody.setText(response.body().getDescription());
                        mBudgetMin.setText(""+response.body().getBudgetMin());
                        mBudgetMax.setText(""+response.body().getBudgetMax());
                        tag_list= turn_Integer_List_to_Tag_List(response.body().getTags());
                        recyclerAdapter[0] = new Create_project_tags_adapter(getApplicationContext(), tag_list);
                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        mRecylerView.setLayoutManager(horizontalLayoutManager);
                        mRecylerView.setAdapter(recyclerAdapter[0]);

                        if(response.body().getLatitude()!=null&&response.body().getLongitude()!=null){
                            map_activator.setActivated(true);
                            m = mMap.addMarker(new MarkerOptions()
                                    .position(
                                            new LatLng(response.body().getLatitude(),
                                                    response.body().getLongitude()))
                                    .draggable(true).visible(true));
                        }

                    }
                }
                @Override
                public void onFailure(@NonNull Call<ProjectObject> call2, @NonNull Throwable t) {
                   }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                if(marker_count[0] <2){
                    //if there is a marker already this if condition removes it

                    if (m != null) {
                        m.remove();
                        marker_count[0] = marker_count[0] -1;
                    }
                    marker_count[0] = marker_count[0] +1;
                    m = mMap.addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(latLng.latitude,
                                            latLng.longitude))
                            .draggable(true).visible(true));
                }
            }

        });
        LatLng calif = new LatLng(36, -119);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(calif));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }




    private void attemptCreate() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mTitle.setError(null);
        mBody.setError(null);
        mBudgetMin.setError(null);
        mBudgetMax.setError(null);
        autoCompleteTag.setError(null);

        String title = mTitle.getText().toString();
        String description = mBody.getText().toString();
        long dateTime =  mDeadlineDate.getCalendarView().getDate();
        long timtTime = mDeadlineTime.getDrawingTime();
        String budgetMin = mBudgetMin.getText().toString();
        String budgetMax = mBudgetMax.getText().toString();
        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(title)) {
            mTitle.setError(getString(R.string.error_field_required));
            focusView = mTitle;
            cancel = true;
        }else if (TextUtils.isEmpty(description)) {
            mBody.setError(getString(R.string.error_field_required));
            focusView = mBody;
            cancel = true;
        }else if (TextUtils.isEmpty(budgetMin)) {
            mBudgetMin.setError(getString(R.string.error_field_required));
            focusView = mBudgetMin;
            cancel = true;
        }else if (TextUtils.isEmpty(budgetMax)) {
            mBudgetMax.setError(getString(R.string.error_field_required));
            focusView = mBudgetMax;
            cancel = true;
        }else if (Integer.parseInt(budgetMin)>=Integer.parseInt(budgetMax)) {
            mBudgetMin.setError("Minimum budget must be less than Maximum budget");
            focusView = mBudgetMin;
            cancel = true;
        }
        else if (tag_list.isEmpty()) {
            mSkillsTitle.setError(getString(R.string.error_field_required));
            focusView = mSkillsTitle;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt save and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {


            int year = mDeadlineDate.getYear();
            int month = mDeadlineDate.getMonth();
            int day = mDeadlineDate.getDayOfMonth();
            int hour = mDeadlineTime.getCurrentHour();
            int minute = mDeadlineTime.getCurrentMinute();
            String deadline= ""+year+"-"+month+"-"+day+"T"+hour+":"+minute+":00"+"Z";


            mAuthTask = new saveProjectTask(category, title, description,deadline,budgetMin,budgetMax,tag_list,( (map_activator.isChecked())?(float) m.getPosition().latitude:null )  ,( (map_activator.isChecked())?(float) m.getPosition().longitude:null ) );
            mAuthTask.execute((Void) null);
        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        if(marker_count[0] <2){
            //if there is a marker already this if condition removes it

            if (m != null) {
                m.remove();
                marker_count[0] = marker_count[0] -1;
            }
            marker_count[0] = marker_count[0] +1;
            m = mMap.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(latLng.latitude,
                                    latLng.longitude))
                    .draggable(true).visible(true));
        }
    }

    public class saveProjectTask extends AsyncTask<Void, Void, Boolean> {

        private final String category,title,description,budgetMin,budgetMax,deadline;
        private Float latitude,longitude;
        private final ArrayList<Tag_Object> tags;

        public saveProjectTask(String category, String title, String description, String  deadline, String budgetMin,String budgetMax, ArrayList<Tag_Object> tags , Float latitude , Float longitude) {
            this.category= category;
            this.title = title;
            this.description = description;
            this.deadline = deadline;
            this.budgetMin = budgetMin;
            this.budgetMax = budgetMax;
            this.tags = tags;
            this.latitude=latitude;
            this.longitude=longitude;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(300);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;



            Call<ProjectObject> call= RetrofitClient.getInstance().getApi().projectCreatePost("token "+LoginActivity.getCREDENTIALS()[0],title,description,turnTags_List_to_Integer_List(tag_list),Integer.parseInt(category),Integer.parseInt(budgetMin),Integer.parseInt(budgetMax),deadline, latitude, longitude);
            call.enqueue(new Callback<ProjectObject>() {
                @Override
                public void onResponse(@NonNull Call<ProjectObject> call, @NonNull Response<ProjectObject> response) {
                    if (response.isSuccessful()){
                        Log.d("MyTag","Geldim");
                        Toast.makeText(CreateProject.this,"SUCCESSFUL EDITTING",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(CreateProject.this, Arrays.toString(turnTags_List_to_Integer_List(tag_list)) +"EMPTY !! "+ category,Toast.LENGTH_LONG).show();
                        Log.d("MyTag","DEADLINE :"+deadline);
                        Log.d("MyTag","body :"+response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProjectObject> call, Throwable t) {
                    Log.d("MyTag","Failed");
                }
            });

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }



    public static int[] turnTags_List_to_Integer_List(ArrayList<Tag_Object> s){

        int[] temp= new int[s.size()];
        for (int i =0 ; i< s.size(); i++){
            temp[i]=s.get(i).getId();
        }
        return temp;
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

    public void onMapSearch(View view) {
        EditText locationSearch = findViewById(R.id.create_project_map_editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addressList.size()>0){
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                if(marker_count[0] <2){
                    //if there is a marker already this if condition removes it

                    if (m != null) {
                        m.remove();
                        marker_count[0] = marker_count[0] -1;
                    }
                    marker_count[0] = marker_count[0] +1;
                    m = mMap.addMarker(new MarkerOptions().position(latLng)
                            .draggable(true).visible(true));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }

        }

    }

}

