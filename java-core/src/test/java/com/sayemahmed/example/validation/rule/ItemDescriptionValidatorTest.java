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
package com.sayemahmed.example.validation.rule;

import static org.assertj.core.api.Assertions.assertThat;

import com.sayemahmed.example.validation.OrderDTO;
import org.junit.jupiter.api.Test;

public class ItemDescriptionValidatorTest {

  @Test
  public void validate_descriptionIsNull_invalid() {
    ItemDescriptionValidator validator = new ItemDescriptionValidator();

    ErrorNotification errorNotification = validator.validate(new OrderDTO.OrderItem());

    assertThat(errorNotification.getAllErrors())
        .isEqualTo(ItemDescriptionValidator.MISSING_ITEM_DESCRIPTION);
  }

  @Test
  public void validate_descriptionIsBlank_invalid() {
    OrderDTO.OrderItem orderItem = new OrderDTO.OrderItem();
    orderItem.setDescription("     ");
    ItemDescriptionValidator validator = new ItemDescriptionValidator();

    ErrorNotification errorNotification = validator.validate(orderItem);

    assertThat(errorNotification.getAllErrors())
        .isEqualTo(ItemDescriptionValidator.MISSING_ITEM_DESCRIPTION);
  }

  @Test
  public void validate_descriptionGiven_valid() {
    OrderDTO.OrderItem orderItem = new OrderDTO.OrderItem();
    orderItem.setDescription("dummy description");
    ItemDescriptionValidator validator = new ItemDescriptionValidator();

    ErrorNotification errorNotification = validator.validate(orderItem);

    assertThat(errorNotification.getAllErrors()).isEmpty();
  }
}
