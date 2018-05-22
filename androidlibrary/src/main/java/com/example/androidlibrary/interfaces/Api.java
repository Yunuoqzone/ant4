package com.example.androidlibrary.interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author xueming_wu
 * @date 2018/5/17 0017
 * TODO: 通用retrofit2 请求接口
 */

public interface Api<T> {
    /**
     * 发送一个post请求
     * @param json 携带的参数
     * @return
     */
    @FormUrlEncoded
    @POST
    public Call<T> doPost(@Field("data") String json);
}
