package alex.valker91.model;

import java.time.LocalDate;

public record User(
        Long id,
        String name,
        String surname,
        LocalDate birthday
) {
}
