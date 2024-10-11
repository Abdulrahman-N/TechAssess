package com.abdulrahman.technicalassessment.home.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abdulrahman.technicalassessment.utils.SingleLiveEvent;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class HomeDataSource {

    private final OkHttpClient client;
    private WebSocket webSocket;

    private final SingleLiveEvent<String> messageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> connectionStatusLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<String> errorLiveData = new SingleLiveEvent<>();

    @Inject
    public HomeDataSource(OkHttpClient client) {
        this.client = client;
    }

    public void connect() {
        loadingLiveData.postValue(true);

        Request request = new Request.Builder()
                .url("wss://echo.websocket.org/")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                loadingLiveData.postValue(false);
                connectionStatusLiveData.postValue(true);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                connectionStatusLiveData.postValue(false);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
                loadingLiveData.postValue(false);
                connectionStatusLiveData.postValue(false);
                errorLiveData.postValue(t.getMessage());
            }
        });
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "Disconnected");
            webSocket = null;
            connectionStatusLiveData.postValue(false);
        }
    }

    public void sendMessage(String message) {
        if (webSocket != null && connectionStatusLiveData.getValue() != null && connectionStatusLiveData.getValue()) {
            try {
                webSocket.send(message);
                messageLiveData.setValue(message);
            } catch (Exception e) {
                errorLiveData.setValue(e.getMessage());
            }
        } else {
            errorLiveData.setValue("You have to connect to websocket first!");
        }
    }

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public LiveData<Boolean> getConnectionStatusLiveData() {
        return connectionStatusLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
