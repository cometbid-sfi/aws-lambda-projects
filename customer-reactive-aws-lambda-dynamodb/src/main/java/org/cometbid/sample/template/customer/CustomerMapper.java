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

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Mapper(componentModel = "spring")
public abstract class CustomerMapper {

    public static List<Customer> fromList(List<Map<String, AttributeValue>> items) {
        return items.stream()
                .map(CustomerMapper::fromMap)
                .collect(Collectors.toList());
    }

    public static Customer fromMap(Map<String, AttributeValue> attributeValueMap) {
        String id = attributeValueMap.get("customerId").s();
        String name = attributeValueMap.get("name").s();
        String email = attributeValueMap.get("email").s();
        String city = attributeValueMap.get("city").s();

        return Customer.builder().id(id)
                .name(name).email(email)
                .city(city)
                .build();
    }

    public static Map<String, AttributeValue> toMap(Customer customer) {
        return Map.of(
                "customerId", AttributeValue.builder().s(customer.id()).build(),
                "name", AttributeValue.builder().s(customer.name()).build(),
                "email", AttributeValue.builder().s(customer.email()).build(),
                "city", AttributeValue.builder().s(customer.city()).build()
        );
    }
    
    @Mapping(source = "id", target = "id")
    abstract Customer cloneWithId(String id, Customer customer);
}
