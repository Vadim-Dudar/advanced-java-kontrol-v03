package v03;

import java.util.logging.Logger;

public class StandardOrderProcessor extends OrderProcessorTemplate {
    private static final Logger logger = Logger.getLogger(StandardOrderProcessor.class.getName());

    @Override
    protected void validate(Order order) {
        if (order.getItems().length == 0) {
            throw new ValidationException("Order must have at least one item.");
        }
        if (order.getItems().length > 10) {
            throw new ValidationException("Order cannot have more than 10 items.");
        }
        logger.info("Order " + order.getId() + " validated successfully.");
    }

    @Override
    protected void reserveStock(Order order) {
        for (OrderItem item : order.getItems()) {
            if ("OUT_OF_STOCK".equals(item.getProductName())) {
                throw new OutOfStockException("Item " + item.getProductName() + " is out of stock.");
            }
        }
        logger.info("Stock reserved for order " + order.getId());
    }

    @Override
    protected Money calculateTotal(Order order) {
        Money total = super.calculateTotal(order);
        if (total.getAmount().compareTo(new java.math.BigDecimal("10000")) > 0) {
            total = total.multiply(new java.math.BigDecimal("0.95")); // 5% discount
            logger.info("Applied 5% discount. New total: " + total);
        }
        return total;
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
