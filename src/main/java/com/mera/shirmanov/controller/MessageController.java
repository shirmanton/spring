package com.mera.shirmanov.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mera.shirmanov.configs.ApplicationProperties;
import com.mera.shirmanov.avro.Customer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@AllArgsConstructor
public class MessageController {

	private final ApplicationProperties properties;
	private final KafkaTemplate<String, Customer> kafkaTemplate;
	private final ObjectMapper mapper;

	@GetMapping("/send")
	public ResponseEntity<String> sendMessage(
			@RequestParam(name = "firstName") String firstName,
			@RequestParam(name = "lastName") String lastName,
			@RequestParam(name = "phoneNumber") String phoneNumber) throws JsonProcessingException {

		Customer customer = new Customer(firstName, lastName, phoneNumber);
		kafkaTemplate.send(
				properties.getKafka().getTopic().getName(),
				properties.getKafka().getTopic().getCustomerKey(),
				customer);
		return new ResponseEntity<>(customer.toString(), HttpStatus.OK);
	}
}
