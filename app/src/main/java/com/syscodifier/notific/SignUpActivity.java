package com.syscodifier.notific;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SignUpActivity extends AppCompatActivity {

    Button SignUpBtn, LoginBtn;
    EditText username, password;
//    TextView errortv;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SignUpBtn = findViewById(R.id.signupbtn);
        LoginBtn = findViewById(R.id.loginbtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();



        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_email = username.getText().toString(), passwd = password.getText().toString();

                mAuth.createUserWithEmailAndPassword(user_email, passwd)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("my_app", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null){
                                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                if(task.isSuccessful()){
                                                    String email = mAuth.getCurrentUser().getEmail();
                                                    String token = task.getResult().getToken().toString();
                                                    User user_obj = new User(email, task.getResult().getToken().toString());

                                                    DatabaseReference dbuser = FirebaseDatabase.getInstance().getReference("user");
                                                    dbuser.child(mAuth.getCurrentUser().getUid()).setValue(user_obj)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Toast.makeText(SignUpActivity.this, "Added token", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });

                                                } else {
                                                    Log.d("my_app", "Failed");
                                                }
                                            }
                                        });
                                        ChangeIntent();
                                    }
//                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if(!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch(FirebaseAuthWeakPasswordException e) {
                                            Toast.makeText(getApplicationContext(), "Weak Password", Toast.LENGTH_LONG).show();
                                        } catch(FirebaseAuthInvalidCredentialsException e) {
                                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                                        } catch(FirebaseAuthUserCollisionException e) {
                                            Toast.makeText(getApplicationContext(), "User Already Exist", Toast.LENGTH_LONG).show();
                                        } catch(Exception e) {
                                            Log.e("my_app", e.getMessage());
                                        }
                                    }
                                }

                                // ...
                            }
                        });


            }
        });



        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_email = username.getText().toString(), passwd = password.getText().toString();

                mAuth.signInWithEmailAndPassword(user_email, passwd)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("my_app", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null){
                                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                if(task.isSuccessful()){
                                                    String email = mAuth.getCurrentUser().getEmail();
                                                    String token = task.getResult().getToken().toString();
                                                    User user_obj = new User(email, task.getResult().getToken().toString());

                                                    DatabaseReference dbuser = FirebaseDatabase.getInstance().getReference("user");
                                                    dbuser.child(mAuth.getCurrentUser().getUid()).setValue(user_obj)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Toast.makeText(SignUpActivity.this, "Added token", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });

                                                } else {
                                                    Log.d("my_app", "Failed");
                                                }
                                            }
                                        });
                                        ChangeIntent();
                                    }
                                    //                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("my_app", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
//                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });


    }

    public void ChangeIntent(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
