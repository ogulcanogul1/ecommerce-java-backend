package com.ecommerce.project.result;

import lombok.Data;

@Data
public class ServiceResult<T> {
        private boolean isSuccess;
        private String message;
        private T data;

    public ServiceResult(boolean isSuccess, String message, T data) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.data = data;
    }

    public ServiceResult(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }


    public static <T> ServiceResult<T> success(T data,String message){
        return new ServiceResult<>(true,message,data);
    }

    public static <T> ServiceResult<T> error(String message){
        return new ServiceResult<>(false,message);
    }

    public static <T> ServiceResult<T> success(String message) {
        return new ServiceResult<>(true, message);
    }

}


