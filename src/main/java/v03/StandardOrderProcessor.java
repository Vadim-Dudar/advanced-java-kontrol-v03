package v03;

import java.util.logging.Logger;

public class StandardOrderProcessor extends OrderProcessorTemplate {
    private static final Logger logger = Logger.getLogger(StandardOrderProcessor.class.getName());

    @Override
    protected void validate(Order order) {
        if (order.getItems().length == 0) {
            throw new ValidationException("Order must have at least one item.");
        }
        logger.info("Order " + order.getId() + " validated successfully.");
    }

    @Override
    protected void pay(Money total, PaymentMethod paymentMethod) {
        boolean success = paymentMethod.pay(total);
        if (!success) {
            throw new PaymentException("Payment failed.");
        }
        logger.info("Payment successful.");
    }
}
