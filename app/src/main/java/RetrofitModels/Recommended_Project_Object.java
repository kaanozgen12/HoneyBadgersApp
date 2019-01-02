package RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recommended_Project_Object {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("tags")
    @Expose
    public int[] tags = null;
    @SerializedName("budget_min")
    @Expose
    public int budgetMin;
    @SerializedName("budget_max")
    @Expose
    public int budgetMax;
    @SerializedName("deadline")
    @Expose
    public String deadline;
    private final static long serialVersionUID = -6752701129443206018L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Recommended_Project_Object() {
    }

    /**
     *
     * @param tags
     * @param id
     * @param budgetMax
     * @param title
     * @param description
     * @param userId
     * @param deadline
     * @param budgetMin
     */
    public Recommended_Project_Object(int id, int userId, String title, String description, int[] tags, int budgetMin, int budgetMax, String deadline) {
        super();
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.tags = tags;
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
}
