package com.fiuady.db;

/**
 * Created by jessm on 18/04/2017.
 */

public class Order {
    private int id_or;
    private int status;
    private int customerId;
    private int date;

    public Order(int id_or, int status, int customerId, int date) {
        this.id_or = id_or;
        this.status= status;
        this.customerId = customerId;
        this.date = date;
    }

    public int getIdOr() {
        return id_or;
    }

    public void setIdOr(int id) {
        this.id_or = id_or;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
