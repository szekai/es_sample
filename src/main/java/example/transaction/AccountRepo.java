package example.transaction;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepo {

    Optional<Account> getAccountByAccountNumber(String accountNumber);

    void updateAccount(Account account);
}
