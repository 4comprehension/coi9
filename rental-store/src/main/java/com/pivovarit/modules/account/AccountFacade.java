package com.pivovarit.modules.account;

import java.util.Optional;

public class AccountFacade {

    private final AccountRepository accountRepository;

    AccountFacade(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> findById(long id) {
        return accountRepository.findById(new AccountId(id));
    }

    public AccountId open(long id, String owner) {
        return accountRepository.save(new Account(new AccountId(id), owner));
    }
}
