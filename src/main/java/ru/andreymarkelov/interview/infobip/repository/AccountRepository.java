package ru.andreymarkelov.interview.infobip.repository;

import org.springframework.data.repository.CrudRepository;
import ru.andreymarkelov.interview.infobip.domain.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findByAccountId(String accountId);
}
