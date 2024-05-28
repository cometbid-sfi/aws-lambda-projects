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
package org.cometbid.sample.template;

import lombok.extern.log4j.Log4j2;
import org.cometbid.sample.template.user.Address;
import org.cometbid.sample.template.user.User;
import org.cometbid.sample.template.user.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@SpringBootApplication
public class CustomerAwsLambdaDynamodb {

    public static void main(String[] args) {
        SpringApplication.run(CustomerAwsLambdaDynamodb.class, args);
    }
    
    @Bean
    public ApplicationRunner runner(UserRepository userRepository){
        return args -> {
            log.info("Spring Boot Aws DynamoDB integration");

            // Save User
            var address = Address.builder().city("Test City").street("Test Street").build();
            var user = User.builder().address(address).username("user name").surname("user surname").age(25).build();
            var savedUser = userRepository.insertUser(user);

            var id = savedUser.getId();

            // Update the user
            user.setAge(18);
            userRepository.updateUser(id, user);

            // Find User By Id
            var existUser = userRepository.findUserById(id);
            log.info(String.format("Exist User Name: %s", existUser.getUsername()));

            log.info(id);

            userRepository.scanUserByBeginsWithUserName("edit");
            userRepository.scanUserByAgeLessThan(20);

            // Will delete saved user.
            // userRepository.deleteUserById(id, existUser.getUsername());
        };
    }
}
