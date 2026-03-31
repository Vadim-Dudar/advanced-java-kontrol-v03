package v03;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Order Processing System Demo ===");
        
        OrderRepository repository = new InMemoryOrderRepository();
        OrderProcessorTemplate processor = new StandardOrderProcessor();
        OrderService service = new OrderService(processor, repository);
        
        PaymentMethod cardPayment = new CreditCardPayment();
        PaymentMethod payPalPayment = new PayPalPayment();
        PaymentMethod bankTransfer = new BankTransferPayment();

        System.out.println("\n--- Сценарій 1: Базове замовлення через Банк трансфер - комісія 2% ---");
        Order order1 = new Order("ORD-001", new Email("alice@example.com"), new OrderItem[]{
                new OrderItem("Laptop", 1, new Money(new BigDecimal("1500")))
        });
        repository.save(order1);
        try {
            service.processOrder("ORD-001", bankTransfer);
            System.out.println("Кінцевий статус замовлення: " + order1.getStatus());
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        System.out.println("\n--- Сценарій 2: Велике замовлення (Знижка 5%) через Credit Card ---");
        Order order2 = new Order("ORD-002", new Email("enterprise@corp.com"), new OrderItem[]{
                new OrderItem("Server", 2, new Money(new BigDecimal("6000"))) // Загальна сума: 12000
        });
        repository.save(order2);
        try {
            service.processOrder("ORD-002", cardPayment);
            System.out.println("Кінцевий статус замовлення: " + order2.getStatus());
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        System.out.println("\n--- Сценарій 3: Обробка винятку відсутності товару ---");
        Order order3 = new Order("ORD-003", new Email("bob@example.com"), new OrderItem[]{
                new OrderItem("OUT_OF_STOCK", 1, new Money(new BigDecimal("500")))
        });
        repository.save(order3);
        try {
            service.processOrder("ORD-003", cardPayment);
        } catch (Exception e) {
            System.out.println("Очікувана помилка обробки: " + e.getMessage());
        }

        System.out.println("\n---сценарій 4: Недопустима сума для РayPal ---");
        Order order4 = new Order("ORD-004", new Email("charlie@example.com"), new OrderItem[]{
                new OrderItem("Keyboard", 1, new Money(new BigDecimal("100"))) // Мінімум для PayPal - 300
        });
        repository.save(order4);
        try {
            service.processOrder("ORD-004", payPalPayment);
        } catch (Exception e) {
            System.out.println("Очікувана помилка оплати: " + e.getMessage());
        }
    }
}
