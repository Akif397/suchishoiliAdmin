package com.example.suchishoiliWeb.suchishoili.DAO;

public class SteadFastResponseDAO {
    private int status;
    private String message;
    private SteadFastConsignmentDao consignment;
    private String delivery_status;
    private int current_balance;

    public int getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(int current_balance) {
        this.current_balance = current_balance;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SteadFastConsignmentDao getConsignment() {
        return consignment;
    }

    public void setConsignment(SteadFastConsignmentDao consignment) {
        this.consignment = consignment;
    }
}
