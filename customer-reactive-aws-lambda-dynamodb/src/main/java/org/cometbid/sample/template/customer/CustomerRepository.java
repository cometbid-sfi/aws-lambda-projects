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
package org.cometbid.sample.template.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @author samueladebowale
 */
@Repository
public class CustomerRepository {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;
    private final CustomerMapper customerMapper;
    private final String customerTable;

    public CustomerRepository(DynamoDbAsyncClient dynamoDbAsyncClient,
            CustomerMapper customerMapper,
            @Value("${application.dynamodb.customer_table}") String customerTable) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
        this.customerMapper = customerMapper;
        this.customerTable = customerTable;
    }

    public Flux<Customer> listCustomers() {

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(customerTable)
                .build();

        return Mono.fromCompletionStage(dynamoDbAsyncClient.scan(scanRequest))
                .map(scanResponse -> scanResponse.items())
                .map(CustomerMapper::fromList)
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Customer> createCustomer(Customer customer) {

        Customer localCustomer = this.customerMapper.cloneWithId(UUID.randomUUID().toString(), customer);

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(customerTable)
                .item(CustomerMapper.toMap(localCustomer))
                .build();

        return Mono.fromCompletionStage(dynamoDbAsyncClient.putItem(putItemRequest))
                .map(putItemResponse -> putItemResponse.attributes())
                .map(attributeValueMap -> localCustomer);
    }

    public Mono<String> deleteCustomer(String customerId) {
        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(customerTable)
                .key(Map.of("customerId", AttributeValue.builder().s(customerId).build()))
                .build();

        return Mono.fromCompletionStage(dynamoDbAsyncClient.deleteItem(deleteItemRequest))
                .map(deleteItemResponse -> deleteItemResponse.attributes())
                .map(attributeValueMap -> customerId);
    }

    public Mono<Customer> getCustomer(String customerId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(customerTable)
                .key(Map.of("customerId", AttributeValue.builder().s(customerId).build()))
                .build();

        return Mono.fromCompletionStage(dynamoDbAsyncClient.getItem(getItemRequest))
                .map(getItemResponse -> getItemResponse.item())
                .map(CustomerMapper::fromMap);
    }

    public Mono<String> updateCustomer(String customerId, Customer customer) {

        Customer localCustomer = this.customerMapper.cloneWithId(customerId, customer);
        
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(customerTable)
                .item(CustomerMapper.toMap(localCustomer))
                .build();

        return Mono.fromCompletionStage(dynamoDbAsyncClient.putItem(putItemRequest))
                .map(updateItemResponse -> customerId);
    }
}
