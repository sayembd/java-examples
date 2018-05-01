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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class MenuValidatorTest {

  @Test
  public void validate_menuIdInvalid_invalid() {
    OrderItem orderItem = new OrderItem();
    String menuId = "some menu id";
    orderItem.setMenuId(menuId);
    MenuRepository menuRepository = mock(MenuRepository.class);
    when(menuRepository.menuExists(any())).thenReturn(false);
    MenuValidator validator = new MenuValidator(menuRepository);

    ErrorNotification errorNotification = validator.validate(orderItem);

    assertThat(errorNotification.getAllErrors())
        .isEqualTo(String.format(MenuValidator.INVALID_MENU_ERROR_FORMAT, menuId));
  }

  @Test
  public void validate_menuIdNull_invalid() {
    MenuRepository menuRepository = mock(MenuRepository.class);
    when(menuRepository.menuExists(any())).thenReturn(true);
    MenuValidator validator = new MenuValidator(menuRepository);

    ErrorNotification errorNotification = validator.validate(new OrderItem());

    assertThat(errorNotification.getAllErrors())
        .isEqualTo(MenuValidator.MISSING_MENU_ERROR);
  }

  @Test
  public void validate_menuIdIsBlank_invalid() {
    OrderItem orderItem = new OrderItem();
    orderItem.setMenuId("       \t");
    MenuRepository menuRepository = mock(MenuRepository.class);
    when(menuRepository.menuExists(any())).thenReturn(true);
    MenuValidator validator = new MenuValidator(menuRepository);

    ErrorNotification errorNotification = validator.validate(orderItem);

    assertThat(errorNotification.getAllErrors())
        .isEqualTo(MenuValidator.MISSING_MENU_ERROR);
  }

  @Test
  public void validate_menuIdValid_validated() {
    OrderItem orderItem = new OrderItem();
    String menuId = "some menu id";
    orderItem.setMenuId(menuId);
    MenuRepository menuRepository = mock(MenuRepository.class);
    when(menuRepository.menuExists(menuId)).thenReturn(true);
    MenuValidator validator = new MenuValidator(menuRepository);

    ErrorNotification errorNotification = validator.validate(orderItem);

    assertThat(errorNotification.getAllErrors()).isEmpty();
  }
}
