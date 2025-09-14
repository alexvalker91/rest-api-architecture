package alex.valker91.model;

import java.time.LocalDate;

public record Subscription(
        Long id,
        User user,
        LocalDate startDate
) {
}
