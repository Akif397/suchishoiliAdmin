package com.example.suchishoiliWeb.suchishoili.DAO;

public class SteadFastConsignmentDao {
    private String consignment_id;
    private String invoice;
    private String tracking_code;
    private String recipient_name;
    private String recipient_phone;
    private String recipient_address;
    private String cod_amount;
    private String status;
    private String note;
    private String created_at;
    private String updated_at;

    public String getConsignment_id() {
        return consignment_id;
    }

    public void setConsignment_id(String consignment_id) {
        this.consignment_id = consignment_id;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getTracking_code() {
        return tracking_code;
    }

    public void setTracking_code(String tracking_code) {
        this.tracking_code = tracking_code;
    }

    public String getRecipient_name() {
        return recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getRecipient_phone() {
        return recipient_phone;
    }

    public void setRecipient_phone(String recipient_phone) {
        this.recipient_phone = recipient_phone;
    }

    public String getRecipient_address() {
        return recipient_address;
    }

    public void setRecipient_address(String recipient_address) {
        this.recipient_address = recipient_address;
    }

    public String getCod_amount() {
        return cod_amount;
    }

    public void setCod_amount(String cod_amount) {
        this.cod_amount = cod_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
