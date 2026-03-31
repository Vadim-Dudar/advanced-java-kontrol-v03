package v03;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

public class OrderService {
    private static final Logger logger = Logger.getLogger(OrderService.class.getName());

    private final OrderProcessorTemplate orderProcessor;
    private final OrderRepository orderRepository;

    // Composition via constructor injection
    public OrderService(OrderProcessorTemplate orderProcessor, OrderRepository orderRepository) {
        this.orderProcessor = orderProcessor;
        this.orderRepository = orderRepository;
    }

    public void processOrder(String orderId, PaymentMethod paymentMethod) {
        logger.info("Starting processing for order: " + orderId);
        try {
            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isEmpty()) {
                logger.warning("Order not found: " + orderId);
                throw new OrderNotFoundException("Order not found: " + orderId);
            }
            
            Order order = orderOpt.get();
            orderProcessor.process(order, paymentMethod);
            logger.info("Successfully processed order: " + orderId);
        } catch (AppException e) {
            logger.warning("Business error processing order " + orderId + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected/infrastructure error while processing order " + orderId, e);
            throw new RuntimeException(new InfrastructureException("System failure during order processing", e));
        }
    }
}
