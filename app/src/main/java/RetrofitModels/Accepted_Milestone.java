package RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Accepted_Milestone {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("acceptedproject_id")
    @Expose
    public int acceptedprojectId;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("amount")
    @Expose
    public int amount;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deadline")
    @Expose
    public String deadline;
    @SerializedName("file")
    @Expose
    public String file;
    @SerializedName("is_done")
    @Expose
    public boolean isDone;
    private final static long serialVersionUID = 2225603821213629068L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Accepted_Milestone() {
    }

    /**
     *
     * @param updatedAt
     * @param amount
     * @param id
     * @param isDone
     * @param file
     * @param createdAt
     * @param description
     * @param userId
     * @param acceptedprojectId
     * @param deadline
     */
    public Accepted_Milestone(int id, int userId, int acceptedprojectId, String description, int amount, String createdAt, String updatedAt, String deadline, String file, boolean isDone) {
        super();
        this.id = id;
        this.userId = userId;
        this.acceptedprojectId = acceptedprojectId;
        this.description = description;
        this.amount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deadline = deadline;
        this.file = file;
        this.isDone = isDone;
    }

}
