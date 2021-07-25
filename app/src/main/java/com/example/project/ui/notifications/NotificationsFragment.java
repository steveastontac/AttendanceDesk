package com.example.project.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.project.MainActivity;
import com.example.project.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private GoogleSignInClient mGoogleSignInClient;
    private NotificationsViewModel notificationsViewModel;

    private ListView listView;
    private NotificationAdapter nadapter;
    Button bt1,bt2,bt3,ubtn;
    TextView usr;
    ImageView profileImg;

    int  getNotifType(String s1)
    {
        int rresid = this.getResources().getIdentifier(s1, "mipmap", "com.example.project");
        return rresid;
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Log.d(TAG, getString(R.string.default_web_client_id));

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);


        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        profileImg = (ImageView)root.findViewById(R.id.profileImg);
        usr = (TextView)root.findViewById(R.id.username);
        FirebaseUser yoosur = FirebaseAuth.getInstance().getCurrentUser();
        usr.setText(yoosur.getDisplayName());

        Uri personPhoto = yoosur.getPhotoUrl();
        Glide.with(this).load(String.valueOf(personPhoto)).centerCrop().into(profileImg);


  //      final TextView textView = root.findViewById(R.id.text_notifications);
        bt1= (Button)root.findViewById(R.id.b6);
        //signout
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }


        });



        bt2=(Button)root.findViewById(R.id.b7) ;
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationsFragment.this.getActivity(),about.class));
            }
        });
        bt3=(Button)root.findViewById(R.id.b8);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationsFragment.this.getActivity(),support.class));
            }
        });
        ubtn=(Button)root.findViewById(R.id.urlbtn);
        ubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoUrl("http://guru.nmamit.in/");
            }


        });

        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
//
//



        String classStart= " The class will start soon ";
        String shortBoy=" This nigga has shoertage ";
        String nType="classs";
  //      listView    = (ListView) root.findViewById(R.id.listvw);
        ArrayList<NotificationData> notifList = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        //reading data

        ref.addValueEventListener(new ValueEventListener() {
            JSONObject obj;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notifList.clear();
                for (DataSnapshot notis: dataSnapshot.child("notifications").getChildren())
                {

                    int iconid= getNotifType((String) notis.child("type").getValue());
                    String title=   (String) notis.child("title").getValue();
                    String details =(String) notis.child("details").getValue();
                    long nKeys=Long.parseLong(notis.getKey());
                    NotificationData n1=new NotificationData
                            (
                                    iconid,
                                    title,
                                    details
                            );
                    n1.setnKey(nKeys);
                    notifList.add(n1);

                }

//                nadapter = new NotificationAdapter(getActivity(),notifList);
//                listView.setAdapter(nadapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                Toast.makeText(getContext(), "cleared ", Toast.LENGTH_SHORT).show();
////                ref.child("cleared_notifs").child("pos"+position).setValue(notifList.get(position).getnKey()). addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull @NotNull Task<Void> task) {
////                        Log.i(" Push ","completed ");
////
////                    }
////                }).addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull @NotNull Exception e) {
////                        Log.i(" Push ","failed");
////                    }
////                });
////                notifList.remove(position);
//
//                nadapter = new NotificationAdapter(getActivity(),notifList);
//                listView.setAdapter(nadapter);
//            }
//        });
//
////        ref.child("cleared_notifs").setValue(arrayremove).
////                addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull @NotNull Task<Void> task) {
////                        Log.i(" Push ","completed ");
////
////                    }
////                }).addOnFailureListener(new OnFailureListener() {
////            @Override
////            public void onFailure(@NonNull @NotNull Exception e) {
////                Log.i(" Push ","failed");
////            }
////        });
//
            return root;
    }

    private void gotoUrl(String s) {
        Uri uri= Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri) );
    }

    private void signOut(){
        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(), task ->{});
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}