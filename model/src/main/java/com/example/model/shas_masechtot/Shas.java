package com.example.model.shas_masechtot_list_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.model.shas_masechtot.Seder;

import java.io.Serializable;
import java.util.List;

public class Shas {

//    @SerializedName("seder")
    private List<Seder> seder;

    protected Shas(Parcel in) {
    }


    public List<Seder> getSeder() {
        return seder;
    }

    public void setSeder(List<Seder> seder) {
        this.seder = seder;
    }


}
