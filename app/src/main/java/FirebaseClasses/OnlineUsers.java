package FirebaseClasses;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import RetrofitModels.User;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.Activities.ChatActivity;
import honeybadgersapp.honeybadgers.Activities.LoginActivity;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineUsers extends AppCompatActivity {
        ListView listView;
        public  ArrayList<String> my_conversations = new ArrayList<>();
    public  ArrayList<String> my_conversations_ids = new ArrayList<>();
        public static ArrayAdapter<String> mAdapter = null;
        Firebase firebase_onlineusers = new Firebase("https://honeybadgers-12976.firebaseio.com").child("Conversations").child(""+ LoginActivity.getCREDENTIALS()[4]);
        private int SIGN_IN_REQUEST_CODE = 99;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            get_My_Conversations();
            setContentView(R.layout.activity_online_users);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            listView = findViewById(R.id.active_users_list_view);
            Firebase.setAndroidContext(this);

            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,my_conversations);
            listView.setAdapter(mAdapter);

            FirebaseDatabase database = FirebaseDatabase.getInstance();


            if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                // Start sign in/sign up activity
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .build(),
                        SIGN_IN_REQUEST_CODE
                );
            } else {
                // User is already signed in. Therefore, display
                // a welcome Toast
                Toast.makeText(this,
                        "Welcome " + FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),
                        Toast.LENGTH_LONG)
                        .show();

                // Load chat rooms
                ChatActivity.from_user=""+LoginActivity.getCREDENTIALS()[4];
                Toast.makeText(OnlineUsers.this, "You are Online", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected void onStart() {
            super.onStart();
            onListItemClick();
        }

        private void onListItemClick() {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Call<List<User>> call2 = RetrofitClient.getInstance().getApi().user_id(my_conversations.get(i));
                    call2.enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call2, Response<List<User>> response2) {
                            User editResponse2 = response2.body().get(0);
                            if (response2.isSuccessful()) {
                                ChatActivity.to_user=""+editResponse2.getId();
                                startActivity(new Intent(OnlineUsers.this, ChatActivity.class));
                            }
                        }
                        @Override
                        public void onFailure(Call<List<User>> call2, Throwable t) {
                          }
                    });

                }

            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    new android.support.v7.app.AlertDialog.Builder(OnlineUsers.this)
                            .setTitle("Are you sure?")
                            .setMessage("Deleting this thread will not erase the messages you sent or received. Only your access to those messages will be gone unless you start a new conversation with that user again.")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    firebase_onlineusers.child(my_conversations_ids.get(i)).equalTo(my_conversations.get(i)).getRef().removeValue();
                                    my_conversations.remove(i);
                                    my_conversations_ids.remove(i);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }).create().show();
                return true;
                }
            });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {

                firebase_onlineusers.push().setValue(LoginActivity.getCREDENTIALS()[1]);

                ChatActivity.from_user=""+LoginActivity.getCREDENTIALS()[4];
                Toast.makeText(OnlineUsers.this, "You are Online", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void get_My_Conversations(){
        Firebase firebase_register;
        firebase_register = new Firebase("https://honeybadgers-12976.firebaseio.com/Conversations").child(LoginActivity.getCREDENTIALS()[4]);
        firebase_register.addChildEventListener(new ChildEventListener() {
            @Override
            public synchronized void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                s = dataSnapshot.getValue(String.class);
                my_conversations.add(s);
                my_conversations_ids.add(dataSnapshot.getKey());
                if(OnlineUsers.mAdapter!=null){
                    OnlineUsers.mAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {
                if(OnlineUsers.mAdapter!=null){
                    OnlineUsers.mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
