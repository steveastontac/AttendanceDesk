package com.example.project.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.R;

import static com.example.project.R.layout.fragment_home;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Button one,two,three,four;
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(fragment_home, container, false);
        one = (Button)root.findViewById(R.id.b1st);
        one.setOnClickListener(this);
        two =(Button)root.findViewById(R.id.b2nd);
        two.setOnClickListener(this);
        three =(Button)root.findViewById(R.id.b3rd);
        three.setOnClickListener(this);
        four =(Button)root.findViewById(R.id.b4th);
        four.setOnClickListener(this);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onClick(View view) {
        Intent one= new Intent(HomeFragment.this.getActivity() , firstyear.class);
        Button button = (Button)view;
        switch (button.getText().toString()){
            case "1st Year":
                one.putExtra("Year",1);
                break;
            case "2nd Year":
                one.putExtra("Year",2);
                break;
            case "3rd Year":
                one.putExtra("Year",3);
                break;
            case "4th Year":
                one.putExtra("Year",4);
                break;
        }
        startActivity(one);

    }
}