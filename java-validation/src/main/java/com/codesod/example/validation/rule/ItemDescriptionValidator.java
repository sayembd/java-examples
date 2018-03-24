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

class ItemDescriptionValidator implements OrderItemValidator {
  static final String MISSING_ITEM_DESCRIPTION = "Item description should be provided";

  @Override
  public ErrorNotification validate(OrderItem orderItem) {
    ErrorNotification errorNotification = new ErrorNotification();
    Optional.ofNullable(orderItem)
        .map(OrderItem::getDescription)
        .map(String::trim)
        .filter(description -> !description.isEmpty())
        .ifPresentOrElse(
            description -> {},
            () -> errorNotification.addError(MISSING_ITEM_DESCRIPTION)
        );
    return errorNotification;
  }
}
