package example.transaction;

import io.vavr.collection.List;
import io.vavr.control.Either;

import java.time.LocalDateTime;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

public class Transaction {

    private AccountRepo repo;

    public void setRepo(AccountRepo repo) {
        this.repo = repo;
    }

    public Either<TransactionCommandError, List<TransactionEvent>> process(TransactionCommand command, LocalDateTime clock) {
        return switch (command) {
            case TransactionCommand.Debit debit -> handleDebit(debit, clock);
            case TransactionCommand.Credit credit -> handleCredit(credit, clock);
        };
    }

    private Either<TransactionCommandError, List<TransactionEvent>> handleDebit(TransactionCommand.Debit debit, LocalDateTime clock) {
        var accountNumber = debit.accountNumber();
        return repo.getAccountByAccountNumber(accountNumber)
                .<Either<TransactionCommandError, List<TransactionEvent>>>map(acc -> {
                    if (acc.balance().compareTo(debit.Amount()) > 0) {
                        var endingBalance = acc.balance().subtract(debit.Amount());
                        return right(List.of(new TransactionEvent.Debited(debit.accountNumber(), clock, endingBalance)));
                    } else {
                        return left(TransactionCommandError.INSUFFICIENT_FUND);
                    }
                }).orElse(left(TransactionCommandError.ACCOUNT_NOT_FOUND));

    }

    private Either<TransactionCommandError, List<TransactionEvent>> handleCredit(TransactionCommand.Credit credit, LocalDateTime clock) {
        var accountNumber = credit.accountNumber();
        return repo.getAccountByAccountNumber(accountNumber)
                .<Either<TransactionCommandError, List<TransactionEvent>>>map(acc -> {
                        var endingBalance = acc.balance().add(credit.Amount());
                        return right(List.of(new TransactionEvent.Credited(credit.accountNumber(), clock, endingBalance)));
                }).orElse(left(TransactionCommandError.ACCOUNT_NOT_FOUND));

    }
}
