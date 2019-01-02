package RetrofitModels;


import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectObject implements Serializable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("tags")
    @Expose
    private int[] tags;
    @SerializedName("category")
    @Expose
    private int categories;
    @SerializedName("budget_min")
    @Expose
    private int budgetMin;
    @SerializedName("budget_max")
    @Expose
    private int budgetMax;
    @SerializedName("deadline")
    @Expose
    private String deadline;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("latitude")
    @Expose
    private Float latitude;
    @SerializedName("longitude")
    @Expose
    private Float longitude;
    @SerializedName("accepted_bid")
    @Expose
    private int accepted_bid;


    private final static long serialVersionUID = -515661806656703852L;

    /**
     * No args constructor for use in serialization
     *
     */
    /*public ProjectObject() {
    }*/

    /**
     * @param tags
     * @param updatedAt
     * @param id
     * @param budgetMax
     * @param title
     * @param createdAt
     * @param description
     * @param userId
     * @param categories
     * @param deadline
     * @param budgetMin
     * @param file
     * @param latitude
     * @param longitude
     * @param accepted_bid
     */
    public ProjectObject(int id, int userId, String title, String description, String createdAt, String updatedAt, int[] tags, int categories, int budgetMin, int budgetMax, String deadline, String file, float latitude, float longitude, int accepted_bid) {
        super();
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tags = tags;
        this.categories = categories;
        this.budgetMin = budgetMin;
        this.budgetMax = budgetMax;
        this.deadline = deadline;
        this.file= file;
        this.latitude= latitude;
        this.longitude = longitude;
        this.accepted_bid= accepted_bid;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCategories() {
        return categories;
    }

    public void setCategories(int categories) {
        this.categories = categories;
    }

    public int getAccepted_bid() {
        return accepted_bid;
    }

    public void setAccepted_bid(int accepted_bid) {
        this.accepted_bid = accepted_bid;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }


    public int[] getTags() {
        return tags;
    }

    public void setTags(int[] tags) {
        this.tags = tags;
    }

    public int getBudgetMin() {
        return budgetMin;
    }

    public void setBudgetMin(int budgetMin) {
        this.budgetMin = budgetMin;
    }

    public int getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(int budgetMax) {
        this.budgetMax = budgetMax;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

  /*  public Compact_Project_Object compress(){
           Compact_Project_Object temp=  new Compact_Project_Object(id, title, 0, "0 bid", string_form_of_int_tags(tags));
            return temp;
    }*/

    public static ArrayList<Tag_Object> string_form_of_int_tags (int[] a){

        final ArrayList<Tag_Object> temp = new ArrayList<>();

        for (int i = 0; i < a.length; i++) {

            Call<Tag_Object> call = RetrofitClient.getInstance().getApi().getTag(a[i]);
            call.enqueue(new Callback<Tag_Object>() {
                @Override
                public void onResponse(Call<Tag_Object> call, Response<Tag_Object> response) {
                    Tag_Object editResponse = response.body();
                    if (response.isSuccessful()) {
                        Log.d("MyTag", "successful tag fetch id:" + editResponse.getId());
                        temp.add(editResponse);
                    }
                }

                @Override
                public void onFailure(Call<Tag_Object> call, Throwable t) {


                }
            });
        }
        return temp;
    }

}