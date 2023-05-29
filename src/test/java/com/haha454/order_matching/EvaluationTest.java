package com.haha454.order_matching;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationTest {

    /*
# The expression
# - has only ">", "=", and "<" operators
# - It supports AND (&&) OR(||) logic
# - It supports only integer type
# - It is evaluated from left to right
# The input is a map of variable names to variable values
# exprStr: "a > 10 && b == 0 || c < b"
# context: {"a": 12, "b": 0, "c": -1}
# return: true
# context: {"a": 12, "b": 2, "c": 3}
# return: false
# Follow up 1-a:
# - what if there are no spaces between tokens?
# Follow up 1:
# - support bracket
# "a > 10 && (b == 0 || c < b)"
# Follow up 2:
# - support NOT, BETWEEN
# Follow up 3:
# - support different data types?
# " version > 3.2.1 "
     */
    @Test
    void evaluate() throws Exception {
        final Map<String, Integer> context = Map.of("a", 12, "b", 2, "c", 3);
        var evaluation = new Evaluation("a > 10 && (b == 0 || c < b)", context);
        assertFalse(evaluation.evaluate());
    }
}