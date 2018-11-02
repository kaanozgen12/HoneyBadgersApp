package FirebaseClasses;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import honeybadgersapp.honeybadgers.Activities.ChatActivity;
import honeybadgersapp.honeybadgers.R;

public class OnlineUsers extends AppCompatActivity {
        ListView listView;
        ArrayAdapter<String> mAdapter = null;
        ArrayList<String> usersList = new ArrayList<String>();
        Firebase firebase_onlineusers = new Firebase("https://honeybadgers-12976.firebaseio.com").child("Conversations").child(""+FirebaseAuth.getInstance().getCurrentUser().getEmail().toString().hashCode());
        private int SIGN_IN_REQUEST_CODE = 99;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_online_users);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            listView = findViewById(R.id.active_users_list_view);
            Firebase.setAndroidContext(this);

            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usersList);
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
                ChatActivity.from_user=""+FirebaseAuth.getInstance().getCurrentUser().getEmail().hashCode();
                Toast.makeText(OnlineUsers.this, "You are Online", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected void onStart() {
            super.onStart();
            usersList.clear();
            firebase_onlineusers.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    s = dataSnapshot.getValue(String.class);
                    Log.v("VALUE", s);
                    if(!s.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        usersList.add(s);
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            onListItemClick();
        }

        private void onListItemClick() {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ChatActivity.to_user=""+usersList.get(i).hashCode();
                    startActivity(new Intent(OnlineUsers.this, ChatActivity.class));
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
                                    firebase_onlineusers.equalTo(usersList.get(i)).getRef().removeValue();
                                    usersList.remove(i);
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

                firebase_onlineusers.push().setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                ChatActivity.from_user=""+FirebaseAuth.getInstance().getCurrentUser().getEmail().hashCode();
                Toast.makeText(OnlineUsers.this, "You are Online", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
