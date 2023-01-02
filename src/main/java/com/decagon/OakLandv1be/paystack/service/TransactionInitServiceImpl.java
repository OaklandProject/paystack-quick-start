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
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class TransactionInitServiceImpl implements TransactionInitService {

    RestTemplate restTemplate = new RestTemplate();

    private ApiConnection apiConnection;

    private final String key = "sk_test_26cc81b3fc91a4e6cd2002ba7f2beeec550cb07f";

    @Override
    public JSONObject initTransaction(Amount amount) throws Exception {

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//
//        String amountInKobo = amount + "00";
//        TransactionInitRequestDto request = new TransactionInitRequestDto();
//        request.setEmail(email);
//        request.setAmount(amountInKobo);
//
//        TransactionInitResponseDto transactionInitResponseDto = null;
//        try {
//            // convert transaction to json then use it as a body to post json
//            Gson gson = new Gson();
//            // add paystack charges to the amount
//            StringEntity postingString = new StringEntity(gson.toJson(request));
//            HttpClient client = HttpClientBuilder.create().build();
//            HttpPost post = new
//                    HttpPost("https://api.paystack.co/transaction/initialize");
//            post.setEntity(postingString);
//            post.addHeader("Content-type", "application/json");
//            post.addHeader("Authorization", "Bearer " + key);
//                    StringBuilder result = new StringBuilder();
//            HttpResponse response = client.execute(post);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                BufferedReader rd = new BufferedReader(new
//                        InputStreamReader(response.getEntity().getContent()));
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    result.append(line);
//                }
//            } else {
//                throw new UnavailableException("Error Occurred while initializing transaction");
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            transactionInitResponseDto = mapper.readValue(result.toString(),
//                    TransactionInitResponseDto.class);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new Exception("Failure initializaing paystack transaction");
//        }
//        return transactionInitResponseDto;

//=============================================================================================================
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//
//        String amountToKobo = amount.getAmount()+"00";
//        TransactionInitRequestDto request = new TransactionInitRequestDto();
//        request.setAmount(amountToKobo);
//        request.setEmail(email);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization","Bearer " + key);
//        HttpEntity<TransactionInitRequestDto> entity = new HttpEntity<>(request, headers);
//        ResponseEntity<TransactionInitResponseDto> response = restTemplate.postForEntity("https://api.paystack.co/transaction/initialize", entity, TransactionInitResponseDto.class);
//        return response.getBody();

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
    public TransactionInitResponseDto verifyPayment(String reference){
//        String status = null;
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet("https://api.paystack.co/transaction/verify/" + reference);
//        request.addHeader("Content-type", "application/json");
//        request.addHeader("Authorization", "Bearer " + key);
//
//        HttpResponse response = client.execute(request);
//
//        if (response.getStatusLine().getStatusCode() == 200) {
//            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            int count  = 0;
//            String line;
//            while ((line = rd.readLine()) != null) {
//                line = line.trim();
//                if(line.startsWith("\"status\"") && count > 2){
//                    status = checkLine(line);
//                }
//                count++;
//            }
//        } else {
//            throw new IOException("Error Occurred while connecting to pay-stack url");
//        }
//        return status;

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


                ObjectMapper mapper = new ObjectMapper();

                responseDto = mapper.readValue(result.toString(), TransactionInitResponseDto.class);
            } else {
                throw new Exception("Error Occured while connecting to paystack url");
            }


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

//    @Override
//    public JSONObject verifyTransaction(String reference) {
//        apiConnection = new ApiConnection("https://api.paystack.co/transaction/verify/" + reference);
//        return apiConnection.connectAndQueryWithGet();
//    }
//
//
//    private String checkLine(String line){
//        if(line.contains("success")){
//            return "success";
//        }else if(line.contains("abandoned"))return "abandoned";
//        else return "failed";
//    }

}
