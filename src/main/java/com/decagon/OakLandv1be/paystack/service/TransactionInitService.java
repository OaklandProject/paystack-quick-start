package com.decagon.OakLandv1be.paystack.service;

import com.decagon.OakLandv1be.paystack.dto.Amount;
import com.decagon.OakLandv1be.paystack.dto.TransactionInitResponseDto;
import org.json.JSONObject;

public interface TransactionInitService {


    JSONObject initTransaction(Amount amount) throws Exception;

    TransactionInitResponseDto verifyPayment(String reference);

    String finalizeTransaction(String reference);

//    JSONObject verifyTransaction(String reference);

//    JSONObject verifyTransaction(String reference);
}
