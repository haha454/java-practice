package com.haha454.order_matching;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class OrderMatchingEngine {
    private final PriorityQueue<SamePriceOrderList> sellOrderHeap, buyOrderHeap;
    private final Map<Integer, SamePriceOrderList> sellOrderListPerPrice, buyOrderListPerPrice;
    private final Map<String, Order> orderPerId;

    public OrderMatchingEngine() {
        this.sellOrderHeap = new PriorityQueue<>(Comparator.comparingInt(e -> e.price));
        this.buyOrderHeap = new PriorityQueue<>(Comparator.<SamePriceOrderList>comparingInt(e -> e.price).reversed());
        this.sellOrderListPerPrice = new HashMap<>();
        this.buyOrderListPerPrice = new HashMap<>();
        this.orderPerId = new LinkedHashMap<>();
    }

    public Iterable<Trade> match(Order order) {
        return switch (order.side) {
            case BUY ->
                    match(order, sellOrderHeap, buyOrderHeap, sellOrderListPerPrice, buyOrderListPerPrice, price -> order.price >= price);
            case SELL ->
                    match(order, buyOrderHeap, sellOrderHeap, buyOrderListPerPrice, sellOrderListPerPrice, price -> order.price <= price);
        };
    }

    private Iterable<Trade> match(Order order, PriorityQueue<SamePriceOrderList> targetOrderHeap, PriorityQueue<SamePriceOrderList> unmatchedOrderHeap,
                                  Map<Integer, SamePriceOrderList> targetOrderListPerPrice, Map<Integer, SamePriceOrderList> unmatchedOrderListPerPrice,
                                  Function<Integer, Boolean> matchingPredicate) {
        popInvalidOrder(targetOrderHeap, targetOrderListPerPrice);
        var result = new ArrayList<Trade>();
        while (!targetOrderHeap.isEmpty() && matchingPredicate.apply(targetOrderHeap.peek().price) && order.quantity > 0) {
            var contraOrder = targetOrderHeap.peek().getEarliestOrder().get();
            var tradingPrice = contraOrder.price;
            var tradingQuantity = Math.min(contraOrder.quantity, order.quantity);
            result.add(new Trade(order.id, contraOrder.id, tradingQuantity, tradingPrice));
            contraOrder.quantity -= tradingQuantity;
            order.quantity -= tradingQuantity;
            popInvalidOrder(targetOrderHeap, targetOrderListPerPrice);
        }

        if (order.quantity > 0) {
            orderPerId.put(order.id, order);
            if (!unmatchedOrderListPerPrice.containsKey(order.price)) {
                SamePriceOrderList samePriceOrderList = new SamePriceOrderList(order.price);
                unmatchedOrderHeap.offer(samePriceOrderList);
                unmatchedOrderListPerPrice.put(order.price, samePriceOrderList);
            }
            unmatchedOrderListPerPrice.get(order.price).addOrder(order);
        }

        return result;
    }

    private static void popInvalidOrder(PriorityQueue<SamePriceOrderList> orderHeap, Map<Integer, SamePriceOrderList> orderListPerPrice) {
        while (!orderHeap.isEmpty() && orderHeap.peek().getEarliestOrder().isEmpty()) {
            SamePriceOrderList samePriceOrderList = orderHeap.poll();
            assert samePriceOrderList != null;
            orderListPerPrice.remove(samePriceOrderList.price);
        }
    }

    public boolean cancel(String orderId) {
        if (!orderPerId.containsKey(orderId) || orderPerId.get(orderId).isCancelled) {
            return false;
        }

        orderPerId.get(orderId).isCancelled = true;
        return true;
    }

    public Iterable<Order> getRemainingOrders() {
        return orderPerId.values().stream().filter(order -> order.quantity > 0 && !order.isCancelled).collect(Collectors.toList());
    }

    public record Trade(String orderId, String contraOrderId, int quantity, int price) {
    }

    public static final class Order {
        private final String id;
        private final Side side;
        private final int price;

        private int quantity;
        private boolean isCancelled;

        public Order(String id, Side side, int price, int quantity) {
            this.id = id;
            this.side = side;
            this.price = price;
            this.quantity = quantity;
            this.isCancelled = false;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Order) obj;
            return Objects.equals(this.side, that.side) &&
                    this.price == that.price &&
                    this.quantity == that.quantity &&
                    this.isCancelled == that.isCancelled;
        }

        @Override
        public int hashCode() {
            return Objects.hash(side, price, quantity, isCancelled);
        }

        @Override
        public String toString() {
            return "Order[" +
                    "id=" + id + ", " +
                    "side=" + side + ", " +
                    "price=" + price + ", " +
                    "quantity=" + quantity + ", " +
                    "isCancelled=" + isCancelled + ']';
        }
    }

    private static class SamePriceOrderList {
        private final int price;
        private final LinkedList<Order> orders;

        public SamePriceOrderList(int price) {
            super();
            this.price = price;
            this.orders = new LinkedList<>();
        }

        public void addOrder(Order order) {
            assert order.price == this.price;
            assert order.quantity > 0;
            assert !order.isCancelled;
            orders.addLast(order);
        }

        public Optional<Order> getEarliestOrder() {
            purgeInvalidOrders();
            return orders.isEmpty() ? Optional.empty() : Optional.of(orders.getFirst());
        }

        private void purgeInvalidOrders() {
            while (!orders.isEmpty() && (orders.getFirst().isCancelled || orders.getFirst().quantity == 0)) {
                orders.removeFirst();
            }
        }
    }

    public enum Side {
        BUY,
        SELL,
    }
}
