package honeybadgersapp.honeybadgers.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import Models.Message_Object;
import honeybadgersapp.honeybadgers.R;

public class ChatActivity extends AppCompatActivity{
    private FirebaseListAdapter<Message_Object> adapter;
    private  ListView listOfMessages;

    Firebase firebase_chatnode ;
    public static String from_user;
    public static String to_user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Prevent keyboard from automatically opening
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Firebase.setAndroidContext(this);
        // Write a message to the database
        final Firebase firebase_register;
        firebase_register = new Firebase("https://honeybadgers-12976.firebaseio.com/Conversations").child(to_user);
        int a = Integer.parseInt(from_user);
        int b= Integer.parseInt(to_user);

        if(a<b){
            firebase_chatnode = new Firebase("https://honeybadgers-12976.firebaseio.com/Chats/"+from_user+"_"+to_user);
        }else if(a>b){
            firebase_chatnode = new Firebase("https://honeybadgers-12976.firebaseio.com/Chats/"+to_user+"_"+from_user);
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.input);


                firebase_register.child(""+LoginActivity.getCREDENTIALS()[4]).setValue(LoginActivity.getCREDENTIALS()[1]);


                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                firebase_chatnode
                        .push()
                        .setValue(new Message_Object(input.getText().toString(),
                                LoginActivity.getCREDENTIALS()[2])
                        );

                // Clear the input
                input.setText("");
            }

        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        displayChatMessages();
    }

    private void displayChatMessages() {
        listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        int a = Integer.parseInt(from_user);
        int b= Integer.parseInt(to_user);
        String temp="";

        if(a<b){
            temp=from_user+"_"+to_user;
        }else if(a>b){
            temp=to_user+"_"+from_user;
        }
        adapter = new FirebaseListAdapter<Message_Object>(this, Message_Object.class,
                R.layout.message_object, FirebaseDatabase.getInstance().getReference().child("Chats").child(temp)) {
            @Override
            protected void populateView(View v, Message_Object model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

/*import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;

import FirebaseClasses.ChatMessageViewHolder;
import Models.Message_Object;
import honeybadgersapp.honeybadgers.R;

public class ChatActivity extends AppCompatActivity {

    EditText editText;
    private FirebaseListAdapter<Message_Object> adapter;
    ArrayList<Message_Object> chatmsgsList = new ArrayList<Message_Object>();
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Message_Object, ChatMessageViewHolder> mFirebaseAdapter1 = null;
    private FirebaseRecyclerAdapter<Message_Object, ChatMessageViewHolder> mFirebaseAdapter2 = null;
    Firebase firebase_chatnode = new Firebase("https://honeybadgers-12976.firebaseio.com/Chats");
    Firebase ref_chatchildnode1 = null;
    Firebase ref_chatchildnode2 = null;
    String from_user, to_user, newmsg, LoggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Firebase.setAndroidContext(this);


        editText = (EditText) findViewById(R.id.input);

        Intent startingintent = getIntent();
        mRecyclerView =  findViewById(R.id.list_of_messages);
        RecyclerView.LayoutManager layoutmgr = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutmgr);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        from_user = startingintent.getStringExtra("FROM_USER");
        to_user = startingintent.getStringExtra("TO_USER");
        LoggedInUser = startingintent.getStringExtra("LOG_IN_USER");
        setTitle(to_user);
        Log.v("NODE CREATED:", from_user + " " + to_user);

        ref_chatchildnode1 = firebase_chatnode.child(from_user + "_" + to_user);
        ref_chatchildnode2 = firebase_chatnode.child(to_user + "_"+ from_user);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newmsg = editText.getText().toString().trim();
                Message_Object m = new Message_Object();
                m.setMessageUser(LoggedInUser); ;
                m.setMessageText(newmsg);
                ref_chatchildnode1.push().setValue(m);
                ref_chatchildnode2.push().setValue(m);
                editText.setText("");
            }
        });

        ref_chatchildnode1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message_Object chatmsg = dataSnapshot.getValue(Message_Object.class);
                chatmsgsList.add(chatmsg);
                mFirebaseAdapter1.notifyDataSetChanged();
                mFirebaseAdapter2.notifyDataSetChanged();
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
        ref_chatchildnode2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message_Object chatmsg = dataSnapshot.getValue(Message_Object.class);
                chatmsgsList.add(chatmsg);
                mFirebaseAdapter1.notifyDataSetChanged();
                mFirebaseAdapter2.notifyDataSetChanged();
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


    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter1 = new FirebaseRecyclerAdapter<Message_Object, ChatMessageViewHolder>(Message_Object.class,
                R.layout.message_object,
                ChatMessageViewHolder.class,
                ref_chatchildnode1) {
            @Override
            public void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, Message_Object m, int i) {

                chatMessageViewHolder.sender.setText(m.getMessageUser());
                chatMessageViewHolder.msg.setText(m.getMessageText());
            }
        };
        mFirebaseAdapter2 = new FirebaseRecyclerAdapter<Message_Object, ChatMessageViewHolder>(Message_Object.class,
                R.layout.message_object,
                ChatMessageViewHolder.class,
                ref_chatchildnode2) {
            @Override
            public void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, Message_Object m, int i) {


                chatMessageViewHolder.sender.setText(m.getMessageUser());
                chatMessageViewHolder.msg.setText(m.getMessageText());
            }
        };

        mRecyclerView.setAdapter(mFirebaseAdapter1);

        mRecyclerView.setAdapter(mFirebaseAdapter2);

    }

}*/
