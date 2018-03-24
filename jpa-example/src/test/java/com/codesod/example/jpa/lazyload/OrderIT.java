/*
 * Copyright 2018 MD Sayem Ahmed
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codesod.example.jpa.lazyload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author MD Sayem Ahmed
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderIT {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Test
  public void should_fetch_orders() {
    Product savedProduct = createTestProduct();
    Customer customer = new Customer(UUID.randomUUID().toString(), "Test Customer", "test@codesod.com");

    DiscountItem discountItem = new DiscountItem(UUID.randomUUID().toString(), new BigDecimal("10"));

    assertThat(savedProduct).isNotNull();
  }

  private Product createTestProduct() {
    PriceValidityPeriod priceValidityPeriod = new PriceValidityPeriod(OffsetDateTime.now(), OffsetDateTime.now().plusMonths(5));
    ProductPrice productPrice = new ProductPrice(UUID.randomUUID().toString(), new BigDecimal("1"), priceValidityPeriod);
    Product product = new Product(UUID.randomUUID().toString(), "Ice Cream", productPrice);

    return productRepository.save(product);
  }
}
