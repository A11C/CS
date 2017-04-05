package com.fiuady.db;

public class AssemblyProduct {
    private int id;
    private int product_id;
    private int qty;

    public AssemblyProduct(int id,int product_id, int qty){
        this.id=id;
        this.product_id=product_id;
        this.qty=qty;
    }

    public int getId() {
        return id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}