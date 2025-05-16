package com.customer.management.controller;


import com.customer.management.model.entity.Customer;
import com.customer.management.model.request.CustomerRequest;
import com.customer.management.model.response.CustomerResponse;
import com.customer.management.reposiotry.CustomerRepository;
import com.customer.management.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @InjectMocks
    private CustomerController controller;

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerService service;

    private AutoCloseable closeable;

    private UUID id;
    private Customer customer;
    private CustomerRequest request;
    private CustomerResponse response;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        customer = new Customer();
        customer.setId(id);
        customer.setName("John");
        customer.setEmail("john@example.com");
        customer.setAnnualSpend(new BigDecimal("11000"));
        customer.setLastPurchaseDate(LocalDateTime.now());

        request = new CustomerRequest();
        request.setName("John");
        request.setEmail("john@example.com");
        request.setAnnualSpend(new BigDecimal("11000"));
        request.setLastPurchaseDate(customer.getLastPurchaseDate());


    }

    @Test
    void testCreateCustomer() {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID())
                .email("abc@gmail.com")
                .annualSpend(new BigDecimal("1000"))
                .lastPurchaseDate(LocalDateTime.parse("2023-12-25T10:30"))
                .build();
        when(service.fromRequest(request)).thenReturn(customer);
        when(repository.save(customer)).thenReturn(customer);
        when(service.toResponse(customer)).thenReturn(response);

        ResponseEntity<CustomerResponse> result = controller.createCustomer(request);

        assertEquals(201, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetById_Found() {
        UUID uuid = UUID.randomUUID();
        Customer customer = Customer.builder()
                .id(uuid)
                .email("abc@gmail.com")
                .annualSpend(new BigDecimal("1000"))
                .lastPurchaseDate(LocalDateTime.parse("2023-12-25T10:30"))
                .build();
        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(uuid)
                .name("Sai")
                .email("abc@gmail.com")
                .annualSpend(new BigDecimal("1000"))
                .lastPurchaseDate(LocalDateTime.parse("2023-12-25T10:30"))
                .build();
        when(repository.findById(uuid)).thenReturn(Optional.of(customer));
        when(service.toResponse(customer)).thenReturn(customerResponse);


        ResponseEntity<CustomerResponse> result = controller.getById(uuid);

        assertEquals("Sai", result.getBody().getName());
    }

    @Test
    void testGetById_NotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<CustomerResponse> result = controller.getById(id);

        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    void testGetByName_NotFound() {
        when(repository.findByName("Jane")).thenReturn(Optional.empty());

        ResponseEntity<CustomerResponse> result = controller.getByName("Jane");

        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    void testGetByEmail_Found() {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID())
                .email("abc@gmail.com")
                .annualSpend(new BigDecimal("1000"))
                .lastPurchaseDate(LocalDateTime.parse("2023-12-25T10:30"))
                .build();
        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(UUID.randomUUID())
                .email("abc@gmail.com")
                .annualSpend(new BigDecimal("1000"))
                .lastPurchaseDate(LocalDateTime.parse("2023-12-25T10:30"))
                .build();

        when(repository.findByEmail("abc@gmail.com")).thenReturn(Optional.of(customer));
        when(service.toResponse(customer)).thenReturn(customerResponse);

        ResponseEntity<CustomerResponse> result = controller.getByEmail("abc@gmail.com");

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(customerResponse, result.getBody());
    }

    @Test
    void testGetByEmail_NotFound() {
        when(repository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        ResponseEntity<CustomerResponse> result = controller.getByEmail("notfound@example.com");

        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    void testUpdateCustomer_Found() {
        when(repository.findById(id)).thenReturn(Optional.of(customer));
        when(repository.save(any())).thenReturn(customer);
        when(service.toResponse(any())).thenReturn(response);

        ResponseEntity<CustomerResponse> result = controller.updateCustomer(id, request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
        verify(repository).save(customer);
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<CustomerResponse> result = controller.updateCustomer(id, request);

        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    void testDeleteCustomer_Found() {
        when(repository.existsById(id)).thenReturn(true);

        ResponseEntity<Void> result = controller.deleteCustomer(id);

        assertEquals(204, result.getStatusCodeValue());
        verify(repository).deleteById(id);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(repository.existsById(id)).thenReturn(false);

        ResponseEntity<Void> result = controller.deleteCustomer(id);

        assertEquals(404, result.getStatusCodeValue());
    }
}
