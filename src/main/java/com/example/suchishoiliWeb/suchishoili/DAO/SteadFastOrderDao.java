package com.example.suchishoiliWeb.suchishoili.DAO;

public class SteadFastOrderDao {
    private String invoice;
    private String recipient_name;
    private String recipient_address;
    private String recipient_phone;
    private String note;
    private int cod_amount;

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getRecipient_name() {
        return recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getRecipient_address() {
        return recipient_address;
    }

    public void setRecipient_address(String recipient_address) {
        this.recipient_address = recipient_address;
    }

    public String getRecipient_phone() {
        return recipient_phone;
    }

    public void setRecipient_phone(String recipient_phone) {
        this.recipient_phone = recipient_phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCod_amount() {
        return cod_amount;
    }

    public void setCod_amount(int cod_amount) {
        this.cod_amount = cod_amount;
    }
}
