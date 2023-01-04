package com.decagon.OakLandv1be.paystack.service;

import com.decagon.OakLandv1be.paystack.utils.ApiConnection;
import com.decagon.OakLandv1be.paystack.utils.ApiQuery;
import com.decagon.OakLandv1be.paystack.dto.Amount;
import com.decagon.OakLandv1be.paystack.dto.TransactionInitRequestDto;
import com.decagon.OakLandv1be.paystack.dto.TransactionInitResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class TransactionInitServiceImpl implements TransactionInitService {

    private ApiConnection apiConnection;

    private final String key = "sk_test_26cc81b3fc91a4e6cd2002ba7f2beeec550cb07f";

    @Override
    public JSONObject initTransaction(Amount amount) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        String amountInKobo = amount.getAmount() + "00";

        TransactionInitRequestDto request = new TransactionInitRequestDto();
        request.setAmount(amountInKobo);
        request.setEmail(email);

        apiConnection = new ApiConnection("https://api.paystack.co/transaction/initialize");

        ApiQuery query = new ApiQuery();
        query.putParams("amount", request.getAmount());
        query.putParams("email", request.getEmail());
        query.putParams("reference", request.getReference());
        query.putParams("callback_url", request.getCallback_url());

        return apiConnection.connectAndQuery(query);

    }


    @Override
    public TransactionInitResponseDto verifyPayment(String reference) {

        TransactionInitResponseDto responseDto = null;
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("https://api.paystack.co/transaction/verify/" + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + key);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

            } else {
                throw new Exception("Error Occured while connecting to paystack url");
            }
            ObjectMapper mapper = new ObjectMapper();

            responseDto = mapper.readValue(result.toString(), TransactionInitResponseDto.class);

            if (responseDto == null || responseDto.getStatus().equals("false")) {
                throw new Exception("An error occurred while  verifying payment");
            } else if (responseDto.getData().getStatus().equals("success")) {
                //PAYMENT IS SUCCESSFUL, APPLY VALUE TO THE TRANSACTION
                responseDto.setStatus(true);
                responseDto.setMessage("Payment successful");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseDto;

    }


}
