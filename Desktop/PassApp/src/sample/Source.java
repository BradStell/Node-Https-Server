package sample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Brad on 2/8/2016.
 */
public class Source {

    private String name;
    private List<Account> accounts;

    public Source() {
        accounts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Iterator<Account> getAccounts() {
        return accounts.listIterator();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
}
