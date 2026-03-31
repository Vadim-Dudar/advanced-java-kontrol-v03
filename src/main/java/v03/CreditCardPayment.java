package v03;

public class CreditCardPayment implements PaymentMethod {
    @Override
    public boolean pay(Money amount) {
        System.out.println("Paid " + amount + " using Credit Card");
        return true;
    }
}

