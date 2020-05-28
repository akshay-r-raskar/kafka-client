package com.example.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.example.model.CuratedCustomer;
import com.example.model.Customer;

@Service
public class AvroProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AvroProcessor.class);

	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public Message<CuratedCustomer> consume(Customer customer) {
		LOGGER.info("New event received: {}", customer);
		return curate(customer);
	}

	private Message<CuratedCustomer> curate(Customer customer) {

		CuratedCustomer curated = new CuratedCustomer();
		curated.setCustomerNumber(customer.getCustomerNumber());
		curated.setCustomerName(customer.getCustomerName());
		curated.setContactFirstName(customer.getContactFirstName());
		curated.setContactLastName(customer.getContactLastName());
		curated.setCountry(customer.getCountry());
		curated.setState(customer.getState());
		Message<CuratedCustomer> message = MessageBuilder.withPayload(curated).setHeader(KafkaHeaders.MESSAGE_KEY, customer.getCustomerNumber())
				.build();
		return message;
	}

}
