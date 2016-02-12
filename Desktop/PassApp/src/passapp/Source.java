package passapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Brad on 2/8/2016.
 */
public class Source {

    String id;
    private String name;
    String userSpelledName;
    private List<Account> accounts;

    public Source() {
        accounts = new ArrayList<>();
    }

    public Source(String name) {
        this.name = name;
        accounts = new ArrayList<>();
    }

    public Source (String name, String id, String userSpelledName) {
        this.name = name;
        this.id = id;
        this.userSpelledName = userSpelledName;
        accounts = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserSpelledName() {
        return userSpelledName;
    }

    public void setUserSpelledName(String userSpelledName) {
        this.userSpelledName = userSpelledName;
    }

    public Iterator<Account> getAccounts() {
        return accounts.listIterator();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
