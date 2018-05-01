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

import com.codesod.example.validation.MenuRepository;
import com.codesod.example.validation.OrderDTO.OrderItem;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MenuValidator implements OrderItemValidator {
  private final MenuRepository menuRepository;

  static final String MISSING_MENU_ERROR = "A menu item must be specified.";
  static final String INVALID_MENU_ERROR_FORMAT = "Given menu [%s] does not exist.";

  @Override
  public ErrorNotification validate(OrderItem orderItem) {
    ErrorNotification errorNotification = new ErrorNotification();
    Optional.ofNullable(orderItem.getMenuId())
        .map(String::trim)
        .filter(menuId -> !menuId.isEmpty())
        .ifPresentOrElse(
            validateMenuExists(errorNotification),
            () -> errorNotification.addError(MISSING_MENU_ERROR)
        );
    return errorNotification;
  }

  private Consumer<String> validateMenuExists(ErrorNotification errorNotification) {
    return menuId -> {
      if (!menuRepository.menuExists(menuId)) {
        errorNotification.addError(String.format(INVALID_MENU_ERROR_FORMAT, menuId));
      }
    };
  }
}
