package com.customer.management.service;

import com.customer.management.model.entity.Customer;
import com.customer.management.model.request.CustomerRequest;
import com.customer.management.model.response.CustomerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private CustomerService service;

    @BeforeEach
    void setUp() {
        service = new CustomerService();
    }

    @Test
    void testCalculateTier_Platinum() {
        Customer customer = Customer.builder()
                .annualSpend(new BigDecimal("15000"))
                .lastPurchaseDate(LocalDateTime.now().minusMonths(3))
                .build();

        String tier = service.calculateTier(customer);
        assertEquals("Platinum", tier);
    }

    @Test
    void testCalculateTier_Gold() {
        Customer customer = Customer.builder()
                .annualSpend(new BigDecimal("2000"))
                .lastPurchaseDate(LocalDateTime.now().minusMonths(9))
                .build();

        String tier = service.calculateTier(customer);
        assertEquals("Gold", tier);
    }

    @Test
    void testCalculateTier_Silver_dueToLowSpend() {
        Customer customer = Customer.builder()
                .annualSpend(new BigDecimal("500"))
                .lastPurchaseDate(LocalDateTime.now().minusMonths(2))
                .build();

        String tier = service.calculateTier(customer);
        assertEquals("Silver", tier);
    }

    @Test
    void testCalculateTier_Silver_dueToOldPurchase() {
        Customer customer = Customer.builder()
                .annualSpend(new BigDecimal("12000"))
                .lastPurchaseDate(LocalDateTime.now().minusMonths(7))
                .build();

        String tier = service.calculateTier(customer);
        assertEquals("Gold", tier); // Still meets gold criteria
    }

    @Test
    void testCalculateTier_Silver_nullSpend() {
        Customer customer = Customer.builder()
                .annualSpend(null)
                .lastPurchaseDate(LocalDateTime.now())
                .build();

        String tier = service.calculateTier(customer);
        assertEquals("Silver", tier);
    }

    @Test
    void testToResponse() {
        Customer customer = Customer.builder()
                .id(java.util.UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .annualSpend(new BigDecimal("5000"))
                .lastPurchaseDate(LocalDateTime.now().minusMonths(5))
                .build();

        CustomerResponse response = service.toResponse(customer);

        assertEquals(customer.getId(), response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals(new BigDecimal("5000"), response.getAnnualSpend());
        assertEquals(customer.getLastPurchaseDate(), response.getLastPurchaseDate());
        assertEquals("Gold", response.getTier()); // Based on spending and date
    }

    @Test
    void testFromRequest() {
        CustomerRequest request = new CustomerRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setAnnualSpend(new BigDecimal("3000"));
        request.setLastPurchaseDate(LocalDateTime.of(2024, 12, 25, 10, 0));

        Customer customer = service.fromRequest(request);

        assertEquals("Alice", customer.getName());
        assertEquals("alice@example.com", customer.getEmail());
        assertEquals(new BigDecimal("3000"), customer.getAnnualSpend());
        assertEquals(LocalDateTime.of(2024, 12, 25, 10, 0), customer.getLastPurchaseDate());
    }
}

