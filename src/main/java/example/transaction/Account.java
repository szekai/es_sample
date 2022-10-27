package example.transaction;

import java.math.BigDecimal;

public record Account(String AccountNumber, BigDecimal balance) {
}
