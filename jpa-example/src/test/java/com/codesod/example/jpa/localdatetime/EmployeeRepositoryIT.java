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
package com.codesod.example.jpa.localdatetime;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class EmployeeRepositoryIT {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Test
  public void findingEmployees_joiningDateIsZeroHour_found() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime joiningDate = LocalDateTime.parse("2014-04-01 00:00:00", formatter);

    Employee employee = new Employee();
    employee.setName("Test Employee");
    employee.setDepartment("Test Department");
    employee.setJoiningDate(joiningDate);
    employeeRepository.save(employee);

    List<Employee> employees = employeeRepository.findAll((root, query, cb) ->
        cb.and(
            cb.greaterThanOrEqualTo(root.get(Employee_.joiningDate), joiningDate),
            cb.lessThan(root.get(Employee_.joiningDate), joiningDate.plusDays(1))
        )
    );

    assertThat(employees).hasSize(1);
  }

  @Test
  public void findingEmployees_joiningDateIsNotZeroHour_found() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime joiningDate = LocalDateTime.parse("2014-04-01 08:00:00", formatter);
    LocalDateTime zeroHour = LocalDateTime.parse("2014-04-01 00:00:00", formatter);

    Employee employee = new Employee();
    employee.setName("Test Employee");
    employee.setDepartment("Test Department");
    employee.setJoiningDate(joiningDate);
    employeeRepository.save(employee);

    List<Employee> employees = employeeRepository.findAll((root, query, cb) ->
        cb.and(
            cb.greaterThanOrEqualTo(root.get(Employee_.joiningDate), zeroHour),
            cb.lessThan(root.get(Employee_.joiningDate), zeroHour.plusDays(1))
        )
    );

    assertThat(employees).hasSize(1);
  }
}