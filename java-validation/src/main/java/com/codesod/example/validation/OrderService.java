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

import com.codesod.example.validation.rule.ErrorNotification;
import com.codesod.example.validation.rule.OrderItemValidator;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
class OrderService {
  private final OrderItemValidator validator;

  void createOrder(OrderDTO orderDTO) {
    ErrorNotification errorNotification = new ErrorNotification();
    orderDTO.getOrderItems()
        .stream()
        .map(validator::validate)
        .forEach(errorNotification::addAll);
    if (errorNotification.hasError()) {
      throw new IllegalArgumentException(errorNotification.getAllErrors());
    }

    log.info("Order {} saved", orderDTO);
  }
}
