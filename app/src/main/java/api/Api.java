package api;

import java.util.ArrayList;
import java.util.List;

import RetrofitModels.EditProfileResponse;
import RetrofitModels.LoginResponse;
import RetrofitModels.ProfileObject;
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import RetrofitModels.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    @GET("/api/v1/user/register/")
    Call<List<User>> user_id(@Query("search") String email);

    @GET("api/v1/user/profile/{id}/")
    Call<ProfileObject> userProfileGet(@Header("Authorization") String token,@Path("id") String id);

    @PUT("api/v1/user/profile/{id}/")
    Call<ProfileObject> userProfileUpdate(@Header("Authorization") String token,@Path("id") String id, @Body ProfileObject profile);

    @FormUrlEncoded
    @POST("api/v1/user/profile/")
    Call<ProfileObject> userProfileCreate(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("avatar") Object avatar,
            @Field("body") String body
    );


    @FormUrlEncoded
    @POST("api/v1/user/register/")
    Call<User> userRegister(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password

    );


    @FormUrlEncoded
    @POST("api/v1/user/register/")
    Call<EditProfileResponse> userRegisterPost(
            @Field("name") String name,
            @Field("body") String body,
            @Field("avatar") String image,
            @Field("rating") int rating

    );
    @FormUrlEncoded
    @POST("api/v1/project/tag/")
    Call<Tag_Object> postTag(
            @Header("Authorization") String token,
            @Field("title") String title
    );

    @GET("api/v1/project/tag/{id}/")
    Call<Tag_Object> getTag(
            @Path("id") int id

    );
    @GET("api/v1/project/tag/")
    Call<ArrayList<Tag_Object>> getTagbyTitle(
            @Query("search") String title
    );

    @GET("api/v1/project/create/")
    Call<List<ProjectObject>> getProjects();

   /* @FormUrlEncoded
    @POST("api/v1/project/create/")
    Call<ProjectObject> projectCreatePost(
            @Header("Authorization") String token,
            @Field("title") String title,
            @Field("description") String description,
            @Field("tags") ArrayList<Integer> tags,
            @Field("categories") ArrayList<Integer> categories,
            @Field("budget_min") int budget_min,
            @Field("budget_max") int budget_max,
            @Field("deadline") String deadline
    );*/

    @FormUrlEncoded
    @POST("api/v1/project/create/")
    Call<ProjectObject> projectCreatePost(
            @Header("Authorization") String token,
            @Field("title") String title,
            @Field("description") String description,
            @Field("tags") int[] tags,
            @Field("categories") int[] categories,
            @Field("budget_min") int budget_min,
            @Field("budget_max") int budget_max,
            @Field("deadline") String deadline

    );

    /*@GET("api/v1/")
    Call<SearchRestaurantResponse[]> getRestaurantsList(@Query("format") String format, @Query("location") String location,
                                                        @Query("rtype") String rtype);*/

}
