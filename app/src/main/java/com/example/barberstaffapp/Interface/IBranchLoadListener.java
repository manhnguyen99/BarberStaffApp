package com.example.barberstaffapp.Interface;

import com.example.barberstaffapp.Model.Salon;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Salon> cityNameList);
    void onBranchFailed(String message);
}
