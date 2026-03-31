package v03;

public class PayPalPayment implements PaymentMethod {
    @Override
    public boolean pay(Money amount) {
        System.out.println("Paid " + amount + " using PayPal");
        return true;
    }
}

