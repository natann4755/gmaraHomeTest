package com.example.myapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.model.Daf;
import com.example.myapp.activity.SplashActivity;
import com.example.myapp.adapters.AllMasechtotAdapter;
import com.example.myapp.adapters.OneDafAdapter;
import com.example.myapp.databinding.FragmentShewStudyRvBinding;
import com.example.myapp.utils.UtilsCalender;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShewStudyRvFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShewStudyRvFragment extends Fragment implements AllMasechtotAdapter.NameMasechet {
    public static final String TAG = ShewStudyRvFragment.class.getSimpleName();
    private static Bundle args;
    private FragmentShewStudyRvBinding binding;
    private ArrayList<Daf> myListLearning1 = new ArrayList<>();
    private ArrayList<Daf> myListLearning2 = new ArrayList<>();
    private ArrayList<Daf> myListLearning3 = new ArrayList<>();
    private ArrayList<String> allMasechtot;
    private  RecyclerView recyclerView;
    private RecyclerView recyclerViewMasechtot;
    private OneDafAdapter myAdapter;


    public ShewStudyRvFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ShewStudyRvFragment newInstance(ArrayList<Daf>myList1, ArrayList<Daf>myList2 , ArrayList<Daf>myList3) {
        ShewStudyRvFragment fragment = new ShewStudyRvFragment();
        args = new Bundle();
        args.putParcelableArrayList(SplashActivity.KEY_EXTRA_List1,myList1);
        args.putParcelableArrayList(SplashActivity.KEY_EXTRA_List2,myList2);
        args.putParcelableArrayList(SplashActivity.KEY_EXTRA_List3,myList3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myListLearning1 = getArguments().getParcelableArrayList(SplashActivity.KEY_EXTRA_List1);
            myListLearning2 = getArguments().getParcelableArrayList(SplashActivity.KEY_EXTRA_List2);
            myListLearning3 = getArguments().getParcelableArrayList(SplashActivity.KEY_EXTRA_List3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShewStudyRvBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args.clear();
        initTabLayout();
        initLearning(1);
        initReciclerviewDapim();
    }

    private void initLearning(int typeOfStudy) {
        int listSize = 0;
        switch (typeOfStudy) {
            case 1:
                if (myListLearning1 != null && myListLearning1.size()>0)
                listSize = myListLearning1.size();
            case 2:
                if (myListLearning2 != null && myListLearning2.size()>0)
                listSize = myListLearning2.size();
            case 3:
                if (myListLearning2 != null && myListLearning2.size()>0)
                listSize = myListLearning3.size();
        }
        if (listSize >2000) {
            initReciclerviewMasechtot();
            binding.showStudyRVMasechtot.setVisibility(View.VISIBLE);
        }
    }

    private void initReciclerviewDapim() {
        recyclerView = binding.showStudyRVDapim;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter = new OneDafAdapter(getContext(), myListLearning1 , myListLearning2 , myListLearning3);
        recyclerView.setAdapter(myAdapter);
        if (myListLearning1.size()>2000){
            int todayDaf = findTodayDafMoveRV();
            if (todayDaf != -1) {
                recyclerView.scrollToPosition(todayDaf);
            }
        }
    }

    private void initReciclerviewMasechtot() {
        recyclerViewMasechtot = binding.showStudyRVMasechtot;
        recyclerViewMasechtot.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        allMasechtot = new ArrayList<>();
        for (int i = 1; i < myListLearning1.size() ; i++) {
            if (allMasechtot.size()==0){
                allMasechtot.add(myListLearning1.get(i).getMasechet());
            }else if(!myListLearning1.get(i).getMasechet().equals(allMasechtot.get(allMasechtot.size()-1))){
                allMasechtot.add(myListLearning1.get(i).getMasechet());
            }
        }

            AllMasechtotAdapter myAdapter2 = new AllMasechtotAdapter(getContext(),allMasechtot,this);
            recyclerViewMasechtot.setAdapter(myAdapter2);
            }



    private int findTodayDafMoveRV() {
        int todayDafInList = -1;
        String today = UtilsCalender.dateStringFormat(Calendar.getInstance());
        for (int i = 0; i < myListLearning1.size(); i++) {
            if (myListLearning1.get(i).getPageDate().equals(today)) {
                todayDafInList = i;
                break;
            }
        }
        return todayDafInList;
    }

    private void initTabLayout() {
        TabLayout tabLayout = binding.tabLayoutTypeList;
        TabLayout.Tab tab1 = tabLayout.newTab();
        TabLayout.Tab tab2 = tabLayout.newTab();
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab1.setText("הכל");
        tab2.setText("למדתי");
        tab3.setText("דילגתי");
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        setListenerOfTabLayout(tabLayout);

    }

    private void setListenerOfTabLayout(TabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        int tabSelected = tab.getPosition();
                        if(tabSelected==0){
                            if (myListLearning1.size() > 2000) {
                                binding.showStudyRVMasechtot.setVisibility(View.VISIBLE);
                            }
                            myAdapter.filterAllDapim();
                            recyclerView.scrollToPosition(0);
                        }
                        if(tabSelected==1) {
                            binding.showStudyRVMasechtot.setVisibility(View.GONE);
                            myAdapter.filterLearned();
                            recyclerView.scrollToPosition(0);
                        }
                        if(tabSelected==2) {
                            binding.showStudyRVMasechtot.setVisibility(View.GONE);
                            myAdapter.filterSkipped();
                            recyclerView.scrollToPosition(0);
                        }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int tabSelected = tab.getPosition();
                if(tabSelected==0){
                    if (myListLearning1.size() > 2000) {
                        myAdapter.filterAllDapim();
                        recyclerView.scrollToPosition(0);
                    }
                }
            }
        });
    }

    public void changeLearning (int typeOfStudy){
        initLearning(typeOfStudy);
        myAdapter.filterChangeTypeStudy(typeOfStudy);
    }

    @Override
    public void nameMasechet(String nameMasechet) {
        myAdapter.filterAllMasechtot(nameMasechet);
        recyclerView.scrollToPosition(0);
    }
}