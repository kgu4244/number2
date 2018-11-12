package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

public class testpage extends AppCompatActivity {


    String email;
    String uid;
    FirebaseDatabase database;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testpage);

        Button goChat = (Button) findViewById(R.id.goChat);
        goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(testpage.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        Button userlist = (Button) findViewById(R.id.UserList);
        userlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(testpage.this, FriendsActivity.class);
                startActivity(intent2);
            }
        });

        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            email = user.getEmail();
            uid = user.getUid();

        }
        Button RegistToList = (Button) findViewById(R.id.RegisttoList);
        RegistToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference myRef = database.getReference("users").child(uid);
                Hashtable<String, String> users = new Hashtable<String, String>();
                users.put("email", email);
                users.put("uid", uid);
                myRef.setValue(users);
                Toast.makeText(testpage.this, "저장되었습니다", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
