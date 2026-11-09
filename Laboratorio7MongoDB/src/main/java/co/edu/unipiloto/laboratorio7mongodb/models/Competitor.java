package co.edu.unipiloto.laboratorio7mongodb.models;

import java.io.Serializable;
import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Embedded;

import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.Field;
import org.eclipse.persistence.nosql.annotations.NoSql;

/**
 * 
 * @NoSql: indica que la entidad se almacenará mediante el formato de documentos de EclipseLink NoSQL.
 * @Field(name = "_id"): vincula el identificador Java con el identificador de MongoDB.
 * @GeneratedValue: permite generar el identificador.
 * @PrePersist y @PreUpdate: gestionan las fechas de creación y actualización.
 * 
 * @author poeta
 */
@NoSql(dataType = "COMPETITOR", dataFormat = DataFormatType.MAPPED)
@Entity
public class Competitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Field(name = "_id")
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", updatable = false)
    private Calendar createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Calendar updatedAt;

    @Embedded
    private Vehicle vehicle;

    private String name;
    private String surname;
    private int age;
    private String telephone;
    private String cellphone;
    private String address;
    private String city;
    private String country;
    private boolean winner;

    public Competitor() {
    }

    @PreUpdate
    private void updateTimestamp() {
        
        this.updatedAt = Calendar.getInstance();

        if (this.vehicle != null) {
            this.vehicle.updateTimestamp();
        }
        
    }

    @PrePersist
    private void creationTimestamp() {
        
        Calendar now = Calendar.getInstance();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.vehicle != null) {
            this.vehicle.initializeTimestamps();
        }
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

}
