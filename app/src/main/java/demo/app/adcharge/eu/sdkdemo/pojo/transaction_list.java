package demo.app.adcharge.eu.sdkdemo.pojo;

import eu.adcharge.api.entities.Interest;

public class transaction_list {
    public String transacrion_date;
    public String transaction_order_number;
    public String transaction_amount;
    public String transaction_status;

    public transaction_list() {
    }

    public String getTransacrion_date() {
        return transacrion_date;
    }

    public void setTransacrion_date(String transacrion_date) {
        this.transacrion_date = transacrion_date;
    }

    public String getTransaction_order_number() {
        return transaction_order_number;
    }

    public void setTransaction_order_number(String transaction_order_number) {
        this.transaction_order_number = transaction_order_number;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }
}
