package RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileObject {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user")
    @Expose
    private String name;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("user_info")
    @Expose
    private User user_info;
    @SerializedName("tags")
    @Expose
    private int[] tags;
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
     * @param name
     * @param avatar
     * @param user_info
     * @param tags
     */
    public ProfileObject( int id, String name, String avatar, String body, User user_info, int[] tags){
        super();
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.body = body;
        this.user_info = user_info;
        this.tags = tags;
    }

    public String getAvatar() {
        return (String) avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId () {
        return id;
    }

    public void setId ( int id){
        this.id = id;
    }


    public String getName () {
        return name;
    }

    public void setName (String name){
        this.name = name;
    }


    public String getBody () {
        return body;
    }

    public void setBody (String body){
        this.body = body;
    }

    public User getUser_info() {
        return user_info;
    }

    public void setUser_info(User user_info) {
        this.user_info = user_info;
    }

    public int[] getTags() {
        return tags;
    }

    public void setTags(int[] tags) {
        this.tags = tags;
    }
}

