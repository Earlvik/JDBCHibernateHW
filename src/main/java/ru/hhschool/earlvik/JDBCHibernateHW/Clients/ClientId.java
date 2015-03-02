package ru.hhschool.earlvik.JDBCHibernateHW.Clients;

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

    @Override
    public boolean equals(Object that){
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        ClientId thatClientId = (ClientId)that;
        return this.value == thatClientId.value;
    }

    @Override
    public int hashCode(){
        return value;
    }
}
