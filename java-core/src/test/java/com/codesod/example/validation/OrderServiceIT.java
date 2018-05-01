/*
 * Copyright 2017 MD Sayem Ahmed
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
package com.codesod.example.validation;

import com.codesod.example.validation.OrderDTO.OrderItem;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceIT {

  @Autowired
  private OrderService orderService;

  @Test
  public void createOrder_orderValid_noError() {
    OrderItem orderItem = createOrderItem();
    OrderDTO orderDTO = createOrder(orderItem);

    orderService.createOrder(orderDTO);
  }

  @Test
  public void createOrder_priceInvalid_error() {
    OrderItem orderItem = createOrderItem();
    orderItem.setPrice("string");
    OrderDTO orderDTO = createOrder(orderItem);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> orderService.createOrder(orderDTO));
  }

  @Test
  public void createOrder_descriptionInvalid_error() {
    OrderItem orderItem = createOrderItem();
    orderItem.setDescription(null);
    OrderDTO orderDTO = createOrder(orderItem);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> orderService.createOrder(orderDTO));
  }

  @Test
  public void createOrder_quantityInvalid_error() {
    OrderItem orderItem = createOrderItem();
    orderItem.setQuantity(0);
    OrderDTO orderDTO = createOrder(orderItem);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> orderService.createOrder(orderDTO));
  }

  @Test
  public void createOrder_menuInvalid_error() {
    OrderItem orderItem = createOrderItem();
    orderItem.setMenuId(null);
    OrderDTO orderDTO = createOrder(orderItem);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> orderService.createOrder(orderDTO));
  }

  private OrderDTO createOrder(OrderItem orderItem) {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setCustomerId("customer id");
    orderDTO.setOrderItems(Collections.singletonList(orderItem));
    return orderDTO;
  }

  private OrderItem createOrderItem() {
    OrderItem orderItem = new OrderItem();
    orderItem.setPrice("101");
    orderItem.setDescription("Item description goes here");
    orderItem.setMenuId("menu id");
    orderItem.setQuantity(5);
    return orderItem;
  }
}
