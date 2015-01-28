package ru.hhschool.earlvik.JDBCHibernateHW.Clients;

import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiId;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Earlviktor on 21.01.2015.
 */

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="client_id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "creation_time", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "car_id")
    private Integer carId;




    public Client(final String firstName, final String lastName, TaxiId carId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.creationDate = new Date();
        this.carId = (carId == null)?null:carId.getValue();
    }

    @Deprecated
    Client() {}

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        final Client thatClient = (Client) that;
        if (firstName != null ? !firstName.equals(thatClient.firstName) : thatClient.firstName != null) return false;
        if (id != null ? !id.equals(thatClient.id) : thatClient.id != null) return false;
        if (lastName != null ? !lastName.equals(thatClient.lastName) : thatClient.lastName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + creationDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s{id=%d, firstName='%s', lastName='%s', creationDate='%s', requested car id='%s}",
                getClass().getSimpleName(), id, firstName, lastName, creationDate,carId);
    }
}
