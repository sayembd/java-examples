/*
 * Copyright 2020 MD Sayem Ahmed
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
package com.sayemahmed.example.executablespecification;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class AddNewAccountService {

  private final SaveAccountPort saveAccountPort;
  private final FindAccountPort findAccountPort;

  void addNewAccount(AddNewAccountCommand command) {
    BigDecimal initialBalance = new BigDecimal(command.getInitialBalance());
    if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Initial balance of an account cannot be negative");
    }
    if (findAccountPort.findAccountByName(command.getAccountName()) != null) {
      throw new IllegalArgumentException("An account with given name already exists");
    }
    saveAccountPort.saveAccount(
        new Account(
            command.getAccountName(),
            initialBalance,
            command.getUserId()
        )
    );
  }

  @Builder
  @Getter
  static class AddNewAccountCommand {
    private final String userId;
    private final String accountName;
    private final String initialBalance;
  }
}
