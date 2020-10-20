package com.jaison.statisticsapi.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

public class Transaction {

    private BigDecimal amount;
    private OffsetDateTime timestamp;

    public Transaction() {
    }

    public Transaction(String amount, OffsetDateTime timestamp) {
        this.amount = setScale(new BigDecimal(amount));
        this.timestamp = timestamp;
    }

    public BigDecimal setScale(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}
