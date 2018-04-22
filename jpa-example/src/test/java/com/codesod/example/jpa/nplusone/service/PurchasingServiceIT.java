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

import com.codesod.example.jpa.nplusone.dto.PurchaseOrderDTO;
import com.codesod.example.jpa.nplusone.entity.PurchaseOrder;
import com.codesod.example.jpa.nplusone.repository.PurchaseOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author MD Sayem Ahmed
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PurchasingServiceIT {

    @Autowired
    private PurchasingService purchasingService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void fetches_data_with_n_plus_1_queries() {
        // Given
        purchasingService.createOrder("Sayem", Arrays.asList("Harry Potter 1", "Harry Potter 2"));
        purchasingService.createOrder("Sayem", Arrays.asList("Harry Potter 3", "Harry Potter 4"));

        // When
        List<PurchaseOrder> ordersOfCustomer = purchasingService.findOrdersOfCustomer("Sayem");

        //Then
        assertThat(ordersOfCustomer).hasSize(2);
    }

    @Test
    public void fetches_a_tuple() {
        // Given
        purchasingService.createOrder("Sayem", Arrays.asList("Tin Goyenda", "Rupali Makorsha"));
        purchasingService.createOrder("Sayem", Arrays.asList("Roktochokhkhhu", "Kalo Beral"));

        // When
        TypedQuery<PurchaseOrderDTO> jpaQuery = entityManager.createQuery(
                "SELECT " +
                        "NEW com.codesod.example.jpa.nplusone.dto.PurchaseOrderDTO(P.id, P.orderDate) " +
                "FROM " +
                        "PurchaseOrder P " +
                "WHERE " +
                        "P.customerId = :customerId",
                PurchaseOrderDTO.class);
        jpaQuery.setParameter("customerId", "Sayem");
        List<PurchaseOrderDTO> orders = jpaQuery.getResultList();

        //Then
        assertThat(orders).isNotEmpty();
    }
}
