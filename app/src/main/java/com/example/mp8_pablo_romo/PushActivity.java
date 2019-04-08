package com.example.mp8_pablo_romo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PushActivity extends AppCompatActivity {

    FirebaseDatabase fbdb;
    DatabaseReference dbrf;

    // All of the EditTexts
    EditText course;
    EditText name;
    EditText mark;

    RadioGroup kids;

    int kid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        fbdb = FirebaseDatabase.getInstance();
        dbrf = fbdb.getReference();
    }


    public void gobacktopull(View view) {
        // Check to make sure that the student has been chosen and then run pushit

        RadioButton b = findViewById(R.id.radio_Bart);
        RadioButton r = findViewById(R.id.radio_Ralph);
        RadioButton m = findViewById(R.id.radio_Milhouse);
        RadioButton l = findViewById(R.id.radio_Lisa);

        if(b.isChecked()) {
            kid = 123;
            pushit();
        } else if(r.isChecked()) {
            kid = 404;
            pushit();
        } else if(m.isChecked()) {
            kid = 456;
            pushit();
        } else if(l.isChecked()) {
            kid = 888;
            pushit();
        } else {
            kid = 0;
            Toast.makeText(getApplicationContext(),"Please don't push without selecting a student",Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(PushActivity.this,PullActivity.class);
        startActivity(intent);
    }

    public void pushit() {
        // Pushes the data to the Firebase database

        course = findViewById(R.id.course_ID);
        name = findViewById(R.id.course_name);
        mark = findViewById(R.id.grade_box);

        DatabaseReference table = dbrf.child("simpsons/grades");
        DatabaseReference newGrade = table.push();

        if(!isNumeric(course.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Please put a number for the course",Toast.LENGTH_SHORT).show();
        } else {
            newGrade.child("course_id").setValue(Integer.parseInt(course.getText().toString()));
            newGrade.child("course_name").setValue(name.getText().toString());
            newGrade.child("student_id").setValue(kid);
            newGrade.child("grade").setValue(mark.getText().toString());
            Toast.makeText(getApplicationContext(),"Successfully Pushed!",Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
}
