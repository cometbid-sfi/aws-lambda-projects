/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.cometbid.sample.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 *
 * @author samueladebowale
 */
@EnableWebFlux
@SpringBootApplication
public class CustomerReactiveAwsLambdaDynamodb {

    public static void main(String[] args) {
        SpringApplication.run(CustomerReactiveAwsLambdaDynamodb.class, args);
    }
}
