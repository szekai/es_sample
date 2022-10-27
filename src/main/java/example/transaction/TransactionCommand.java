package example.transaction;

import java.io.Serializable;
import java.math.BigDecimal;

public sealed interface TransactionCommand extends Serializable {

    record Debit(String accountNumber, BigDecimal Amount) implements TransactionCommand {
    }

    record Credit(String accountNumber, BigDecimal Amount) implements TransactionCommand {
    }
}
