package alex.valker91.model;

public record UserRequestDto(
        Long id,
        String name,
        String surname,
        String birthday
) {
}
