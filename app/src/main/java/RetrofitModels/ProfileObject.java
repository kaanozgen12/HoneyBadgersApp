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
     */
    public ProfileObject( int id, String name, String avatar, String body){
        super();
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.body = body;
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


}

