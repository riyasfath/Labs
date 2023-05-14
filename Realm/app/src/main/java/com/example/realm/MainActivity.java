package com.example.realm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    EditText Cname, Cduration , Ctrack , Cdesc ;
    Button Add,display;
    Realm realm;
    String courseName, courseDuration, courseDescription, courseTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm= Realm.getDefaultInstance();
        Cname = findViewById(R.id.textView6);
        Cduration = findViewById(R.id.textView7);
        Ctrack = findViewById(R.id.textView8);
        Cdesc = findViewById(R.id.textView9);
        Add = findViewById(R.id.button);
        display = findViewById(R.id.button2);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseName = Cname.getText().toString();
                courseDescription = Cdesc.getText().toString();
                courseDuration = Cduration.getText().toString();
                courseTracks = Ctrack.getText().toString();
                if (TextUtils.isEmpty(courseName)) {
                    Cname.setError("Please enter Course Name");
                } else if (TextUtils.isEmpty(courseDescription)) {
                    Cdesc.setError("Please enter Course Description");
                } else if (TextUtils.isEmpty(courseDuration)) {
                    Cduration.setError("Please enter Course Duration");
                } else if (TextUtils.isEmpty(courseTracks)) {
                    Ctrack.setError("Please enter Course Tracks");
                } else {
                    addDataToDatabase(courseName, courseDescription, courseDuration, courseTracks);
                    Toast.makeText(MainActivity.this, "Course Added To Database", Toast.LENGTH_SHORT).show();
                    Cname.setText("");
                    Cdesc.setText("");
                    Cduration.setText("");
                    Ctrack.setText("");
                
                }
            }
        });
    }
    private void addDataToDatabase(String courseName, String courseDescription, String courseDuration, String courseTracks) {
        DataModel model = new DataModel();
        Number id = realm.where(DataModel.class).max("id");
        long nextId;
        if (id == null) {
            nextId = 1;
        } else {
            nextId = id.intValue() + 1;
        }

        model.setId(nextId);
        model.setCourseDescription(courseDescription);
        model.setCourseName(courseName);
        model.setCourseDuration(courseDuration);
        model.setCourseTracks(courseTracks);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.copyToRealm(model);
            }
        });
    }
}