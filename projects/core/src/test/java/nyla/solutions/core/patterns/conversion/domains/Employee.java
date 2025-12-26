package nyla.solutions.core.patterns.conversion.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Employee(String id, String empName, String jobName, LocalDateTime hireDate, BigDecimal salary, BigDecimal commission, String department, String managerId) {
    public Employee {
        java.util.Objects.requireNonNull(id, "id required");
        java.util.Objects.requireNonNull(empName, "employee name required");
    }
}