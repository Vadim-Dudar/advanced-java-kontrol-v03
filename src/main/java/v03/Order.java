package v03;

import java.util.Arrays;
import java.util.Objects;

public class Order {
    private final String id;
    private final Email customerEmail;
    private final OrderItem[] items;

    public Order(String id, Email customerEmail) {
        this(id, customerEmail, new OrderItem[0]);
    }

    public Order(String id, Email customerEmail, OrderItem[] items) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or blank");
        }
        if (customerEmail == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (items == null) {
            throw new IllegalArgumentException("Items cannot be null");
        }
        this.id = id;
        this.customerEmail = customerEmail;
        this.items = Arrays.copyOf(items, items.length);
    }

    public String getId() {
        return id;
    }

    public Email getCustomerEmail() {
        return customerEmail;
    }

    public OrderItem[] getItems() {
        return Arrays.copyOf(items, items.length);
    }

    public Money calculateTotal() {
        Money total = new Money();
        for (OrderItem item : items) {
            total = total.add(item.getTotalPrice());
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerEmail=" + customerEmail +
                ", itemsCount=" + items.length +
                '}';
    }
}

