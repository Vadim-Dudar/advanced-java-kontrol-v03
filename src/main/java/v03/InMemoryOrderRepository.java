package v03;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryOrderRepository implements OrderRepository {
    private final Map<String, Order> storage = new HashMap<>();

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void save(Order order) {
        if (order != null) {
            storage.put(order.getId(), order);
        }
    }
}

