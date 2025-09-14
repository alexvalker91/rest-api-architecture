package alex.valker91.mapper;

import alex.valker91.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public final class DtoMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // example "2025-09-14"

    private DtoMapper() {}

    public static User toUser(UserRequestDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        if (dto.getBirthday() != null) {
            user.setBirthday(LocalDate.parse(dto.getBirthday(), DATE_TIME_FORMATTER));
        }
        return user;
    }

    public static UserResponseDto toUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        if (user.getBirthday() != null) {
            dto.setBirthday(user.getBirthday().format(DATE_TIME_FORMATTER));
        } else {
            dto.setBirthday(null);
        }
        return dto;
    }

    public static List<UserResponseDto> toUserResponseDtoList(List<User> users) {
        return users.stream().map(DtoMapper::toUserResponseDto).collect(Collectors.toList());
    }

    public static Subscription toSubscription(SubscriptionRequestDto dto, User user) {
        Subscription subscription = new Subscription();
        subscription.setId(dto.getId());
        subscription.setUser(user);
        subscription.setStartDate(LocalDate.now());
        return subscription;
    }

    public static SubscriptionResponseDto toSubscriptionResponseDto(Subscription subscription) {
        SubscriptionResponseDto dto = new SubscriptionResponseDto();
        dto.setId(subscription.getId());
        if (subscription.getUser() != null) {
            dto.setUserId(subscription.getUser().getId());
        } else {
            dto.setUserId(null);
        }
        if (subscription.getStartDate() != null) {
            dto.setStartDate(subscription.getStartDate().format(DATE_TIME_FORMATTER));
        } else {
            dto.setStartDate(null);
        }
        return dto;
    }

    public static List<SubscriptionResponseDto> toSubscriptionResponseDtoList(List<Subscription> subscriptions) {
        return subscriptions.stream().map(DtoMapper::toSubscriptionResponseDto).collect(Collectors.toList());
    }
}
