package com.ocoelhogabriel.orders_monolith;

import org.springframework.boot.SpringApplication;

public class TestOrdersMonolithApplication {

	public static void main(String[] args) {
		SpringApplication.from(OrdersMonolithApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
