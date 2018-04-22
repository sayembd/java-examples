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
package com.codesod.example.jpa.nplusone.service;

import com.codesod.example.jpa.nplusone.entity.PurchaseOrder;
import com.codesod.example.jpa.nplusone.entity.PurchaseOrderItem;
import com.codesod.example.jpa.nplusone.repository.PurchaseOrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author MD Sayem Ahmed
 */
@Service
public class PurchasingService {
    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchasingService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Transactional
    public void createOrder(String customerId, List<String> bookIds) {
        PurchaseOrder purchaseOrder = new PurchaseOrder(UUID.randomUUID().toString(), customerId);
        bookIds.stream()
                .map(bookId -> new PurchaseOrderItem(UUID.randomUUID().toString(), bookId))
                .forEach(purchaseOrder::addOrderItem);
        purchaseOrderRepository.save(purchaseOrder);
    }

    public List<PurchaseOrder> findOrdersOfCustomer(String customerId) {
        return purchaseOrderRepository.findByCustomerId(customerId);
    }
}
