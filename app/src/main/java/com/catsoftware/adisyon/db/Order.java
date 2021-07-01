package com.catsoftware.adisyon.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ordersTable")
public class Order implements java.io.Serializable {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    @ColumnInfo(name = "orderNo")
    public String orderNo;

    @ColumnInfo(name = "hour")
    public int hour;

    @ColumnInfo(name = "minute")
    public int minute;

    @ColumnInfo(name = "driver")
    public String driver;

    @ColumnInfo(name = "paymentMethod")
    public String paymentMethod;

    @ColumnInfo(name = "price")
    public Double price;

    @ColumnInfo(name="isDeleted")
    public boolean isDeleted;

    @ColumnInfo(name="registrationDay")
    public int registrationDay;

    @ColumnInfo(name="registrationMonth")
    public int registrationMonth;

    @ColumnInfo(name="registrationYear")
    public int registrationYear;

    public void setRegistrationYear(int registrationYear) {
        this.registrationYear = registrationYear;
    }

    public void setRegistrationDay(int registrationDay) {
        this.registrationDay = registrationDay;
    }

    public void setRegistrationMonth(int registrationMonth) {
        this.registrationMonth = registrationMonth;
    }

    public int getID() {
        return ID;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
