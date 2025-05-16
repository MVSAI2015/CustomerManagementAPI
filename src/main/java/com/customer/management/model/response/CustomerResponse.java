package com.customer.management.model.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
	private UUID id;
	private String name;
	private String email;
	private BigDecimal annualSpend;
	private LocalDateTime lastPurchaseDate;
	private String tier;
}
