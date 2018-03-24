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
package com.codesod.example.validation.rule;

import com.codesod.example.validation.OrderDTO.OrderItem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class QuantityValidatorTest {

  @Test
  public void validate_quantityIsNull_invalid() {
    QuantityValidator validator = new QuantityValidator();

    ErrorNotification errorNotification = validator.validate(new OrderItem());

    assertThat(errorNotification.getAllErrors())
        .isEqualTo(QuantityValidator.MISSING_QUANTITY_ERROR);
  }

  @Test
  public void validate_quantityIsZero_invalid() {
    OrderItem orderItem = new OrderItem();
    int quantity = 0;
    orderItem.setQuantity(quantity);
    QuantityValidator validator = new QuantityValidator();

    ErrorNotification errorNotification = validator.validate(orderItem);

    assertThat(errorNotification.getAllErrors())
        .isEqualTo(String.format(QuantityValidator.INVALID_QUANTITY_ERROR, quantity));
  }

  @Test
  public void validate_quantityNegative_invalid() {
    OrderItem orderItem = new OrderItem();
    int quantity = -1;
    orderItem.setQuantity(quantity);
    QuantityValidator validator = new QuantityValidator();

    ErrorNotification errorNotification = validator.validate(orderItem);

    assertThat(errorNotification.getAllErrors())
        .isEqualTo(String.format(QuantityValidator.INVALID_QUANTITY_ERROR, quantity));
  }

  @Test
  public void validate_quantityValid_validated() {
    OrderItem orderItem = new OrderItem();
    orderItem.setQuantity(5);
    QuantityValidator validator = new QuantityValidator();

    ErrorNotification errorNotification = validator.validate(orderItem);

    assertThat(errorNotification.getAllErrors()).isEmpty();
  }
}
