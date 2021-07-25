package com.example.project.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.R;
import com.example.project.ui.notifications.NotificationAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



class DashboardData 
{
    private String subdescr;
    private String subcode ;
    private String subname;
    private int weekpresent;
    private int weekabsent ;
    private long subtime;
    
    public DashboardData( String subn , String subc, String subd, int wab, int wpr, long stm)
    {
        this.subcode=subc;
        this.subname=subn;
        this.subdescr=subd;
        this.weekabsent=wab;
        this.weekpresent=wpr;
        this.subtime=stm;

    }
    public String getSubcode(){return  subcode;}
    public String getSubname(){return subname;}
    public String getSubdescr(){return subdescr;}
    public int getPresents(){return weekpresent;}
    public int getAbsents(){return weekabsent;}
    public long getSubtime(){return subtime;}

    public void setSubcode(String sc){this.subcode=sc;}
    public void setSubname(String sn){this.subname=sn;}
    public void setSubdescr(String sd){this.subdescr=sd;}
    public void setPresents(int wp){this.weekpresent=wp;}
    public void setAbsents(int wa){this.weekabsent=wa;}
    public void setSubtime(int stm){this.subtime=stm;}


}

class DashboardAdapter extends ArrayAdapter<DashboardData>
{
    private Context dcontext;
    private ArrayList<DashboardData> sublist = new ArrayList<>();
    public DashboardAdapter(@NonNull Context context,
                               @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<DashboardData> list) throws Exception
    {

            super(context, 0, list);

        dcontext = context;
        sublist = list;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(dcontext).inflate(R.layout.listdashsteve,parent,false);

        DashboardData cursub = sublist.get(position);

        TextView t1 = (TextView)listItem.findViewById(R.id.code);
        TextView t2 = (TextView)listItem.findViewById(R.id.name);
        TextView t3 = (TextView)listItem.findViewById(R.id.descr);
        TextView t4 = (TextView)listItem.findViewById(R.id.absentees);

        t1.setText(cursub.getSubcode());
        t2.setText(cursub.getSubname());
        t3.setText(cursub.getSubdescr());
        t4.setText( " Absentees this week : " + String.valueOf(cursub.getAbsents()) );

        return listItem;
    }
}

public class DashboardFragment extends Fragment{


    private ListView listView;
    private DashboardAdapter dadapter;
    private DashboardViewModel dashboardViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =

              new ViewModelProvider(this).get(DashboardViewModel.class);

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
       ArrayList<DashboardData> subjects = new ArrayList<>();
      listView = (ListView)root.findViewById(R.id.listV);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();



        ref.addValueEventListener(new ValueEventListener() {
            JSONObject obj;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subjects.clear();
                for (DataSnapshot yearss: dataSnapshot.getChildren())
                {
                    for( DataSnapshot sections : yearss.getChildren())
                    {
                        String key=sections.getKey();
                        String subc=   (String) String.valueOf(sections.child("classcode").getValue());
                        String subn=   (String) String.valueOf(sections.child("classname").getValue());
                        String subd=   (String) String.valueOf(sections.child("classdescr").getValue());
                        long subt= (long)sections.child("classtime").getValue();
                        int wpr=0;
                        int wab=0;
                        for( DataSnapshot student : sections.child("students").getChildren())
                        {
                            wpr+=1;
                            if(Integer.parseInt(String.valueOf(student.child("counter").getValue()))>0)
                            {
                                Log.i("key student",student.getKey());
                                wab+=Integer.parseInt(String.valueOf(student.child("counter").getValue()));
                            }
                        }
                        wpr=(wpr*7)-wab;

                        DashboardData d1=new DashboardData(
                                subn,subc,subd,wab,wpr,subt
                        );
                     Log.i("absent", String.valueOf(wab));
                        subjects.add(d1);

                    }

                }
                try {
                    dadapter = new DashboardAdapter(getContext(), subjects);
                }
                catch (Exception r)
                {
                    System.out.println(r);
                }
                    listView.setAdapter(dadapter);

           }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                String nm= subjects.get(position).getSubname();
                String cd= subjects.get(position).getSubcode();
                String dc= subjects.get(position).getSubdescr();
                String wab =  " Absentees this week : "+String.valueOf(subjects.get(position).getAbsents());
//                TextView tname ;
//
//                tname = (TextView)root.findViewById(R.id.namee);
//                tname.setText(nm);
//
//                tname = (TextView)root.findViewById(R.id.descrr);
//                tname.setText(dc);
//
//                tname = (TextView)root.findViewById(R.id.codee);
//                tname.setText(cd);
//
//                tname = (TextView)root.findViewById(R.id.absenteess);
//                tname.setText(" Absentees this week : "+wab);

                Intent i = new Intent(getContext() , statistics.class);
                i.putExtra("name", nm);
                i.putExtra("code", cd);
                i.putExtra("desc", dc);
                i.putExtra("abse", wab);
                i.putExtra("position", wab);
                startActivity(i);


               //   ref.setValue(subjects.get(position).getSubcode()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Void> task) {
//                        Log.i(" Push ","completed ");
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Log.i(" Push ","failed");
//                    }
//                });
//                notifList.remove(position);
//
//                nadapter = new NotificationAdapter(getActivity(),notifList);
//                listView.setAdapter(nadapter);
            }
        });



        return root;
    }
}