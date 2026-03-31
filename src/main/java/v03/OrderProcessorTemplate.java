package v03;

public abstract class OrderProcessorTemplate {
    
    public final void process(Order order, PaymentMethod paymentMethod) {
        validate(order);
        Money total = calculateTotal(order);
        pay(total, paymentMethod);
        completeOrder(order);
    }

    protected abstract void validate(Order order);
    protected abstract void pay(Money total, PaymentMethod paymentMethod);

    protected Money calculateTotal(Order order) {
        System.out.println("Calculating total for order " + order.getId());
        return order.calculateTotal();
    }

    protected void completeOrder(Order order) {
        System.out.println("Order " + order.getId() + " is completed. Notification sent to " + order.getCustomerEmail().getAddress());
    }
}

