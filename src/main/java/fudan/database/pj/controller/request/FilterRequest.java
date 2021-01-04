package fudan.database.pj.controller.request;

/**
 * @author YHT
 */
public class FilterRequest {
    private int filter;
    private String username;
    public FilterRequest(){}

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
