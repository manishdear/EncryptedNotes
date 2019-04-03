package com.wordpress.uniquecoder.encryptednotes;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {


    EditText mailid;
    EditText pd;
    EditText username;
    EditText confirmPassword;
    ProgressBar progressBar;
    EditText gstin;

    private FirebaseAuth mAuth;
    DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        databaseUser = FirebaseDatabase.getInstance().getReference("user");

        mailid = (EditText)findViewById(R.id.etemail);
        pd = (EditText)findViewById(R.id.etPassword);
        username = (EditText)findViewById(R.id.etusername);
        confirmPassword = (EditText)findViewById(R.id.etconfirmpassword);
        gstin = (EditText)findViewById(R.id.etgstin);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        findViewById(R.id.btnsignup).setOnClickListener(this);
        findViewById(R.id.tvloginlink).setOnClickListener(this);
    }


    private void addUser(){
        String name = username.getText().toString().trim();
        String email = mailid.getText().toString().trim();
        String gstinId = gstin.getText().toString().trim();

        if(!gstinId.isEmpty()){
            userProfile user = new userProfile(gstinId,name,email);
            databaseUser.child(gstinId).setValue(user);
            Toast.makeText(this,"Your data saved",Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser() {
        String email = mailid.getText().toString().trim();
        String password = pd.getText().toString().trim();
        String name = username.getText().toString().trim();
        String gst = gstin.getText().toString().trim();
        String cP = confirmPassword.getText().toString().trim();
        String signupId = gst+"@gmail.com";

        if(name.isEmpty()){
            username.setError("user name is required");
            username.requestFocus();
            return;
        }

        if(gst.isEmpty()){
            gstin.setError("GSTIN is requied");
            gstin.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            mailid.setError("Email is required");
            mailid.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mailid.setError("Please enter a valid email");
            mailid.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            pd.setError("Password is required");
            pd.requestFocus();
            return;
        }

        if(!password.equals(cP)){
            confirmPassword.setError("Password not match");
            confirmPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            pd.setError("Minimum lenght of password should be 6");
            pd.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        PreferenceManager.getDefaultSharedPreferences(SignupActivity.this).edit().putString("gstid", gst).apply();

        mAuth.createUserWithEmailAndPassword(signupId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    addUser();
                    finish();
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                }
                else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(),"failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnsignup:
                registerUser();
                break;

            case R.id.tvloginlink:
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }
}
