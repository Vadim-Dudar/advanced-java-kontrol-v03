package v03;

import java.util.Optional;

public class OrderService {
    private final OrderProcessorTemplate orderProcessor;
    private final OrderRepository orderRepository;

    // Composition via constructor injection
    public OrderService(OrderProcessorTemplate orderProcessor, OrderRepository orderRepository) {
        this.orderProcessor = orderProcessor;
        this.orderRepository = orderRepository;
    }

    public void processOrder(String orderId, PaymentMethod paymentMethod) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        
        Order order = orderOpt.get();
        orderProcessor.process(order, paymentMethod);
    }
}

