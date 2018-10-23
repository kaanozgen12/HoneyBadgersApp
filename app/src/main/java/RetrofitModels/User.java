package RetrofitModels;

public class User {

    private int id;
    private String email,task;

    public User(int id, String email, String task) {
        this.id = id;
        this.email = email;
        this.task = task;
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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
