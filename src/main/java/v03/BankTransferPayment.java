package v03;

import java.math.BigDecimal;

public class BankTransferPayment implements PaymentMethod {
    @Override
    public boolean pay(Money amount) {
        // Commission 2%
        Money totalWithFee = amount.multiply(new BigDecimal("1.02"));
        System.out.println("Paid " + totalWithFee + " using Bank Transfer (including 2% fee)");
        return true;
    }
}

