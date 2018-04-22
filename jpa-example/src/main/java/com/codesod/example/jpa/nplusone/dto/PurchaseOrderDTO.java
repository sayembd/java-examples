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
package com.codesod.example.jpa.nplusone.dto;

import java.time.OffsetDateTime;

/**
 * @author MD Sayem Ahmed
 */
public class PurchaseOrderDTO {
    private final String id;
    private final OffsetDateTime orderDate;

    public PurchaseOrderDTO(String id, OffsetDateTime orderDate) {
        this.id = id;
        this.orderDate = orderDate;
    }

    public String getId() {
        return id;
    }

    public OffsetDateTime getOrderDate() {
        return orderDate;
    }
}
