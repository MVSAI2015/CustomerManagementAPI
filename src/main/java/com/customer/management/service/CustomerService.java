package com.customer.management.service;

import com.customer.management.model.entity.Customer;
import com.customer.management.model.request.CustomerRequest;
import com.customer.management.model.response.CustomerResponse;
import com.customer.management.reposiotry.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repository;

	public String calculateTier(Customer customer) {
		if (customer.getAnnualSpend() == null) return "Silver";
		BigDecimal spend = customer.getAnnualSpend();
		LocalDateTime lastPurchase = customer.getLastPurchaseDate();
		LocalDateTime now = LocalDateTime.now();

		if (spend.compareTo(BigDecimal.valueOf(10000)) >= 0 &&
				lastPurchase != null && lastPurchase.isAfter(now.minusMonths(6))) {
			return "Platinum";
		}
		if (spend.compareTo(BigDecimal.valueOf(1000)) >= 0 &&
				lastPurchase != null && lastPurchase.isAfter(now.minusMonths(12))) {
			return "Gold";
		}
		return "Silver";
	}

	public CustomerResponse toResponse(Customer customer) {
		return CustomerResponse.builder()
				.id(customer.getId())
				.name(customer.getName())
				.email(customer.getEmail())
				.annualSpend(customer.getAnnualSpend())
				.lastPurchaseDate(customer.getLastPurchaseDate())
				.tier(calculateTier(customer))
				.build();
	}

	public Customer fromRequest(CustomerRequest request) {
		return Customer.builder()
				.name(request.getName())
				.email(request.getEmail())
				.annualSpend(request.getAnnualSpend())
				.lastPurchaseDate(request.getLastPurchaseDate())
				.build();
	}
}