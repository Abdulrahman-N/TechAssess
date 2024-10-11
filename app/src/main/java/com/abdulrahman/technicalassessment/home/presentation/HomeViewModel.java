package com.abdulrahman.technicalassessment.home.presentation;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.abdulrahman.technicalassessment.home.data.HomeDataSource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final HomeDataSource homeDataSource;

    @Inject
    public HomeViewModel(HomeDataSource homeDataSource) {
        this.homeDataSource = homeDataSource;
        connect();
    }

    public LiveData<String> getMessageLiveData() {
        return homeDataSource.getMessageLiveData();
    }

    public LiveData<Boolean> getConnectionStatusLiveData() {
        return homeDataSource.getConnectionStatusLiveData();
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return homeDataSource.getLoadingLiveData();
    }

    public LiveData<String> getErrorLiveData() {
        return homeDataSource.getErrorLiveData();
    }

    public void connect() {
        homeDataSource.connect();
    }

    public void disconnect() {
        homeDataSource.disconnect();
    }

    public void sendMessage(String message) {
        homeDataSource.sendMessage(message);
    }
}
