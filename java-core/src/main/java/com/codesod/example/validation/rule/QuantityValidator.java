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

import java.util.Optional;
import java.util.function.Consumer;

class QuantityValidator implements OrderItemValidator {
  static final String MISSING_QUANTITY_ERROR = "Quantity must be given";
  static final String INVALID_QUANTITY_ERROR = "Given quantity %s is not valid.";

  @Override
  public ErrorNotification validate(OrderItem orderItem) {
    ErrorNotification errorNotification = new ErrorNotification();
    Optional.ofNullable(orderItem)
        .map(OrderItem::getQuantity)
        .ifPresentOrElse(
            validateQuantity(errorNotification),
            () -> errorNotification.addError(MISSING_QUANTITY_ERROR)
        );
    return errorNotification;
  }

  private Consumer<? super Integer> validateQuantity(ErrorNotification errorNotification) {
    return quantity -> {
      if (quantity <= 0) {
        errorNotification.addError(String.format(INVALID_QUANTITY_ERROR, quantity));
      }
    };
  }
}
