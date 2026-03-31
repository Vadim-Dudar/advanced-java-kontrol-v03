package v03;

import java.util.Objects;

public class OrderItem {
    private final String productName;
    private final int quantity;
    private final Money unitPrice;

    public OrderItem(String productName, int quantity, Money unitPrice) {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price cannot be null");
        }
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public Money getTotalPrice() {
        return unitPrice.multiply(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
                Objects.equals(productName, orderItem.productName) &&
                Objects.equals(unitPrice, orderItem.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, quantity, unitPrice);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}

