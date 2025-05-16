package com.customer.management.controller;

import com.customer.management.model.entity.Customer;
import com.customer.management.model.request.CustomerRequest;
import com.customer.management.model.response.CustomerResponse;
import com.customer.management.reposiotry.CustomerRepository;
import com.customer.management.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private CustomerService service;

	@PostMapping
	public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest request) {
		Customer saved = repository.save(service.fromRequest(request));
		return ResponseEntity.status(201).body(service.toResponse(saved));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponse> getById(@PathVariable UUID id) {
		return repository.findById(id)
				.map(service::toResponse)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping(params = "name")
	public ResponseEntity<CustomerResponse> getByName(@RequestParam String name) {
		return repository.findByName(name)
				.map(service::toResponse)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping(params = "email")
	public ResponseEntity<CustomerResponse> getByEmail(@RequestParam String email) {
		return repository.findByEmail(email)
				.map(service::toResponse)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id, @RequestBody @Valid CustomerRequest request) {
		return repository.findById(id)
				.map(existing -> {
					existing.setName(request.getName());
					existing.setEmail(request.getEmail());
					existing.setAnnualSpend(request.getAnnualSpend());
					existing.setLastPurchaseDate(request.getLastPurchaseDate());
					return ResponseEntity.ok(service.toResponse(repository.save(existing)));
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
