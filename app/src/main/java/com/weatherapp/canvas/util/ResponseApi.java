package com.weatherapp.canvas.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ResponseApi<T> {
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable error;

    private ResponseApi(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> ResponseApi<T> loading() {
        return new ResponseApi<>(Status.LOADING, null, null);
    }

    public static <T> ResponseApi success(@NonNull T data) {
        return new ResponseApi<>(Status.SUCCESS, data, null);
    }

    public static <T> ResponseApi<T> error(@NonNull Throwable error) {
        return new ResponseApi<>(Status.ERROR,  null, error);
    }
    public enum  Status {
        LOADING,
        SUCCESS,
        ERROR
    }

}