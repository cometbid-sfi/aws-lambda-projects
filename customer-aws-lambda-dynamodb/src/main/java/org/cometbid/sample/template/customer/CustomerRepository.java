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

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author samueladebowale
 */
@Repository
@AllArgsConstructor
public class CustomerRepository {

    private final DynamoDBMapper dynamoDBMapper;

    private final CustomerMapper customerMapper;

    /**
     * 
     * @return 
     */
    public List<Customer> findAll() {
        return dynamoDBMapper.scan(Customer.class, new DynamoDBScanExpression());
    }

    /**
     *
     * @param customer
     * @return
     */
    public String createCustomer(Customer customer) {
        dynamoDBMapper.save(customer);
        return customer.getId();
    }

    /**
     *
     * @param id
     * @return
     */
    public Customer getCustomerById(String id) {
        return dynamoDBMapper.load(Customer.class, id);
    }

    /**
     *
     * @param id
     * @param customer
     * @return
     */
    public Customer updateCustomer(String id, Customer customer) {
        Customer loadedCustomer = dynamoDBMapper.load(Customer.class, id);

        Customer newCustomer = customerMapper.updateEntity(loadedCustomer, customer);
        // map these two entity

        dynamoDBMapper.save(newCustomer);

        return dynamoDBMapper.load(Customer.class, id);
    }

    /**
     *
     * @param id
     * @return
     */
    public String deleteCustomer(String id) {
        Customer load = dynamoDBMapper.load(Customer.class, id);

        dynamoDBMapper.delete(load);
        return "Customer deleted successfully:: " +id;
    }
}
