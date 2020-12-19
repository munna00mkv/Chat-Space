package com.example.chatspace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class GroupChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageButton sendMessageButton;
    private EditText userMessageInput;
    //    private ScrollView mScrollView;  // 1. Using scrollView To show data
//    private TextView displayTextMessages; // 2. Text View To Display Data
    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, GroupNameRef, GroupMessageRefKey;
    // Adding recyclerView

    private RecyclerView chatRv;
    private ArrayList<GroupMessage> groupMessageArrayList;
    private GroupChatAdapter groupChatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        currentGroupName = getIntent().getExtras().get("groupName").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid(); // getting current user id
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users"); // Reference to the current user
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName); // Reference to the current group name


        chatRv = findViewById(R.id.chatRv);
        chatRv.setHasFixedSize(true);
        chatRv.setLayoutManager(new LinearLayoutManager(this));

        InitializeFields();
        GetUserInfo(); // Method for getting user info

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveMessageInfoToDatabase(); // Method to save data into firebase
                userMessageInput.setText(""); // when data send to firebase make it null
//                mScrollView.fullScroll(ScrollView.FOCUS_DOWN); // Automatic scroll down on sending message
            }
        });
        groupMessageArrayList = new ArrayList<>();
//        String messageKEY = GroupNameRef.push().getKey();
//        GroupMessageRefKey = GroupNameRef.child(messageKEY);
        GroupNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupMessageArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    GroupMessage model = ds.getValue(GroupMessage.class);
                    groupMessageArrayList.add(model);

                }
                groupChatAdapter = new GroupChatAdapter(GroupChatActivity.this, groupMessageArrayList);
                chatRv.setAdapter(groupChatAdapter);

//                groupChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


//        GroupNameRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.exists()) {
//                    DisplayMessages(snapshot);
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.exists()) {
//                    DisplayMessages(snapshot);
//                }
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    private void InitializeFields() {
        mToolbar = (Toolbar) findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGroupName);

        sendMessageButton = (ImageButton) findViewById(R.id.send_message_button);
        userMessageInput = (EditText) findViewById(R.id.input_group_message);
//        displayTextMessages = (TextView)findViewById(R.id.group_chat_text_display);
//        mScrollView = (ScrollView) findViewById(R.id.my_scroll_view); // Id of scroll View

    }

    private void GetUserInfo() {
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentUserName = snapshot.child("name").getValue().toString(); // getting current user name
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SaveMessageInfoToDatabase() {
        String message = userMessageInput.getText().toString();
        String messageKEY = GroupNameRef.push().getKey(); // Reference to the Generated unique key
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please Write message first.", Toast.LENGTH_SHORT).show();
        } else {
            // Getting current date
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM d, ''yy");
            currentDate = currentDateFormat.format(calForDate.getTime());
            // Getting current time
            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("h:mm a");
            currentTime = currentTimeFormat.format(calForTime.getTime());
            // use hash map to update children
            HashMap<String, Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey); // Group name key changed each time

            GroupMessageRefKey = GroupNameRef.child(messageKEY); // Reference to the unique key which is generated by group user
            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", currentUserName);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);
            GroupMessageRefKey.updateChildren(messageInfoMap);


        }
    }
}

//    private void DisplayMessages(DataSnapshot snapshot) {
//        groupMessageArrayList.clear();


//}

//        Iterator iterator = snapshot.getChildren().iterator();
//        while(iterator.hasNext()){
//            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
//            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
//            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
//            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();
//            displayTextMessages.append(chatName + " :\n" + chatMessage + "\n" + chatTime + "    " + chatDate + "\n\n\n");
//            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
//
//
        //        }
//    }
