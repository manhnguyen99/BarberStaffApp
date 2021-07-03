package com.example.barberstaffapp.Interface;

import com.example.barberstaffapp.Model.City;

import java.util.List;

public interface IOnAllStateLoadListener {
    void onAllStateLoadSuccess(List<City> cityList);
    void onAllStateLoadFailed(String message);
}
