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

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.stream.Collectors;

/**
 *
 * @author samueladebowale
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> listCustomers(ServerRequest serverRequest) {
        return customerRepository.listCustomers()
                .collect(Collectors.toList())
                .flatMap(customers -> ServerResponse.ok().body(BodyInserters.fromValue(customers)));
    }

    /**
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> createCustomer(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(Customer.class)
                .flatMap(customer -> customerRepository.createCustomer(customer))
                .flatMap(customer -> ServerResponse.created(URI.create("/customers/" + customer.id())).build());
    }

    /**
     * 
     * @param serverRequest
     * @return 
     */
    public Mono<ServerResponse> deleteCustomer(ServerRequest serverRequest) {
        String customerId = serverRequest.pathVariable("customerId");

        return customerRepository.deleteCustomer(customerId)
                .flatMap(customer -> ServerResponse.ok().build());
    }

    /**
     * 
     * @param serverRequest
     * @return 
     */
    public Mono<ServerResponse> getCustomer(ServerRequest serverRequest) {
        String customerId = serverRequest.pathVariable("customerId");

        return customerRepository.getCustomer(customerId)
                .flatMap(customer -> ServerResponse.ok().body(BodyInserters.fromValue(customer)));
    }

    /**
     * 
     * @param serverRequest
     * @return 
     */
    public Mono<ServerResponse> updateCustomer(ServerRequest serverRequest) {
        String customerId = serverRequest.pathVariable("customerId");

        return serverRequest.bodyToMono(Customer.class)
                .flatMap(customer -> customerRepository.updateCustomer(customerId, customer))
                .flatMap(customer -> ServerResponse.ok().build());
    }
}
