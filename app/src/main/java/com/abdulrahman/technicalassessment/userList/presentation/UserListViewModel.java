package com.abdulrahman.technicalassessment.userList.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abdulrahman.technicalassessment.userList.domain.GetUserListUseCase;
import com.abdulrahman.technicalassessment.userList.domain.User;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class UserListViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final GetUserListUseCase getUserListUseCase;
    private final MutableLiveData<List<User>> _userList = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>();

    public LiveData<List<User>> userList = _userList;
    public LiveData<String> error = _error;
    public LiveData<Boolean> loading = _loading;

    @Inject
    public UserListViewModel(GetUserListUseCase getUsersUseCase) {
        this.getUserListUseCase = getUsersUseCase;
        fetchUsers();
    }

    public void fetchUsers() {
        _loading.setValue(true);
        Disposable disposable = getUserListUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        users -> {
                            _loading.postValue(false);
                            _userList.postValue(users);
                        },
                        throwable -> {
                            _loading.postValue(false);
                            _error.postValue(throwable.getMessage());
                        }
                );
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

}