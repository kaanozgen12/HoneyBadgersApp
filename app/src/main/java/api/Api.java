package api;

import RetrofitModels.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

/*
    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse> createUser(
        @Field("email") String email;
        @Field("password" String password);
    );*/

    @FormUrlEncoded
    @POST("api/v1/user/login/")
    Call<LoginResponse> userLogin(
        @Field("username") String email,
        @Field("password") String password

    );
    @FormUrlEncoded
    @POST("api/v1/user/register/")
    Call<LoginResponse> userRegister(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password

    );
}
