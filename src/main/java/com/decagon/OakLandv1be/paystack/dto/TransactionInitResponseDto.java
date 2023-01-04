package com.decagon.OakLandv1be.paystack.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransactionInitResponseDto {

    private Boolean status;
    private String message;
    private Data data;
}
