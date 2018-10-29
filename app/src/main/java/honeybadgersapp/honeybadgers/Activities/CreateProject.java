package honeybadgersapp.honeybadgers.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import Adapters.Tag_adapter_create_project;
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProject extends AppCompatActivity{

    private CreateProject.saveProjectTask mAuthTask = null;
    String tags[] = null;
    ArrayAdapter<String> TagAdapter = null;
    private AutoCompleteTextView autoCompleteTag;
    private EditText mTitle;
    private EditText mBody;
    private TimePicker mDeadlineTime;
    private DatePicker mDeadlineDate;
    private EditText mBudgetMin;
    private EditText mBudgetMax;
    private Button mButton;
    private TextView mSkillsTitle;
    private Spinner mSpinner;
    private String category="";
    ArrayList<Tag_Object> tag_list = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.create_project);
        //Prevent keyboard from automatically opening
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final RecyclerView mRecylerView= findViewById(R.id.create_project_recyclerview);
        Tag_adapter_create_project recyclerAdapter =new Tag_adapter_create_project(this,tag_list);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecylerView.setLayoutManager(horizontalLayoutManager);
        mRecylerView.setAdapter(recyclerAdapter);
        tags = getResources().getStringArray(R.array.Tags);
        TagAdapter = new ArrayAdapter<>(this, R.layout.hint_completion_layout, R.id.tvHintCompletion, tags);
        // TagAdapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, tags);
        autoCompleteTag = findViewById(R.id.create_project_tags_edit);
        autoCompleteTag.setAdapter(TagAdapter);
        autoCompleteTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Call<ArrayList<Tag_Object>> call0= RetrofitClient.getInstance().getApi().getTagbyTitle((String)adapterView.getItemAtPosition(i));
                call0.enqueue(new Callback<ArrayList<Tag_Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Tag_Object>> call0, @NonNull Response<ArrayList<Tag_Object>> response0) {
                        if (response0.isSuccessful()){
                            tag_list.add(new Tag_Object(response0.body().get(0).getId(),response0.body().get(0).getTitle()));
                            Objects.requireNonNull(mRecylerView.getAdapter()).notifyDataSetChanged();
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


            mAuthTask = new saveProjectTask(category, title, description,deadline,budgetMin,budgetMax,tag_list);
            mAuthTask.execute((Void) null);
        }
    }

    public class saveProjectTask extends AsyncTask<Void, Void, Boolean> {

        private final String category,title,description,budgetMin,budgetMax,deadline;
        private final ArrayList<Tag_Object> tags;

        public saveProjectTask(String category, String title, String description, String  deadline, String budgetMin,String budgetMax, ArrayList<Tag_Object> tags) {
            this.category= category;
            this.title = title;
            this.description = description;
            this.deadline = deadline;
            this.budgetMin = budgetMin;
            this.budgetMax = budgetMax;
            this.tags = tags;
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
            final int[] a= new int[1];
            a[0]=Integer.parseInt(category);


            Call<ProjectObject> call= RetrofitClient.getInstance().getApi().projectCreatePost("token "+LoginActivity.getCREDENTIALS()[0],title,description,turnTags_List_to_Integer_List(tag_list),a,Integer.parseInt(budgetMin),Integer.parseInt(budgetMax),deadline);
            call.enqueue(new Callback<ProjectObject>() {
                @Override
                public void onResponse(@NonNull Call<ProjectObject> call, @NonNull Response<ProjectObject> response) {
                    if (response.isSuccessful()){
                        Log.d("MyTag","Geldim");
                        Toast.makeText(CreateProject.this,"SUCCESSFUL EDITTING",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(CreateProject.this, Arrays.toString(turnTags_List_to_Integer_List(tag_list)) +"EMPTY !! "+ Arrays.toString(a),Toast.LENGTH_LONG).show();
                        Log.d("MyTag","DEADLINE :"+deadline);
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

}
