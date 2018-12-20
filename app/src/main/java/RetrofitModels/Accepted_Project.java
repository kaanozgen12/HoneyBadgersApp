package RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Accepted_Project {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("freelancer_id")
    @Expose
    public int freelancerId;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("price")
    @Expose
    public int price;
    @SerializedName("deadline")
    @Expose
    public String deadline;
    @SerializedName("file")
    @Expose
    public Object file;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("accepted_bid")
    @Expose
    public int acceptedBid;
    @SerializedName("is_done")
    @Expose
    public boolean isDone;
    private final static long serialVersionUID = 986727475938814959L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Accepted_Project() {
    }

    /**
     *
     * @param freelancerId
     * @param acceptedBid
     * @param id
     * @param updatedAt
     * @param isDone
     * @param title
     * @param price
     * @param description
     * @param createdAt
     * @param file
     * @param userId
     * @param longitude
     * @param latitude
     * @param deadline
     */
    public Accepted_Project(int id, int userId, int freelancerId, String title, String description, String createdAt, String updatedAt, int price, String deadline, Object file, String latitude, String longitude, int acceptedBid, boolean isDone) {
        super();
        this.id = id;
        this.userId = userId;
        this.freelancerId = freelancerId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.price = price;
        this.deadline = deadline;
        this.file = file;
        this.latitude = latitude;
        this.longitude = longitude;
        this.acceptedBid = acceptedBid;
        this.isDone = isDone;
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

    public int getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(int freelancerId) {
        this.freelancerId = freelancerId;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Object getFile() {
        return file;
    }

    public void setFile(Object file) {
        this.file = file;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getAcceptedBid() {
        return acceptedBid;
    }

    public void setAcceptedBid(int acceptedBid) {
        this.acceptedBid = acceptedBid;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
