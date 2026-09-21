package com.pivovarit.modules.account;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryAccountRepository implements AccountRepository {

    private final Map<AccountId, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public AccountId save(Account account) {
        accounts.put(account.id(), account);
        return account.id();
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        return Optional.ofNullable(accounts.get(id));
    }
}
