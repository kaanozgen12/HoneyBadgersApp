package RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment_Object implements Serializable
{

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public UserId userId;
    @SerializedName("profile_id")
    @Expose
    public int profileId;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("commenter_avatar")
    @Expose
    public Object commenterAvatar;
    private final static long serialVersionUID = 1005172197079253897L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Comment_Object() {
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param commenterAvatar
     * @param createdAt
     * @param description
     * @param profileId
     * @param userId
     */
    public Comment_Object(int id, UserId userId, int profileId, String description, String createdAt, String updatedAt, Object commenterAvatar) {
        super();
        this.id = id;
        this.userId = userId;
        this.profileId = profileId;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commenterAvatar = commenterAvatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
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

    public Object getCommenterAvatar() {
        return commenterAvatar;
    }

    public void setCommenterAvatar(Object commenterAvatar) {
        this.commenterAvatar = commenterAvatar;
    }

    public class UserId implements Serializable
    {

        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("username")
        @Expose
        public String username;
        private final static long serialVersionUID = 5591444306357753988L;

        /**
         * No args constructor for use in serialization
         *
         */
        public UserId() {
        }

        /**
         *
         * @param id
         * @param username
         * @param email
         * @param name
         */
        public UserId(int id, String email, String name, String username) {
            super();
            this.id = id;
            this.email = email;
            this.name = name;
            this.username = username;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}



