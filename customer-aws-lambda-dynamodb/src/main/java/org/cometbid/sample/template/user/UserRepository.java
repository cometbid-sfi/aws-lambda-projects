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
package org.cometbid.sample.template.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author samueladebowale
 */
@Repository
@Log4j2
public class UserRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public UserRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public User insertUser(User user) {
        dynamoDBMapper.save(user);
        return user;
    }

    /**
     * 
     * @param id
     * @return 
     */
    public User findUserById(String id) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1",new AttributeValue().withS(id));

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(eav);

        List<User> users = dynamoDBMapper.query(User.class,queryExpression);
        return users.get(0);
    }

    /**
     * 
     * @param term 
     */
    public void scanUserByBeginsWithUserName(String term){
        HashMap<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS(term));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("begins_with(username,:v1)")
                .withExpressionAttributeValues(eav);

        List<User> users =  dynamoDBMapper.scan(User.class, scanExpression);
       log.info(users.size());
    }

    /**
     * 
     * @param age 
     */
    public void scanUserByAgeLessThan(int age){
        HashMap<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withN(String.valueOf(age)));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("age < :v1")
                .withExpressionAttributeValues(eav);

        List<User> users =  dynamoDBMapper.scan(User.class, scanExpression);
        log.info(users.size());
    }

    /**
     * 
     * @param id
     * @param username
     * @return 
     */
    public String deleteUserById(String id, String username) {
        User user = dynamoDBMapper.load(User.class, id, username);
        dynamoDBMapper.delete(user);
        return "User deleted!";
    }

    /**
     * 
     * @param id
     * @param user
     * @return 
     */
    public String updateUser(String id, User user) {
        var existUser = findUserById(id);
        existUser.setAge(user.getAge());
        existUser.setSurname(user.getSurname());
        existUser.setAddress(user.getAddress());
        dynamoDBMapper.save(existUser);
        return id;
    }
}
