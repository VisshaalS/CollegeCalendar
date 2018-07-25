package com.example.collegecalendar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static android.graphics.Color.GREEN;

public class CalendarActivity extends AppCompatActivity  {

    FirebaseFirestore db;
    RelativeLayout relative;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        relative = findViewById(R.id.relative);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();



        //from simplified coding tutorial

        db.collection("Events").whereEqualTo("user", auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d: list){
                        String day = d.getString("day");
                        String className = d.getString("className");
                        String classNumber = d.getString("classNumber");
                        String startTime = d.getString("startTime");
                        String endTime = d.getString("endTime");
                        String color = d.getString("color");
                        //gets the id of the document in order to delete it later on
                        String docId = d.getId();

                        createButton(day,className,classNumber, startTime,endTime,docId,color);


                    }

                }
            }
        });

    }

    private void createButton(String day,String className,String classNumber,String startTime,String endTime,final String docId, String color) {

        // x value for setX determined by the inputted day
        int xPos = 0;

        if(day.equals("Sunday")) {
            xPos = 51;
        }
        else if(day.equals("Monday")) {
            xPos = 127;
        }
        else if(day.equals("Tuesday")) {
            xPos = 203;
        }
        else if (day.equals("Wednesday")) {
            xPos = 279;
        }
        else if (day.equals("Thursday")) {
            xPos = 355;
        }
        else if(day.equals("Friday")) {
            xPos = 431;
        }
        else if(day.equals("Saturday")) {
            xPos = 507;
        }


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
        float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,xPos,dm);
        float y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,start,dm);
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, dm);
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,heightVal,dm);


        //creating the button
        Button create = new Button(this);
        create.setX(x);
        create.setY(y);
        create.setText(className + "\n" + classNumber);
        create.setBackgroundColor(Color.parseColor(color));
        create.setLayoutParams(new RelativeLayout.LayoutParams(Math.round(width),Math.round(height)));
        relative.addView(create);

        //this is to delete the button and the data in firebase just in case user makes a mistake.

        //have to declare strings that are final in order to be in the scope of the onclicklistener
        create.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //removes the button
             relative.removeView(v);
             //deletes the document from firestore.
             db.collection("Events").document(docId).delete();
                return true;
            }
        });


    }
}