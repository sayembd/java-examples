/*
 * Copyright 2018 MD Sayem Ahmed
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
package com.codesod.example.jpa.lazyload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author MD Sayem Ahmed
 */
@Data
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@Entity
class ProductPrice {

  @Id
  private String id;

  private BigDecimal amount;

  @Embedded
  private PriceValidityPeriod validityPeriod;
}
