package v03;

public class PayPalPayment implements PaymentMethod {
    @Override
    public boolean pay(Money amount) {
        if (amount.getAmount().compareTo(new java.math.BigDecimal("300")) < 0) {
            throw new PaymentException("PayPal accepts amounts from 300");
        }
        System.out.println("Paid " + amount + " using PayPal");
        return true;
    }
}
