package RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileObject {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("rating")
    @Expose
    private float rating;
    private final static long serialVersionUID = 2297952749881170354L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProfileObject() {
    }

    /**
     *
     * @param id
     * @param body
     * @param createdAt
     * @param name
     * @param userId
     * @param rating
     * @param avatar
     */
    public ProfileObject( int id, int userId, String name, Object avatar, String body, String createdAt, float rating){
        super();
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
        this.body = body;
        this.createdAt = createdAt;
        this.rating = rating;
    }

    public int getId () {
        return id;
    }

    public void setId ( int id){
        this.id = id;
    }

    public int getUserId () {
        return userId;
    }

    public void setUserId ( int userId){
        this.userId = userId;
    }

    public String getName () {
        return name;
    }

    public void setName (String name){
        this.name = name;
    }

    public Object getAvatar () {
        return avatar;
    }

    public void setAvatar (Object avatar){
        this.avatar = avatar;
    }

    public String getBody () {
        return body;
    }

    public void setBody (String body){
        this.body = body;
    }

    public String getCreatedAt () {
        return createdAt;
    }

    public void setCreatedAt (String createdAt){
        this.createdAt = createdAt;
    }

    public float getRating () {
        return rating;
    }

    public void setRating ( float rating){
        this.rating = rating;
    }

}

