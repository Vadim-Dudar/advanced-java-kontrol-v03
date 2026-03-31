package v03;

public class StandardOrderProcessor extends OrderProcessorTemplate {

    @Override
    protected void validate(Order order) {
        if (order.getItems().length == 0) {
            throw new IllegalStateException("Order must have at least one item.");
        }
        System.out.println("Order " + order.getId() + " validated successfully.");
    }

    @Override
    protected void pay(Money total, PaymentMethod paymentMethod) {
        boolean success = paymentMethod.pay(total);
        if (!success) {
            throw new IllegalStateException("Payment failed.");
        }
    }
}

