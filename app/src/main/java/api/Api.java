package api;

import java.util.ArrayList;
import java.util.List;

import RetrofitModels.Bid_Object;
import RetrofitModels.EditProfileResponse;
import RetrofitModels.LoginResponse;
import RetrofitModels.Milestone_Object;
import RetrofitModels.ProfileObject;
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import RetrofitModels.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @GET("api/v1/user/profile/")
    Call<List<ProfileObject>> userProfileGet(@Header("Authorization") String token,@Query("search") String id);

    @GET("api/v1/project/create/")
    Call<List<ProjectObject>> getMyProjects(@Query("search") int user_id);

    @FormUrlEncoded
    @PUT("api/v1/user/profile/{id}/")
    Call<ProfileObject> userProfileUpdate(@Header("Authorization") String token,@Path("id") String id, @Field("name") String name,
                                          @Field("avatar") String avatar,
                                          @Field("body") String body);

    @FormUrlEncoded
    @POST("api/v1/project/bid/")
    Call<Bid_Object> bidOnProject(@Header("Authorization") String token, @Field("project_id") int project_id,
                                  @Field("description") String description,
                                  @Field("amount") int amount);

    @FormUrlEncoded
    @POST("api/v1/project/milestone/")
    Call<Milestone_Object> milestoneOnBid(@Header("Authorization") String token, @Field("bid_id") int bid_id,
                                          @Field("description") String description,
                                          @Field("amount") int amount,
                                          @Field("deadline") String deadline
                                          );
    @GET("api/v1/project/bid/")
    Call<List<Bid_Object>> getAllBidsOfProject(@Header("Authorization") String token,@Query("search") int project_id);
    @GET("api/v1/project/milestone/")
    Call<List<Milestone_Object>> getAllMilestonesOfBid(@Header("Authorization") String token,@Query("search") int bid_id);



    @DELETE("/api/v1/project/create/{id}/")
    Call<Void> deleteProject(@Header("Authorization") String token, @Path("id") int project_id,@Query("validate") boolean validate);

    @FormUrlEncoded
    @POST("api/v1/user/profile/")
    Call<ProfileObject> userProfileCreate(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("avatar") String avatar,
            @Field("body") String body
    );

    @Multipart
    @POST("/media/images/")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image);


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
    @GET("api/v1/user/register/{id}/")
    Call<User> getUserEmail(
            @Path("id") int id

    );
    @GET("api/v1/project/tag/")
    Call<ArrayList<Tag_Object>> getTagbyTitle(
            @Query("search") String title
    );

    @GET("api/v1/project/create/")
    Call<List<ProjectObject>> getProjects();

    @GET("api/v1/project/tag/")
    Call<List<Tag_Object>> getTags();

    @GET("api/v1/project/create/{id}/")
    Call<ProjectObject> getProjectbyId(
            @Path("id") int id
    );

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
            @Field("category") int categories,
            @Field("budget_min") int budget_min,
            @Field("budget_max") int budget_max,
            @Field("deadline") String deadline,
            @Field("latitude") Float latitude,
            @Field("longitude") Float longitude

    );

    @Multipart
    @POST ("/api/Accounts/editaccount")
    Call<User> editProfilePhoto (@Header("Authorization") String authorization, @Part("file\"; filename=\"pp.png\" ") RequestBody file , @Part("FirstName") RequestBody fname);




    /*@GET("api/v1/")
    Call<SearchRestaurantResponse[]> getRestaurantsList(@Query("format") String format, @Query("location") String location,
                                                        @Query("rtype") String rtype);*/

}
