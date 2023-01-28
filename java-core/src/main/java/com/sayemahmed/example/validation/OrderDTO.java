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
package com.sayemahmed.example.validation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDTO {

  @NotNull
  private String customerId;

  @NotNull
  @Size(min = 1)
  private List<OrderItem> orderItems;

  @Getter
  @Setter
  @ToString
  public static class OrderItem {
    private String menuId;
    private String description;
    private String price;
    private Integer quantity;
  }
}
