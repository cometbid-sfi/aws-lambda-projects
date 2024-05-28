/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.sample.template.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.concurrent.CompletableFuture;
import org.cometbid.sample.template.customer.Customer;
import static org.hamcrest.Matchers.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author samueladebowale
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = DynanoDBInitializer.class)
public class RoutesTests extends DynanoDBInitializer {

    @Autowired
    DynamoDbAsyncClient dynamoDbAsyncClient;

    @Container
    public static GenericContainer dynamodb
            = new GenericContainer<>("amazon/dynamodb-local:latest")
                    .withExposedPorts(DYNAMODB_PORT);

    @Autowired
    public WebTestClient webTestClient;

    @Test
    public void shouldCreateCustomerWhenCustomerAPIInvoked() {

        // Create customers table in DynamoDB
        CompletableFuture<CreateTableResponse> createTable = dynamoDbAsyncClient.createTable(
                CreateTableRequest.builder()
                        .tableName("customers")
                        .attributeDefinitions(
                                AttributeDefinition.builder()
                                        .attributeName("customerId")
                                        .attributeType("S")
                                        .build()
                        )
                        .keySchema(
                                KeySchemaElement.builder()
                                        .attributeName("customerId")
                                        .keyType(KeyType.HASH)
                                        .build()
                        )
                        .provisionedThroughput(
                                ProvisionedThroughput.builder()
                                        .readCapacityUnits(5l)
                                        .writeCapacityUnits(5l)
                                        .build()
                        )
                        .build());

        Mono.fromFuture(createTable).block();

        Customer customer = Customer.create("John",
                "Sydney",
                "john@example.com");

        webTestClient
                .post()
                .uri("/customers")
                .bodyValue(customer)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader()
                .value("Location",
                        is(not(blankOrNullString())));
    }
}
