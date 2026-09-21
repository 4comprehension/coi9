package com.pivovarit.modules.account;

import java.util.Optional;

interface AccountRepository {
    AccountId save(Account account);
    Optional<Account> findById(AccountId id);
}
