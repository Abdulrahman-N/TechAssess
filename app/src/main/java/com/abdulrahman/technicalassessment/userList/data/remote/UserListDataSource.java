package com.abdulrahman.technicalassessment.userList.data.remote;

import com.abdulrahman.technicalassessment.userList.data.local.LocalUserListDataSource;
import com.abdulrahman.technicalassessment.utils.Security;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserListDataSource {

    private final OkHttpClient okHttpClient;
    private final LocalUserListDataSource localUserListDataSource;
    private final Gson gson;

    @Inject
    public UserListDataSource(OkHttpClient okHttpClient, LocalUserListDataSource localDataSource) {
        this.okHttpClient = okHttpClient;
        this.localUserListDataSource = localDataSource;
        this.gson = new Gson();
    }

    public Single<UserListResponse> fetchUsers() {
        return Single.create(emitter -> {
                    Request request = new Request.Builder()
                            .url("https://gorest.co.in/public-api/users")
                            .get()
                            .build();

                    try (Response response = okHttpClient.newCall(request).execute()) {
                        if (!response.isSuccessful()) {
                            emitter.onError(new IOException("Unexpected response code: " + response.code()));
                            return;
                        }

                        if (response.body() == null) {
                            emitter.onError(new IOException("Response body is null"));
                            return;
                        }

                        try {
                            String encryptedJson = Security.encrypt(response.body().string());
                            localUserListDataSource.storeEncryptedJson(encryptedJson);
                            UserListResponse userResponse = gson.fromJson(Security.decrypt(localUserListDataSource.getEncryptedJson()), UserListResponse.class);
                            emitter.onSuccess(userResponse);
                        } catch (Exception e) {
                            emitter.onError(new IOException("Failed to parse response: " + e.getMessage()));
                        }
                    } catch (IOException e) {
                        emitter.onError(e);
                    }
                }
        );
    }


}
