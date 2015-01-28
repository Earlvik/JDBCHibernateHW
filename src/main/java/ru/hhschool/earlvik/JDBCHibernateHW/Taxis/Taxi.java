package ru.hhschool.earlvik.JDBCHibernateHW.Taxis;

/**
 * Created by Earlviktor on 21.01.2015.
 */
public class Taxi {

    private TaxiId id;

    private String driverName;

    private String car;

    private boolean available;



    public static Taxi create(final String driverName, final String car, final boolean available) {
        return new Taxi(null, driverName,car,available);
    }


    static Taxi existing(final TaxiId id, final String driverName, final String car, final boolean available) {
        return new Taxi(id, driverName, car, available);
    }

    private Taxi(TaxiId id, String driverName, String car, boolean available) {
        this.id = id;
        this.driverName = driverName;
        this.car = car;
        this.available = available;
    }

    public void setId(TaxiId id) {
        this.id = id;
    }

    public TaxiId getId() {
        return id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(final Object that){
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        final Taxi thatTaxi = (Taxi) that;
        if (driverName != null ? !driverName.equals(thatTaxi.driverName) : thatTaxi.driverName != null) return false;
        if (id != null ? !(id.getValue() == thatTaxi.id.getValue()) : thatTaxi.id != null) return false;
        if (car != null ? !car.equals(thatTaxi.car) : thatTaxi.car != null) return false;
        if(available != ((Taxi) that).available) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (driverName != null ? driverName.hashCode() : 0);
        result = 31 * result + (car != null ? car.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{id=%d, driver='%s', car='%s', available='%s'}",
                getClass().getSimpleName(), (id==null)?null:id.getValue(), driverName, car, available
        );
    }
}
