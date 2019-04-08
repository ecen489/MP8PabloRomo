package com.example.mp8_pablo_romo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PullActivity extends AppCompatActivity {


    FirebaseDatabase fbdb;
    DatabaseReference dbrf;

    FirebaseAuth mAuth;

    EditText stud;
    ListView maniac;

    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        // Firebase things
        fbdb = FirebaseDatabase.getInstance();
        dbrf = fbdb.getReference();

        mAuth = FirebaseAuth.getInstance();

        // Listmania things
        maniac = findViewById(R.id.listmania);

        // The student ID to be grabbed
        stud = findViewById(R.id.student);
    }

    public void query1(View view) {
        // Performs Query 1
        // All courses and grades associated with the student ID entered will be shown below in the
        // recycler view.

        if(stud.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please give a student ID",Toast.LENGTH_SHORT).show();
        } else if(isNumeric(stud.getText().toString())) {
            // pull the data and put into the recycler view
            ID = Integer.parseInt(stud.getText().toString());

            DatabaseReference gradekey = dbrf.child("simpsons/grades");

            Query query = gradekey.orderByChild("student_id").equalTo(ID);
            query.addListenerForSingleValueEvent(valueEventListener1);
        } else {
            Toast.makeText(getApplicationContext(),"A student ID is numeric, please try again",Toast.LENGTH_SHORT).show();
        }

    }

    // The ValueEventListener for Query 1
    ValueEventListener valueEventListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            int count = 0;

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Go through to count how big to make the array to give to the maniac
                    count++;
                }

                // Make the array to be put on the maniac
                String[] allgrades = new String[count];

                if(count != 0) {
                    int i = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Go through and get the data and insert into the array container

                        Grade grade = snapshot.getValue(Grade.class);

                        try {
                            allgrades[i] = grade.getcourse_name() + ", " + grade.getgrade();
                        } catch (NullPointerException n) {
                            Toast.makeText(getApplicationContext(), "null pointer exception you FUCKED UP and you SUCK", Toast.LENGTH_SHORT).show();
                        }
                        i++;
                    }
                }

                // Put all grades onto the maniac
                ArrayAdapter<String> myadapter = new ArrayAdapter<String>(PullActivity.this,android.R.layout.simple_list_item_1,allgrades);
                maniac.setAdapter(myadapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),"Database Error dipshit",Toast.LENGTH_SHORT).show();
        }
    };

    public void query2(View view) {
        // Performs Query 2
        // Does the same as Query 1 but for all students whose IDs are greater than or equal to
        // the inputted ID currently.

        if(stud.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please give a student ID",Toast.LENGTH_SHORT).show();
        } else if(isNumeric(stud.getText().toString())) {
            // pull the data and put into the recycler view
            ID = Integer.parseInt(stud.getText().toString());

            DatabaseReference gradekey =dbrf.child("simpsons/grades");

            Query query = gradekey.orderByChild("student_id").startAt(ID);
            query.addListenerForSingleValueEvent(valueEventListener2);
        } else {
            Toast.makeText(getApplicationContext(),"A student ID is numeric, please try again",Toast.LENGTH_SHORT).show();
        }

    }

    // The ValueEventListener for Query 1
    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            int count = 0;


            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Go through to count how big to make the array to give to the maniac
                    count++;
                }

                // Make the array to be put on the maniac
                String[] allgrades = new String[count];

                if(count != 0) {
                    int i = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Go through and get the data and insert into the array container

                        Grade grade = snapshot.getValue(Grade.class);

                        try {
                            switch(grade.getstudent_id()) {
                                case 123:
                                    allgrades[i] = "Bart, " + grade.getcourse_name() + ", " + grade.getgrade();
                                    break;
                                case 404:
                                    allgrades[i] = "Ralph, " + grade.getcourse_name() + ", " + grade.getgrade();
                                    break;
                                case 456:
                                    allgrades[i] = "Milhouse, " + grade.getcourse_name() + ", " + grade.getgrade();
                                    break;
                                case 888:
                                    allgrades[i] = "Lisa, " + grade.getcourse_name() + ", " + grade.getgrade();
                                    break;
                            }

                        } catch (NullPointerException n) {
                            Toast.makeText(getApplicationContext(), "null pointer exception you FUCKED UP and you SUCK", Toast.LENGTH_SHORT).show();
                        }
                        i++;
                    }
                }

                // Put all grades onto the maniac
                ArrayAdapter<String> myadapter = new ArrayAdapter<String>(PullActivity.this,android.R.layout.simple_list_item_1,allgrades);
                maniac.setAdapter(myadapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),"Database Error dipshit",Toast.LENGTH_SHORT).show();
        }
    };

    public void pushing(View view) {
        // Changes the activity to the PushActivity
        Intent push = new Intent(PullActivity.this,PushActivity.class);
        startActivity(push);
    }

    public void getmeouttahere(View view) {
        // Goes back to the MainActivity so that the user can be logged out
        mAuth.signOut();

        Intent main = new Intent(PullActivity.this,MainActivity.class);
        startActivity(main);
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

