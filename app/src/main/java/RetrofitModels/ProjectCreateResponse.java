package RetrofitModels;

public class ProjectCreateResponse {
    private int id;
    private int user_id;
    private String title;
    private String description;
    private String created_at;
    private String updated_at;
    private int[] tags;
    private int[] categories;
    private int budgetMin;
    private int budgetMax;
    private String deadline;

    public ProjectCreateResponse(int id, int user_id, String title, String description, String created_at, String updated_at, int[] tags, int[] categories, int budgetMin, int budgetMax, String deadline) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.tags = tags;
        this.categories = categories;
        this.budgetMin = budgetMin;
        this.budgetMax = budgetMax;
        this.deadline = deadline;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int[] getTags() {
        return tags;
    }

    public void setTags(int[] tags) {
        this.tags = tags;
    }

    public int[] getCategories() {
        return categories;
    }

    public void setCategories(int[] categories) {
        this.categories = categories;
    }

    public int getBudgetMin() {
        return budgetMin;
    }

    public void setBudgetMin(int budgetMin) {
        this.budgetMin = budgetMin;
    }

    public int getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(int budgetMax) {
        this.budgetMax = budgetMax;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
