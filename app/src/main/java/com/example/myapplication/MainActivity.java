package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.Hashtable;

public  class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    EditText etEmail;
    EditText etPassword;
    String stEmail;
    String email;
    String uid;
    String stPassword;
    ProgressBar pbLogin;
    FirebaseDatabase database;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Secret Chating App");

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    SharedPreferences sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", user.getUid());
                    editor.putString("email", user.getEmail());
                    editor.apply();
                    user.getEmail();


                }
            }


        };
        Button btncancel = (Button) findViewById(R.id.btnCancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = etEmail.getText().toString();
                stPassword = etPassword.getText().toString();

                if(stEmail.isEmpty() || stEmail.equals("") || stPassword.isEmpty() || stPassword.equals(""))
                {
                    Toast.makeText(MainActivity.this, "입력해 주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, stEmail+","+stPassword, Toast.LENGTH_SHORT).show();
                    registerUser(stEmail, stPassword);
                    database = FirebaseDatabase.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user != null)
                    {
                        email = user.getEmail();
                        uid = user.getUid();

                    }
                    DatabaseReference myRef = database.getReference("users").child(uid);
                    Hashtable<String, String> users = new Hashtable<String, String>();
                    users.put("email", email);
                    users.put("uid", uid);
                    users.put("pwd", stPassword);
                    myRef.setValue(users);



                }

            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = etEmail.getText().toString();
                stPassword = etPassword.getText().toString();


                if(stEmail.isEmpty() || stEmail.equals("") || stPassword.isEmpty() || stPassword.equals(""))
                {
                    Toast.makeText(MainActivity.this, "입력해 주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    userLogin(stEmail,stPassword);

                    etEmail.setText(null);
                    etPassword.setText(null);
                }

            }
        });
    }


    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop(){
        super.onStop();
        if(mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }
    public void registerUser(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete"+task.isSuccessful());
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(MainActivity.this, "Authentication sucess",Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }

                });
    }
    private void userLogin(String email, String password){
        pbLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        pbLogin.setVisibility(View.GONE);

                        if(task.isSuccessful()) {
                            Log.d(TAG, "signinWithEmail:onComplete:" + task.isSuccessful());
                            //Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            //Intent intent = new Intent(MainActivity.this, TapActivity.class);
                            Intent intent = new Intent(MainActivity.this, testpage.class);
                            startActivity(intent);
                        }
                        else if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });


    }
}
