package com.example.collegecalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etName;
    private Button bRegister;
    private TextView tvLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        bRegister = findViewById(R.id.bRegister);
        tvLogin = findViewById(R.id.tvLogin);

        //listens for a user click
        bRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        //VERY IMPORTANT. NEED THIS TO MAKE the registerUser firebase section work.
        mAuth = FirebaseAuth.getInstance();
        users = FirebaseFirestore.getInstance();

    }

    /*
    Method that controls what happens when bRegister is clicked.
     */
    private void registerUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();

        //TextUtils needed for null pointer exception

        //need to display messages for empty errors as well as other errors.
        if (TextUtils.isEmpty(email)&& TextUtils.isEmpty(password) && TextUtils.isEmpty(name)){
            Toast.makeText(this, "All fields are empty", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Name field is empty", Toast.LENGTH_SHORT).show();

        }
        //this is an error message if the email is not valid
        else if(password.length() < 6){
            Toast.makeText(this, "Password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
        }
        //if there are no empty fields or other errors then it will progress
        else {
            //Firebase method that creates user and then listens to when action is complete
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //message to let user know it was successful
                                Toast.makeText(RegisterActivity.this, "Successful!", Toast.LENGTH_SHORT).show();

                                //map of the userInfo that should be entered into the firebase firestore
                                Map<String,String> userInfo = new HashMap<>();
                                userInfo.put("email",etEmail.getText().toString().trim());
                                userInfo.put("name",etName.getText().toString().trim());

                                //putting the information into the collection with userid being doc.
                                users.collection("users").document(mAuth.getCurrentUser().getUid()).set(userInfo);


                                //after registration is complete, need to move to MainActivity which acts as a hub
                                 Intent regToMain = new Intent(RegisterActivity.this, MainActivity.class);
                                 RegisterActivity.this.startActivity(regToMain);
                                 //stops user from returning to this activity using back button
                                 finish();

                            } else {
                                //message to know attempt was fail. Firebase should deal with duplicates??
                                Toast.makeText(RegisterActivity.this, "Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        //Method finishes and will run again when button is clicked.

    }


    //what happens when either bRegister or tvLogin is clicked
    @Override
    public void onClick(View v) {
        if(v == bRegister){
            //void method that registers the user to firebase and if successful goes to MainActivity
            registerUser();
        }

        if(v == tvLogin){
            //goes to the Login Activity
            Intent regToLogin = new Intent(RegisterActivity.this,LoginActivity.class);
            RegisterActivity.this.startActivity(regToLogin);
        }
    }


}
