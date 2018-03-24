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

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;

class PriceValidator implements OrderItemValidator {
  static final String PRICE_EMPTY_ERROR = "Price cannot be empty.";
  static final String PRICE_INVALID_ERROR_FORMAT = "Given price [%s] is not in valid format";

  @Override
  public ErrorNotification validate(OrderItem orderItem) {
    ErrorNotification errorNotification = new ErrorNotification();
    Optional.ofNullable(orderItem)
        .map(OrderItem::getPrice)
        .map(String::trim)
        .filter(itemPrice -> !itemPrice.isEmpty())
        .ifPresentOrElse(
            validatePriceFormat(errorNotification),
            () -> errorNotification.addError(PRICE_EMPTY_ERROR)
        );
    return errorNotification;
  }

  private Consumer<? super String> validatePriceFormat(ErrorNotification errorNotification) {
    return itemPrice -> {
      try {
        new BigDecimal(itemPrice);
      } catch (NumberFormatException ex) {
        errorNotification.addError(String.format(PRICE_INVALID_ERROR_FORMAT, itemPrice));
      }
    };
  }
}
