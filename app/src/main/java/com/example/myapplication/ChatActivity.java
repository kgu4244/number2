package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String[] myDataset = {"hello", "today", "what", "movie"};

    EditText etText;
    Button btnSend;
    String email;
    InputMethodManager imm;

    FirebaseDatabase database;
    List<Chat> mChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("Secret Chating App");

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            email = user.getEmail();
        }


        etText = (EditText) findViewById(R.id.etText);
        etText.bringToFront();
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String stText = etText.getText().toString();
                if(stText.equals("") || stText.isEmpty())
                {
                    Toast.makeText(ChatActivity.this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChatActivity.this, email+","+stText, Toast.LENGTH_SHORT).show();

                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                    String formattedDate = df.format(c.getTime());


                     DatabaseReference myRef = database.getReference("chats").child(formattedDate);

                    Hashtable<String, String> chat = new Hashtable<String, String>();
                    chat.put("email", email);
                    //암호화 하고 싶으면 stText 암호화 하기
                    String base64Str="";
                    byte[] base64Data = stText.getBytes();
                    try{
                        base64Str = base64.encrytBASE64(base64Data);
                    }catch(Exception a){

                    }
                    stText = base64Str.toString();
                    chat.put("text", stText);
                    myRef.setValue(chat);
                    etText.setText(null);

                }

                imm.hideSoftInputFromWindow(etText.getWindowToken(),0);
            }
        });

        Button btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mChats = new ArrayList<>();
        mAdapter = new MyAdapter(mChats, email, ChatActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        DatabaseReference myRef = database.getReference("chats");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Chat chat = dataSnapshot.getValue(Chat.class);

                //복호화
                String base64Str = chat.text;
                byte[] base64Byte=null;
                try {
                    base64Byte = base64.decrytBASE64(base64Str);

                }catch (Exception a){

                }
                String output = new String(base64Byte);
                chat.text = output;

                mChats.add(chat);
                mAdapter.notifyItemInserted(mChats.size() - 1);
                mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);

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
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
