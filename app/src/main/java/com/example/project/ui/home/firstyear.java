package com.example.project.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.ui.notifications.NotificationData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;




public class firstyear extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private int year;

    private static final String TAG = "bruh";
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference databaseReference;
    TextView tv;
    String nTitless;
    String nDetailss;
    String switch1="shortage";
    ListView lv;
    Spinner yearone;
    int dayOfWeek;
    String dayWeekText;
    String section;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstyear);
        year = getIntent().getIntExtra("Year", 0);
        yearone =(Spinner)findViewById(R.id.sp1);

        yearone.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        List<String> categories = new ArrayList<String>();
        categories.add("Section 'A'");
        categories.add("Section 'B'");
        Date date=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        dayWeekText = new SimpleDateFormat("EEEE").format(date);
        lv = (ListView)findViewById(R.id.listvw);
        tv = (TextView)findViewById(R.id.tView);
        switch(year){
            case 1:
                tv.setText("First Year");
                break;
            case 2:
                tv.setText("Second Year");
                break;
            case 3:
                tv.setText("Third Year");
                break;
            case 4:
                tv.setText("Fourth Year");
                break;
        }



        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearone.setAdapter(dataAdapter);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
        Button clickButton = (Button) findViewById(R.id.send);


        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if( simpleSwitch.isChecked())
                {
                    simpleSwitch.setText(" Class");
                    switch1="classs";
                }
                else
                {
                    simpleSwitch.setText("Shortage ");
                    switch1="shortage";
                }

            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        List<AttendanceData> attendanceDataList = new ArrayList<>();
        Log.d(TAG,"Running");
        clickButton.setOnClickListener( new View.OnClickListener() {
            String keynade;

            @Override
            public void onClick(View v) {
//                nDetailss = (String) nD.getText().toString();
//                nTitless = (String) nT.getText().toString();
                HashMap<String,Object> map = new HashMap<>();
                map.put("classcode", nTitless);
                map.put("classdescr", nDetailss);
                map.put("classname","switch1");
                map.put("classtime","2pm");
                ref.addValueEventListener(new ValueEventListener() {
                    JSONObject obj;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot notis: dataSnapshot.getChildren())
                        {
                            Log.i("info obtained ", notis.getKey());
                            keynade=notis.getKey();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

                //setting attendance
                AttendanceAdapter aa = (AttendanceAdapter) lv.getAdapter();
                List <AttendanceData> adata = aa.getList();
                for(AttendanceData ad : adata ){
                    ref.child(String.valueOf(year)).child(section).child("weekdays").child(dayWeekText).child(ad.getstudusn())
                            .child("attendance").setValue(ad.getAttendance());
                }

                //writing data to database
                //set counter value
                for(AttendanceData ad : adata ){

                    int adc=ad.getCounter();
                    //adc=0;
                    ref.child(String.valueOf(year)).child(section).child("students").child(ad.getstudusn())
                            .child("counter").setValue( adc);
                    System.out.println(" set the value of counter for  "+ad.getName()+" as "+ad.getCounter());
                }




                simpleSwitch.toggle();
            }
        });



    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = yearone.getSelectedItem().toString();
        section = "A";
        switch(text){
            case "Section 'A'":
                section = "A";
                break;
            case "Section 'B'":
                section = "B";
                break;

        }
        getListFromDatabase(section);

    }

    private void getListFromDatabase(String section) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        List<AttendanceData> attendanceDataList = new ArrayList<>();
        Log.d(TAG,"Running");



        Log.d(TAG,dayWeekText);          // reading data from FireBase
        ref.child(String.valueOf(year)).child(section).child("weekdays").child(dayWeekText).addChildEventListener(new ChildEventListener() {



            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                AttendanceData ad = snapshot.getValue(AttendanceData.class);
                ad.setDaye(dayWeekText);
                ad.setStudusn(snapshot.getKey());
                ad.setSx(section);
                ad.setYr(Integer.toString(year));
                attendanceDataList.add(ad);
                AttendanceAdapter apt = new AttendanceAdapter(getBaseContext(), attendanceDataList);
                lv.setAdapter(apt);
                Log.d(TAG,"Running");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }


            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        section = "A";
        getListFromDatabase("A");
    }


}