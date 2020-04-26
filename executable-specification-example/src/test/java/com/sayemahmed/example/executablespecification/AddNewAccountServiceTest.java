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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.sayemahmed.example.executablespecification.AddNewAccountService.AddNewAccountCommand;
import java.math.BigDecimal;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddNewAccountServiceTest {

  @Mock
  private SaveAccountPort saveAccountPort;

  @Mock
  private FindAccountPort findAccountPort;

  @Nested
  @DisplayName("Given account does not exist")
  class AccountDoesNotExist {
    private AddNewAccountService accountService;

    @BeforeEach
    void setUp() {
      accountService = new AddNewAccountService(saveAccountPort, findAccountPort);
    }

    @Nested
    @DisplayName("When user adds a new account")
    class WhenUserAddsANewAccount {
      private static final String ACCOUNT_NAME = "test account";
      private static final String INITIAL_BALANCE = "56.0";
      private static final String USER_ID = "some id";

      private Account savedAccount;

      @Captor
      private ArgumentCaptor<Account> accountArgumentCaptor;

      @BeforeEach
      void setUp() {
        AddNewAccountCommand command = AddNewAccountCommand.builder()
            .accountName(ACCOUNT_NAME)
            .initialBalance(INITIAL_BALANCE)
            .userId(USER_ID)
            .build();
        accountService.addNewAccount(command);
        BDDMockito.then(saveAccountPort).should().saveAccount(accountArgumentCaptor.capture());
        savedAccount = accountArgumentCaptor.getValue();
      }

      @Test
      @DisplayName("Then added account has the given name")
      void accountAddedWithGivenName() {
        BDDAssertions.then(savedAccount.getName()).isEqualTo(ACCOUNT_NAME);
      }

      @Test
      @DisplayName("Then added account has the given initial balance")
      void accountAddedWithGivenInitialBalance() {
        BDDAssertions.then(savedAccount.getBalance()).isEqualTo(new BigDecimal(INITIAL_BALANCE));
      }

      @Test
      @DisplayName("Then added account has user's id")
      void accountAddedWithUsersId() {
        BDDAssertions.then(accountArgumentCaptor.getValue().getUserId()).isEqualTo(USER_ID);
      }
    }

    @Test
    @DisplayName("When user adds a new account with negative initial balance Then add new account fails")
    void addNewAccountFailsWithNegativeInitialBalance() {
      AddNewAccountCommand command = AddNewAccountCommand.builder()
          .initialBalance("-56.0")
          .build();

      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> accountService.addNewAccount(command));

      BDDMockito.then(saveAccountPort).shouldHaveNoInteractions();
    }
  }

  @Test
  @DisplayName("Given account with the same name exists When user adds a new account Then add new account fails")
  void addNewAccountFailsForDuplicateAccounts() {
    String existingAccountName = "existing name";
    AddNewAccountCommand command = AddNewAccountCommand.builder()
        .initialBalance("0")
        .accountName(existingAccountName)
        .build();
    given(findAccountPort.findAccountByName(existingAccountName)).willReturn(mock(Account.class));
    AddNewAccountService accountService = new AddNewAccountService(saveAccountPort,
        findAccountPort);

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> accountService.addNewAccount(command));

    BDDMockito.then(saveAccountPort).shouldHaveNoInteractions();
  }
}
