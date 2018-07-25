package com.example.collegecalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button bSignOut;
    Button bCalendar;
    Button bInput;
    Button bGroup;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bSignOut = findViewById(R.id.bSignOut);
        bCalendar = findViewById(R.id.bCalendar);
        bInput = findViewById(R.id.bInput);
        bGroup = findViewById(R.id.bGroup);

        bSignOut.setOnClickListener(this);
        bCalendar.setOnClickListener(this);
        bInput.setOnClickListener(this);
        bGroup.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        //bSignOut signs out the user from the application
        if(v == bSignOut){
            mAuth.signOut();
            Intent mainToLogin = new Intent(MainActivity.this,LoginActivity.class);
            MainActivity.this.startActivity(mainToLogin);
            finish();
        }
        //bCalendar allows the user to see their personal calendar. Goes to CalendarActivity
        if(v==bCalendar){
            Intent mainToCalendar = new Intent(MainActivity.this,CalendarActivity.class);
            MainActivity.this.startActivity(mainToCalendar);
        }
        //bInput allows the user to input an event. Goes to UserInputActivity.
        if(v==bInput){
            Intent mainToInput = new Intent(MainActivity.this,UserInputActivity.class);
            MainActivity.this.startActivity(mainToInput);
        }
        if(v==bGroup){
            Intent mainToGroup = new Intent(MainActivity.this,GroupActivity.class);
            MainActivity.this.startActivity(mainToGroup);
        }
    }
}
