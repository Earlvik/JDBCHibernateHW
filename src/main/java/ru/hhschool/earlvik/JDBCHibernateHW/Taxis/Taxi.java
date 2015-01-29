package ru.hhschool.earlvik.JDBCHibernateHW.Taxis;

/**
 * Created by Earlviktor on 21.01.2015.
 */
public class Taxi {
    /**
     * Unique taxi number
     */
    private TaxiId id;
    /**
     * Name of taxi driver
     */
    private String driverName;
    /**
     * Car name
     */
    private String car;
    /**
     * Is taxi available for calling
     */
    private boolean available;
    /**
     * Number of drives by this taxi
     */
    private int drives;



    public static Taxi create(final String driverName, final String car, final boolean available) {
        return new Taxi(null, driverName,car,available, 0);
    }


    static Taxi existing(final TaxiId id, final String driverName, final String car, final boolean available, int drives) {
        return new Taxi(id, driverName, car, available, drives);
    }

    private Taxi(TaxiId id, String driverName, String car, boolean available, int drives) {
        this.id = id;
        this.driverName = driverName;
        this.car = car;
        this.available = available;
        this.drives = drives;
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

    public int getDrives() {
        return drives;
    }

    public void setDrives(int drives) {
        this.drives = drives;
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
                "%s{id=%d, driver='%s', car='%s', available='%s', drives='%s}",
                getClass().getSimpleName(), (id==null)?null:id.getValue(), driverName, car, available, drives
        );
    }
}
