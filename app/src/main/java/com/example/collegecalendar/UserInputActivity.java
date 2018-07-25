package com.example.collegecalendar;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserInputActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner daySpinner;
    EditText etClassName;
    EditText etClassNumber;
    TimePicker startTime;
    TimePicker endTime;
    Button bEnter;
    Button bColor;
    FirebaseFirestore db;
    FirebaseAuth auth;
    int colorPos;
    String color;
    String[] colors = {"#33ccff","#AC58FA","#01DF3A","#5C88F7","#FFBF00","#1DE9B6","#FA5858","#F4FA58"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);


        daySpinner = findViewById(R.id.daySpinner);
        etClassName = findViewById(R.id.etClassName);
        etClassNumber = findViewById(R.id.etClassNumber);
        startTime = findViewById(R.id.startPicker);
        endTime = findViewById(R.id.endPicker);
        bEnter = findViewById(R.id.bEnter);
        bColor = findViewById(R.id.bColor);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        bEnter.setOnClickListener(this);
        bColor.setOnClickListener(this);

        bColor.setBackgroundColor(Color.parseColor(colors[colors.length-1]));
        color = colors[colors.length-1];

    }

    private void dataEntry(){
        int startHour = startTime.getCurrentHour();
        int startMin = startTime.getCurrentMinute();
        int endHour = endTime.getCurrentHour();
        int endMin = endTime.getCurrentMinute();

        String startTime = startHour + ":" + startMin;
        String endTime = endHour + ":" + endMin;

        int start = (startHour * 60) + startMin;
        int end = (endHour * 60) + endMin;

        if(start < 480 || end < 480 || start >= 1380 || end >=1380){
            Toast.makeText(this, "Time not in boundaries", Toast.LENGTH_SHORT).show();
        }
        else if(end < start ) {
            Toast.makeText(this, "End time is before start time", Toast.LENGTH_SHORT).show();
        }
        else if(start == end){
            Toast.makeText(this, "Times are the same.", Toast.LENGTH_SHORT).show();
        }
        else {
            Map<String, String> eventInfo = new HashMap<>();
            eventInfo.put("day", daySpinner.getSelectedItem().toString());
            eventInfo.put("className", etClassName.getText().toString().trim());
            eventInfo.put("classNumber", etClassNumber.getText().toString().trim());
            eventInfo.put("startTime", startTime);
            eventInfo.put("endTime", endTime);
            eventInfo.put("user", auth.getUid());
            eventInfo.put("color", color);
            db.collection("Events").add(eventInfo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(UserInputActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserInputActivity.this, "Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    @Override
    public void onClick(View v) {
        if(v == bEnter){
            dataEntry();
        }
        if(v == bColor){
            bColor.setBackgroundColor(Color.parseColor(colors[colorPos]));
            color = colors[colorPos];
            colorPos++;
            if(colorPos > colors.length - 1){
                colorPos = 0;
            }
        }
    }


}
