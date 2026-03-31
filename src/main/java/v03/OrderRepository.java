package v03;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(String id);
    void save(Order order);
}

