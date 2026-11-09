package co.edu.unipiloto.laboratorio7mongodb.models;

import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.NoSql;

/**
 *
 * @author poeta
 */
@NoSql(dataFormat = DataFormatType.MAPPED)
@Embeddable
public class Vehicle {

    private String plate;
    private String brand;
    private int modelYear;

    @Column(name = "create_at", updatable = false)
    @Temporal(TemporalType.DATE)
    private Calendar createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Calendar updatedAt;

    public Vehicle() {
    }

    public void initializeTimestamps() {
        Calendar now = Calendar.getInstance();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void updateTimestamp() {
        this.updatedAt = Calendar.getInstance();
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }
    
}
