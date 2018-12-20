package RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Milestone_Object implements Serializable {


    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("bid_id")
    @Expose
    public int bidId;
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
    private String status;
    private final static long serialVersionUID = 4155219522019956326L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Milestone_Object() {
    }

    /**
     *
     * @param updatedAt
     * @param amount
     * @param id
     * @param createdAt
     * @param description
     * @param userId
     * @param bidId
     * @param deadline
     */
    public Milestone_Object(int id, int userId, int bidId, String description, int amount, String createdAt, String updatedAt, String deadline, String status) {
        super();
        this.id = id;
        this.userId = userId;
        this.bidId = bidId;
        this.description = description;
        this.amount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deadline = deadline;
        this.status= status;
    }
    public Milestone_Object(  int amount, String description, String deadline, String status) {
        super();
        this.description = description;
        this.amount = amount;
        this.deadline = deadline;
        this.status= status;
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

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

