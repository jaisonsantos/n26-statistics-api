package com.jaison.statisticsapi.service;

import com.jaison.statisticsapi.dto.ResponseMessageDto;
import com.jaison.statisticsapi.exception.ValidationException;
import com.jaison.statisticsapi.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    // error messages
    public static final String VALIDATION_JSON_NOT_PARSABLE = "Invalid json";
    public static final String VALIDATION_EMPTY_REQUEST_BODY = "Empty request body";
    public static final String VALIDATION_MISSING_TIMESTAMP = "Missing time stamp field";
    public static final String VALIDATION_MISSING_AMOUNT = "Missing amount field";
    public static final String VALIDATION_MISSING_AMOUNT_AND_TIMESTAMP = "Missing amount and timestamp field";
    public static final String VALIDATION_TRANSACTION_IS_IN_THE_FUTURE = "Transaction is in the future";

    private final StatisticsManagementService service;

    public TransactionServiceImpl(StatisticsManagementService service) {
        this.service = service;
    }

    @Override
    public ResponseMessageDto addTransaction(Transaction transaction) {
        ResponseMessageDto responseMessage;
        try {
            validate(transaction);

            if (modelOlderThan60Sec(transaction)) {
                responseMessage = new ResponseMessageDto(HttpStatus.NO_CONTENT, null);
            } else {
                service.addTransaction(transaction);
                responseMessage = new ResponseMessageDto(HttpStatus.CREATED, null);
            }

            if (isInTheFuture(transaction)) {
                responseMessage = new ResponseMessageDto(HttpStatus.UNPROCESSABLE_ENTITY, VALIDATION_TRANSACTION_IS_IN_THE_FUTURE);
            }

        } catch (ValidationException ve) {
            responseMessage = new ResponseMessageDto(HttpStatus.UNPROCESSABLE_ENTITY, VALIDATION_JSON_NOT_PARSABLE);
        }

        return responseMessage;
    }

    @Override
    public void deleteAllTransactions() {
        service.deleteStatistics();
    }

    private boolean modelOlderThan60Sec(Transaction transaction) {
        return service.isOlderThan(transaction.getTimestamp().toInstant());
    }

    private boolean isInTheFuture(Transaction transaction) {
        return service.isInTheFuture(transaction.getTimestamp().toInstant());
    }

    private void validate(Transaction transaction) {
        if (transaction == null)
            throw new ValidationException(VALIDATION_EMPTY_REQUEST_BODY);
        if (transaction.getAmount() == null && transaction.getTimestamp() == null)
            throw new ValidationException(VALIDATION_MISSING_AMOUNT_AND_TIMESTAMP);
        if (transaction.getTimestamp() == null)
            throw new ValidationException(VALIDATION_MISSING_TIMESTAMP);
        if (transaction.getAmount() == null)
            throw new ValidationException(VALIDATION_MISSING_AMOUNT);
    }
}
