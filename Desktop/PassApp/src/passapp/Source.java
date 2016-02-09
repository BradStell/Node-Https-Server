package passapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Brad on 2/8/2016.
 */
public class Source {

    private String name;
    private List<Account> accounts;

    public Source(String name) {
        this.name = name;
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

    public void shit() {
        System.out.print("Shit");
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
