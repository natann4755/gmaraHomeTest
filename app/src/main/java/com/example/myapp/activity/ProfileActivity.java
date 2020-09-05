package com.example.myapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.SharedSQLiteStatement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.model.Daf;
import com.example.model.Profile;
import com.example.model.shas_masechtot.Seder;
import com.example.myapp.R;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.databinding.ActivityProfileBinding;

import com.example.myapp.fragment.NumberOfRepetitionsProfileFragment;
import com.example.myapp.fragment.TypeStudyProfileFragment;
import com.example.myapp.utils.ConvertIntToPage;
import com.example.myapp.utils.ManageSharedPreferences;
import com.example.myapp.utils.Toast;
import com.example.myapp.utils.UtilsCalender;
import com.google.gson.Gson;
import com.example.model.shas_masechtot_list_models.Shas;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements TypeStudyProfileFragment.OnFragmentTypeStudyProfile, NumberOfRepetitionsProfileFragment.OnFragmentNumberOfRepetitionsProfile
 {

    private ActivityProfileBinding binding;
    private ArrayList<Daf> mListLearning = new ArrayList<>();
    private com.example.model.shas_masechtot_list_models.Shas mAllShas;
    private Profile mProfile = new Profile(0);
    private String mStringTypeOfStudy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initListAllShah();
        openFragment(TypeStudyProfileFragment.newInstance(mProfile, (ArrayList<Seder>) mAllShas.getSeder()),TypeStudyProfileFragment.TAG);
    }

    private void initListAllShah() {
        Gson gson = new Gson();
        try {
            String txt = convertStreamToString(Objects.requireNonNull(this).getAssets().open("list_all_shas_json.txt"));
            mAllShas = gson.fromJson(txt, Shas.class);
        } catch (Exception e) {
        }
    }

    public void openFragment(Fragment myFragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_profile_FL, myFragment)
                .addToBackStack(tag)
                .commit();;
    }

    public void createListTypeOfStudy(String typeOfStudy, int pages) {
        if (mStringTypeOfStudy.equals("דף היומי")) {
            createListAllShas();
        }else {
            createListMasechet(typeOfStudy, pages);
        }
    }

    private void createListAllShas() {
        mListLearning.clear();
        Calendar startDafHayomi = UtilsCalender.findDateOfStartDafHayomiEnglishDate();
        int id = 1;
        for (int i = 0; i < mAllShas.getSeder().size(); i++) {
            for (int j = 0; j < mAllShas.getSeder().get(i).getMasechtot().size(); j++) {
                for (int k = 2; k < (mAllShas.getSeder().get(i).getMasechtot().get(j).getPages() + 2); k++) {
                    int masecetPage = ConvertIntToPage.fixKinimTamidMidot(k,mAllShas.getSeder().get(i).getMasechtot().get(j).getName());
                    Daf mPage = new Daf(mAllShas.getSeder().get(i).getMasechtot().get(j).getName(), masecetPage,"דף היומי" ,1,id);
//                    mPage.setPageDate(startDafHayomi.get(Calendar.DAY_OF_MONTH) + "/" + (startDafHayomi.get(Calendar.MONTH) + 1) + "/" + startDafHayomi.get(Calendar.YEAR));
                    mPage.setPageDate(UtilsCalender.dateStringFormat(startDafHayomi));
                    mListLearning.add(mPage);
                    startDafHayomi.add(Calendar.DATE, 1);
                    id++;
                }
            }
        }
    }



    private void createListMasechet(String masechetName, int pages) {
        mListLearning.clear();
        int id = 1;
        for (int i = 2 ; i < (pages+2) ; i++) {
            mListLearning.add(new Daf(masechetName, ConvertIntToPage.fixKinimTamidMidot(i,masechetName),masechetName,1 ,id));
            id++;
        }
    }


    private void alertDialogAreYouSure() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        ManageSharedPreferences.setProfile(mProfile, getBaseContext());
                        ManageSharedPreferences.setHaveLearning(true, getBaseContext());
                        AppDataBase.getInstance(getBaseContext()).daoLearning1().insertAllLearning(mListLearning);
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
//                        intent.putExtra(KEY_EXTRA_List1, mListLearning);
                        startActivity(intent);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        String typeOfStudy = "מסוג: " + mStringTypeOfStudy;
        String numberOfReps = "חזרות: " + mProfile.getNumberOfReps();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ברצונך ליצור לימוד יומי");
        builder.setMessage(typeOfStudy + "\n" + numberOfReps);
        builder.setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }



    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    @Override
    public void updateActivityTypeStudy(String typeOfStudy, int pages) {
        mStringTypeOfStudy = typeOfStudy;
        createListTypeOfStudy(typeOfStudy,pages);
    }

    @Override
    public void typeStudyOk() {
        if (mStringTypeOfStudy == null || mStringTypeOfStudy.equals("")){
            Toast.Toast(this,"עליך לבחור סוג לימוד");
        }else {
            openFragment(NumberOfRepetitionsProfileFragment.newInstance(mProfile), NumberOfRepetitionsProfileFragment.TAG);
        }
    }

    @Override
    public void numberOfRepOk() {
        alertDialogAreYouSure();
    }
}

