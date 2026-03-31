package v03;

public class CreditCardPayment implements PaymentMethod {
    @Override
    public boolean pay(Money amount) {
        if (amount.getAmount().compareTo(new java.math.BigDecimal("30000")) > 0) {
            throw new PaymentException("Credit Card accepts amounts up to 30000");
        }
        System.out.println("Paid " + amount + " using Credit Card");
        return true;
    }
}
