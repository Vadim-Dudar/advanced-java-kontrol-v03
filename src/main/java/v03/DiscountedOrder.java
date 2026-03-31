package v03;

import java.util.Objects;

public class DiscountedOrder extends Order {
    private final Money discount;

    public DiscountedOrder(String id, Email customerEmail, OrderItem[] items, Money discount) {
        super(id, customerEmail, items);
        if (discount == null) {
            throw new IllegalArgumentException("Discount cannot be null");
        }
        this.discount = discount;
    }

    public DiscountedOrder(String id, Email customerEmail, Money discount) {
        this(id, customerEmail, new OrderItem[0], discount);
    }

    @Override
    public Money calculateTotal() {
        Money originalTotal = super.calculateTotal();
        return originalTotal.subtract(discount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        if (getClass() != o.getClass()) return false;
        DiscountedOrder that = (DiscountedOrder) o;
        return Objects.equals(discount, that.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), discount);
    }
}
