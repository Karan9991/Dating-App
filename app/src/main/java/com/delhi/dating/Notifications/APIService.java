package com.delhi.dating.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA-heYoSs:APA91bH2ilxuWL_b_JMsBWV5zvMpiekl2LWF2ZxF5xAPx4E9Yn6263Cwlcck42ZdQZ-TFvQrINf_VV5UAce3WsNJm_Uj80mSBo0BI5Fd--ZhpPAYbdYQnMghPlY31bJ2gBLn0jOq710n"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
