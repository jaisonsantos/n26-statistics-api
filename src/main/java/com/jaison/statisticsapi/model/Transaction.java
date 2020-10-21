package com.jaison.statisticsapi.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Transaction {

    private BigDecimal amount;
    private OffsetDateTime timestamp;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, OffsetDateTime timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
