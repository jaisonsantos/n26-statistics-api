package com.jaison.statisticsapi.service;

import com.jaison.statisticsapi.dto.ResponseMessageDto;
import com.jaison.statisticsapi.exception.ValidationException;
import com.jaison.statisticsapi.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    // error messages
    public static final String JSON_NOT_PARSABLE = "Not valid json: ";
    public static final String INVALID_TRANSACTION = "Transaction is older than 60 secs";
    public static final String VALIDATION_EMPTY_REQUEST_BODY = "Empty request body";
    public static final String VALIDATION_MISSING_TIMESTAMP = "Missing time stamp field";
    public static final String VALIDATION_MISSING_AMOUNT = "Missing amount field";
    public static final String VALIDATION_MISSING_AMOUNT_AND_TIMESTAMP = "Missing amount and timestamp field";

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
                responseMessage = new ResponseMessageDto(HttpStatus.CREATED, INVALID_TRANSACTION);
            } else {
                service.addTransaction(transaction);
                responseMessage = new ResponseMessageDto(HttpStatus.NO_CONTENT, null);
            }
        } catch (ValidationException ve) {
            responseMessage = new ResponseMessageDto(HttpStatus.UNPROCESSABLE_ENTITY, JSON_NOT_PARSABLE + ve.getMessage());
        }

        return responseMessage;
    }

    @Override
    public void deleteTransactions() {
        service.deleteStatistics();
    }

    private boolean modelOlderThan60Sec(Transaction transaction) {
        return service.isOlderThan(transaction.getTimestamp());
    }

    private void validate(Transaction transaction) throws ValidationException {

        if (transaction == null)
            throw new ValidationException(VALIDATION_EMPTY_REQUEST_BODY);
        if (transaction.getAmount() == null && transaction.getTimestamp() == null)
            throw new ValidationException(VALIDATION_MISSING_AMOUNT_AND_TIMESTAMP);
        if (transaction.getTimestamp() == null || transaction.getTimestamp().toString().isEmpty())
            throw new ValidationException(VALIDATION_MISSING_TIMESTAMP);
        if (transaction.getAmount() == null)
            throw new ValidationException(VALIDATION_MISSING_AMOUNT);
    }
}
