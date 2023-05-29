package com.haha454.order_matching;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMatchingEngineTest {

    @Test
    void match_normal_1() {
        var engine = new OrderMatchingEngine();
        assertIterableEquals(Collections.EMPTY_LIST,
                engine.match(
                        new OrderMatchingEngine.Order("12345", OrderMatchingEngine.Side.BUY, 10000, 5)));
        assertIterableEquals(Collections.EMPTY_LIST,
                engine.match(
                        new OrderMatchingEngine.Order("zod42", OrderMatchingEngine.Side.SELL, 10001, 2)));
        assertIterableEquals(Collections.EMPTY_LIST,
                engine.match(
                        new OrderMatchingEngine.Order("13471", OrderMatchingEngine.Side.BUY, 9971, 6)));
        assertIterableEquals(List.of(
                        new OrderMatchingEngine.Trade("abe14", "12345", 5, 10000),
                        new OrderMatchingEngine.Trade("abe14", "13471", 2, 9971)),
                engine.match(
                        new OrderMatchingEngine.Order("abe14", OrderMatchingEngine.Side.SELL, 9800, 7)));
        assertIterableEquals(List.of(
                        new OrderMatchingEngine.Order("zod42", OrderMatchingEngine.Side.SELL, 10001, 2),
                        new OrderMatchingEngine.Order("13471", OrderMatchingEngine.Side.BUY, 9971, 4)),
                engine.getRemainingOrders());
    }

    @Test
    void match_normal_2() {
        var engine = new OrderMatchingEngine();
        assertIterableEquals(Collections.EMPTY_LIST,
                engine.match(
                        new OrderMatchingEngine.Order("11431", OrderMatchingEngine.Side.BUY, 175, 9)));
        assertIterableEquals(List.of(
                        new OrderMatchingEngine.Trade("plu401", "11431", 9, 175)),
                engine.match(
                        new OrderMatchingEngine.Order("plu401", OrderMatchingEngine.Side.SELL, 170, 10)));
        assertIterableEquals(List.of(
                        new OrderMatchingEngine.Trade("45691", "plu401", 1, 170)),
                engine.match(
                        new OrderMatchingEngine.Order("45691", OrderMatchingEngine.Side.BUY, 180, 3)));
        assertIterableEquals(List.of(
                        new OrderMatchingEngine.Order("45691", OrderMatchingEngine.Side.BUY, 180, 2)),
                engine.getRemainingOrders());
    }

    @Test
    void cancel() {
    }
}