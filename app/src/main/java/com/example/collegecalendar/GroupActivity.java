package com.example.collegecalendar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    RelativeLayout relative;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        relative  = findViewById(R.id.relative);


        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> userList = queryDocumentSnapshots.getDocuments();
                    int xPos = -24;
                    int linePos = 50;
                    for (DocumentSnapshot user : userList) {
                        String docId = user.getId();
                        String userName = user.getString("name");
                        xPos += 75;
                        linePos += 75;
                        userHeaders(userName,docId,xPos,linePos);

                    }
                }
            }
        });

    }

    private void userHeaders(String userName, String docId,final int xPos, int linePos){

        //making DisplayMetrics for dp conversion
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, dm);
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, dm);
        float lineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,dm);
        float lineHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 930, dm);
        float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,xPos,dm);
        float lineX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,linePos,dm);

        //created the textView for the headers
        TextView create = new TextView(this);
        create.setText(userName);
        create.setX(x);
        create.setLayoutParams(new LinearLayout.LayoutParams(Math.round(width),Math.round(height)));
        create.setTypeface(null,Typeface.BOLD);


        //vertical linear layout created for each header in order to add data since cant get positions.
        relative.addView(create);


        db.collection("Events").whereEqualTo("user",docId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> eventList = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot events : eventList){
                        String day = events.getString("day");
                        String className = events.getString("className");
                        String classNumber = events.getString("classNumber");
                        String startTime = events.getString("startTime");
                        String endTime = events.getString("endTime");
                        String color = events.getString("color");

                       //getting the current day.
                        Calendar sCalendar = Calendar.getInstance();
                        String currentDay = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

                        if(day.equals(currentDay)) {
                            Button but = createButton(className, classNumber, startTime, endTime,color);
                            //maybe add the setX() here...
                            DisplayMetrics dm = getResources().getDisplayMetrics();
                            float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, xPos, dm);
                            but.setX(x);

                            //adds the buttons to the relative view.
                            relative.addView(but);
                        }


                    }
                }
            }
        });


        //now need a line separator in between going all the way down. Need relative view for this
        View line = new View(this);
        line.setLayoutParams(new RelativeLayout.LayoutParams(Math.round(lineWidth),Math.round(lineHeight)));
        line.setBackgroundColor(Color.BLACK);
        line.setX(lineX);
        relative.addView(line);

    }

    private Button createButton(String className, String classNumber, String startTime, String endTime,String color) {


        // startTime calculation which will be used for the set Y.
        int startHour = Integer.parseInt((startTime.substring(0,startTime.indexOf(":"))));
        int startMin = Integer.parseInt(startTime.substring(startTime.indexOf(":") + 1,startTime.length()));

        startHour = (startHour - 8) * 60;
        //need to add 30 because of the header. start will be the variable for setY
        int start = startHour + startMin + 30;

        // endTime calculation in order to determine height of the event.
        int endHour = Integer.parseInt((endTime.substring(0,endTime.indexOf(":"))));
        int endMin = Integer.parseInt(endTime.substring(endTime.indexOf(":") + 1,endTime.length()));

        endHour = (endHour -8) * 60;

        int end = endHour + endMin + 30;

        int heightVal = end - start;

        //making display metrics in order to convert pixels to dp
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,start,dm);
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, dm);
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,heightVal,dm);


        //creating the button
        Button but = new Button(this);
        but.setY(y);
        but.setText(className + "\n" + classNumber);
        but.setBackgroundColor(Color.parseColor(color));
        but.setLayoutParams(new LinearLayout.LayoutParams(Math.round(width), Math.round(height)));

        return but;

    }


}
