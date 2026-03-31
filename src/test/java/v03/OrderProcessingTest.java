package v03;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderProcessingTest {

    private OrderService orderService;
    private OrderRepository orderRepository;
    private StandardOrderProcessor orderProcessor;

    @BeforeEach
    void setUp() {
        orderRepository = new InMemoryOrderRepository();
        orderProcessor = new StandardOrderProcessor();
        orderService = new OrderService(orderProcessor, orderRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidemail", "test.com", "user@"})
    void shouldThrowExceptionForInvalidEmail(String invalidEmail) {
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    void shouldThrowExceptionForNegativeMoney() {
        assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-10")));
    }

    @Test
    void shouldThrowExceptionForBlankOrderItemName() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem("  ", 2, new Money(new BigDecimal("10"))));
    }

    @Test
    void shouldThrowExceptionForZeroQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem("Laptop", 0, new Money(new BigDecimal("1000"))));
    }

    @Test
    void shouldThrowExceptionWhenOrderItemsArrayIsEmptyDuringProcessing() {
        Order emptyOrder = new Order("ORDER-001", new Email("test@test.com"));
        orderRepository.save(emptyOrder);
        
        assertThrows(ValidationException.class, () -> orderService.processOrder("ORDER-001", new CreditCardPayment()));
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderDoesNotExist() {
        assertThrows(OrderNotFoundException.class, () -> orderService.processOrder("NON-EXISTENT", new CreditCardPayment()));
    }

    @Test
    void shouldFailPaymentAndThrowPaymentException() {
        Order order = new Order("ORDER-002", new Email("test@test.com"), new OrderItem[]{
                new OrderItem("Phone", 1, new Money(new BigDecimal("500")))
        });
        orderRepository.save(order);

        PaymentMethod failingPayment = amount -> false;

        assertThrows(PaymentException.class, () -> orderService.processOrder("ORDER-002", failingPayment));
    }

    @Test
    void shouldWrapUnexpectedExceptionsInInfrastructureException() {
        PaymentMethod crashingPayment = amount -> {
            throw new IllegalArgumentException("Low level gateway exception");
        };

        Order order = new Order("ORDER-003", new Email("a@b.com"), new OrderItem[]{
                new OrderItem("PC", 1, new Money(new BigDecimal("1000")))
        });
        orderRepository.save(order);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.processOrder("ORDER-003", crashingPayment));
        assertTrue(ex.getCause() instanceof InfrastructureException);
        assertTrue(ex.getCause().getCause() instanceof IllegalArgumentException);
    }

    @Test
    void shouldSuccessfullyProcessOrder() {
        Order order = new Order("ORDER-004", new Email("success@test.com"), new OrderItem[]{
                new OrderItem("Mouse", 2, new Money(new BigDecimal("20")))
        });
        orderRepository.save(order);

        assertDoesNotThrow(() -> orderService.processOrder("ORDER-004", new PayPalPayment()));
    }

    @Test
    void shouldCorrectlyCalculateTotalAmount() {
        Order order = new Order("ORDER-005", new Email("calc@test.com"), new OrderItem[]{
                new OrderItem("Item1", 2, new Money(new BigDecimal("10"))), // 20
                new OrderItem("Item2", 1, new Money(new BigDecimal("30")))  // 30
        });

        assertEquals(new Money(new BigDecimal("50")), order.calculateTotal());
    }

    @Test
    void shouldApplyDiscountCorrectly() {
        DiscountedOrder order = new DiscountedOrder(
                "ORDER-006", 
                new Email("discount@test.com"), 
                new OrderItem[]{
                    new OrderItem("Item1", 1, new Money(new BigDecimal("100")))
                }, 
                new Money(new BigDecimal("20"))
        );

        assertEquals(new Money(new BigDecimal("80")), order.calculateTotal());
    }

    @Test
    void shouldNotAllowExternalModificationOfItemsViaConstructor() {
        OrderItem[] externalArray = new OrderItem[]{
                new OrderItem("Item1", 1, new Money(new BigDecimal("10")))
        };
        
        Order order = new Order("ORDER-007", new Email("a@test.com"), externalArray);
        
        // Modify external array
        externalArray[0] = new OrderItem("Item2", 2, new Money(new BigDecimal("50")));
        
        assertEquals("Item1", order.getItems()[0].getProductName());
    }

    @Test
    void shouldNotAllowExternalModificationOfItemsViaGetter() {
        Order order = new Order("ORDER-008", new Email("a@test.com"), new OrderItem[]{
                new OrderItem("Item1", 1, new Money(new BigDecimal("10")))
        });
        
        OrderItem[] internalArray = order.getItems();
        internalArray[0] = new OrderItem("Hacked", 99, new Money(new BigDecimal("999")));
        
        assertEquals("Item1", order.getItems()[0].getProductName());
    }

    @Test
    void shouldReturnCorrectMoneyAmountUsingMultiplication() {
        Money unitPrice = new Money(new BigDecimal("25.50"));
        Money total = unitPrice.multiply(4);
        
        assertEquals(new BigDecimal("102.00"), total.getAmount().setScale(2));
    }

    @Test
    void shouldProperlyMatchEqualsAndHashCodeForEquivalentMoney() {
        Money moneyA = new Money(new BigDecimal("10.00"));
        Money moneyB = new Money(new BigDecimal("10"));
        
        assertEquals(moneyA, moneyB);
        assertEquals(moneyA.hashCode(), moneyB.hashCode());
    }
}

