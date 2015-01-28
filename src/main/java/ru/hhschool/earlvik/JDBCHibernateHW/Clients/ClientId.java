package ru.hhschool.earlvik.JDBCHibernateHW.Clients;

/**
 * Created by Earlviktor on 21.01.2015.
 */
public class ClientId {
    private int value;

    public ClientId(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
