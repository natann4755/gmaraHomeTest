package com.example.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.example.model.Daf;
import com.example.myapp.R;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.databinding.ActivityMainBinding;
import com.example.myapp.fragment.ShewStudyRvFragment;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;
    ArrayList<Daf> myList1;
    ArrayList<Daf>  myList2;
    ArrayList<Daf>  myList3;
    private ShewStudyRvFragment mShewStudyRvFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        myList1 = initData();
        openFragment(mShewStudyRvFragment = ShewStudyRvFragment.newInstance(myList1, myList2 , myList3), ShewStudyRvFragment.TAG);
        initViews();
    }

    public void openFragment(Fragment myFragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.MA_frameLayout, myFragment)
                .addToBackStack(tag)
                .commit();
    }

    private void initViews() {
        if (myList1 != null && myList1.size()>0){
            binding.typeOfStudy1BU.setText(myList1.get(0).getTypeOfStudy());
        }
        if (myList2 != null && myList2.size()>0){
            binding.typeOfStudy2BU.setText(myList2.get(0).getTypeOfStudy());
        }
        if (myList3 != null && myList3.size()>0){
            binding.typeOfStudy3BU.setText(myList3.get(0).getTypeOfStudy());
        }
        binding.typeOfStudy1BU.setOnClickListener(v -> {mShewStudyRvFragment.changeLearning(1);});
        binding.typeOfStudy2BU.setOnClickListener(v -> {mShewStudyRvFragment.changeLearning(2);});
        binding.typeOfStudy3BU.setOnClickListener(v -> {mShewStudyRvFragment.changeLearning(3);});
    }

    private ArrayList<Daf> initData() {
//        AppDataBase.getInstance(this).daoLearning1().deleteAll();
        return (ArrayList<Daf>) AppDataBase.getInstance(this).daoLearning1().getAllLearning(1);
    }
}