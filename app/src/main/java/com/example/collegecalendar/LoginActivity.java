package com.example.collegecalendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etEmail;
    private EditText etPassword;
    private Button bLogin;
    private TextView tvRegister;

    private FirebaseAuth loginAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        bLogin = findViewById(R.id.bLogin);
        tvRegister = findViewById(R.id.tvRegister);

        //need onclicklisteners
        bLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        //important to make firebase work
        loginAuth = FirebaseAuth.getInstance();

    }

    /*
    Method that controls what happens when bLogin is clicked.
     */
    private void loginUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        //TextUtils needed for null pointer exception

        //need to display messages for empty errors
        if (TextUtils.isEmpty(email)&& TextUtils.isEmpty(password)){
            Toast.makeText(this, "Both fields are empty", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show();

        }
        /*
        *no need to check for other errors since it wont work if it doesnt exist in firebase auth
        * already covered in RegisterActivity.
        */
        else{
            loginAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        loginAuth.getCurrentUser();
                        //task is successful. Move to MainActivity and let user know it was successful
                        Toast.makeText(LoginActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                        Intent logToMain = new Intent(LoginActivity.this,MainActivity.class);
                        LoginActivity.this.startActivity(logToMain);
                        //Stops users from hitting back button and going to login
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Not Valid. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //Method is done
    }


    @Override
    public void onClick(View v) {
        if(v==bLogin){
            //method that logs in the user and moves them to the main method + closes this activity
            loginUser();

        }
        if(v==tvRegister){
            //moves to the register activity.
            Intent logToReg = new Intent(LoginActivity.this,RegisterActivity.class);
            LoginActivity.this.startActivity(logToReg);
        }

    }
}
