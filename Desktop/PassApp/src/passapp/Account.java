package passapp;

/**
 * Created by Brad on 2/8/2016.
 */
public class Account {

    private String username;
    private String password;
    private Source parent;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Source getParent() {
        return parent;
    }

    public void setParent(Source parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Username = " + this.username + " Password = " + this.password;
    }
}
