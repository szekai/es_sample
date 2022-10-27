package example.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public sealed interface TransactionEvent extends Serializable {
    LocalDateTime createdAt();

    record Debited(String accountNumber, LocalDateTime createdAt, BigDecimal balance) implements TransactionEvent {
    }

    record Credited(String accountNumber, LocalDateTime createdAt, BigDecimal balance) implements TransactionEvent {
    }
}
