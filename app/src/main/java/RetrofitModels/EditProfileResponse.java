package RetrofitModels;

public class EditProfileResponse {
    private int id;
    private int user_id;
    private String name;
    private Object avatar;
    private String body;
    private String created_at;
    private double rating;


    public EditProfileResponse(int id, int user_id, String name, Object avatar, String body, String created_at, double rating) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.avatar = avatar;
        this.body = body;
        this.created_at = created_at;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
