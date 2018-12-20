package RetrofitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wallet implements Serializable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("budget")
    @Expose
    private int budget;
    private final static long serialVersionUID = -567732314879669427L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Wallet() {
    }

    /**
     *
     * @param id
     * @param budget
     * @param userId
     */
    public Wallet(int id, int userId, int budget) {
        super();
        this.id = id;
        this.userId = userId;
        this.budget = budget;
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


    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

}