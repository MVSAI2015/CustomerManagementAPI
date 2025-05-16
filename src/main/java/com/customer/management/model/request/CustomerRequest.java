package com.customer.management.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
	private String name;
	private String email;
	private BigDecimal annualSpend;
	private LocalDateTime lastPurchaseDate;
}
