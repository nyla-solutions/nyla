package nyla.solutions.core.patterns.creational.generator.qaRecords;

import java.util.List;

public record Order(List<OrderItem> orderDetails) {
}
