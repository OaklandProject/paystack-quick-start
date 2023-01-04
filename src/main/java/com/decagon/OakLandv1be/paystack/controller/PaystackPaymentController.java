package com.decagon.OakLandv1be.paystack.controller;


import com.decagon.OakLandv1be.paystack.dto.Amount;
import com.decagon.OakLandv1be.paystack.dto.TransactionInitResponseDto;
import com.decagon.OakLandv1be.paystack.service.TransactionInitService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaystackPaymentController {

    private final TransactionInitService service;

    @PostMapping( value = "/pay")
    public ResponseEntity<String> processPayment(@RequestBody Amount amount) throws Exception {
        JSONObject response = service.initTransaction(amount);

        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @GetMapping("/verify/{reference}")
    public ResponseEntity<Object> confirmPayment(@PathVariable String reference){

        TransactionInitResponseDto response = service.verifyPayment(reference);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
