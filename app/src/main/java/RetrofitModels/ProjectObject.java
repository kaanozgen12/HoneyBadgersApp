package RetrofitModels;


import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import Models.Compact_Project_Object;
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
    private int[] tags = null;
    @SerializedName("categories")
    @Expose
    private int[] categories = null;
    @SerializedName("budget_min")
    @Expose
    private int budgetMin;
    @SerializedName("budget_max")
    @Expose
    private int budgetMax;
    @SerializedName("deadline")
    @Expose
    private String deadline;
    private final static long serialVersionUID = -515661806656703852L;

    /**
     * No args constructor for use in serialization
     *
     */
    /*public ProjectObject() {
    }*/

    /**
     *
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
     */
    public ProjectObject(int id, int userId, String title, String description, String createdAt, String updatedAt, int[] tags, int[] categories, int budgetMin, int budgetMax, String deadline) {
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

    public  Compact_Project_Object compress(){
            final Compact_Project_Object temp=  new Compact_Project_Object(id, title, 0, "0 bid",new ArrayList<Tag_Object>());


        for (int i = 0; i < tags.length; i++) {

            Call<Tag_Object> call = RetrofitClient.getInstance().getApi().getTag(tags[i]);
            call.enqueue(new Callback<Tag_Object>() {
                @Override
                public void onResponse(Call<Tag_Object> call, Response<Tag_Object> response) {
                    Tag_Object editResponse = response.body();
                    if (response.isSuccessful()) {
                        Log.d("MyTag", "successful tag fetch id:" + editResponse.getId());
                        temp.getTags().add(editResponse);
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