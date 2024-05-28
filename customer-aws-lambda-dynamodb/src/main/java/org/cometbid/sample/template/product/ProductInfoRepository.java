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
package org.cometbid.sample.template.product;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

/**
 *
 * @author samueladebowale
 */
@Repository
@AllArgsConstructor
public class ProductInfoRepository {

    private final DynamoDBMapper dynamoDBMapper;

    private final ProductMapper productMapper;
    
    private final DynamoDbTable<ProductInfo> table;

    /**
     * 
     * @return 
     */
    public List<ProductInfo> findAll() {
        return table.scan().items().stream().toList();
    }

    /**
     *
     * @param product
     * @return
     */
    public String createProduct(ProductInfo product) {
        dynamoDBMapper.save(product);
        return product.getId();
    }

    /**
     *
     * @param id
     * @return
     */
    public ProductInfo getProductById(String id) {
        return dynamoDBMapper.load(ProductInfo.class, id);
    }

    /**
     *
     * @param id
     * @param product
     * @return
     */
    public ProductInfo updateProduct(String id, ProductInfo product) {
        ProductInfo loadedProduct = dynamoDBMapper.load(ProductInfo.class, id);

        ProductInfo newCustomer = productMapper.updateEntity(loadedProduct, product);
        // map these two entity

        dynamoDBMapper.save(newCustomer);

        return dynamoDBMapper.load(ProductInfo.class, id);
    }

    /**
     *
     * @param id
     * @return
     */
    public String deleteProduct(String id) {
        ProductInfo load = dynamoDBMapper.load(ProductInfo.class, id);

        dynamoDBMapper.delete(load);
        return load.getId() + " get deleted !";
    }

}
