package example.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static example.transaction.TransactionCommandError.ACCOUNT_NOT_FOUND;
import static example.transaction.TransactionCommandError.INSUFFICIENT_FUND;

class TransactionTest {

    private Transaction transaction = new Transaction();

    @BeforeEach
    void setUp() {
        transaction.setRepo(new MockAccountRepo());
    }
    private LocalDateTime now = LocalDateTime.now();

    @Test
    public void shouldDebit() {
        //given
        var debitCommand = new TransactionCommand.Debit("1", new BigDecimal("200"));

        //when
        var events = transaction.process(debitCommand, now).get();

        //then
        assertThat(events).containsOnly(new TransactionEvent.Debited("1", now, new BigDecimal("800")));
        System.out.println(events);
    }

    @Test
    public void errorDebit_accountNotFound() {
        //given
        var debitCommand = new TransactionCommand.Debit("10", new BigDecimal("200"));

        //when
        var events = transaction.process(debitCommand, now);

        //then
        assertThat(events.getLeft()).isEqualTo(ACCOUNT_NOT_FOUND);
        System.out.println(events);
    }

    @Test
    public void errorDebit_Insufficient_fund() {
        //given
        var debitCommand = new TransactionCommand.Debit("1", new BigDecimal("2000"));

        //when
        var events = transaction.process(debitCommand, now);

        //then
        assertThat(events.getLeft()).isEqualTo(INSUFFICIENT_FUND);
        System.out.println(events);
    }


    @Test
    public void shouldCredit() {
        //given
        var creditCommand = new TransactionCommand.Credit("1", new BigDecimal("200"));

        //when
        var events = transaction.process(creditCommand, now).get();

        //then
        assertThat(events).containsOnly(new TransactionEvent.Credited("1", now, new BigDecimal("1200")));
        System.out.println(events);
    }

    class MockAccountRepo implements AccountRepo{

        private Map<String, Account> accs = Map.of(
                "1", new Account("1", new BigDecimal("1000")),
                "2", new Account("2", new BigDecimal("1000")),
                "3", new Account("3", new BigDecimal("1000")),
                "4", new Account("4", new BigDecimal("1000")),
                "5", new Account("5", new BigDecimal("1000"))
        );

        @Override
        public Optional<Account> getAccountByAccountNumber(String accountNumber) {
            return Optional.ofNullable(accs.get(accountNumber));
        }

        @Override
        public void updateAccount(Account account) {

        }
    }
}