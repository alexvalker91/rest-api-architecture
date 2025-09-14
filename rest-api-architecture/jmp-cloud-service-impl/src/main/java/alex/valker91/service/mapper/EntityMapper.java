package alex.valker91.service.mapper;

import alex.valker91.model.Subscription;
import alex.valker91.model.SubscriptionEntity;
import alex.valker91.model.User;
import alex.valker91.model.UserEntity;

public class EntityMapper {

    private EntityMapper() {}

    public static UserEntity toUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setSurname(user.getSurname());
        userEntity.setBirthday(user.getBirthday());
        return userEntity;
    }

    public static User toUser(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setSurname(userEntity.getSurname());
        user.setBirthday(userEntity.getBirthday());
        return user;
    }

    public static SubscriptionEntity toSubscriptionEntity(Subscription subscription) {
        Long userId = null;
        if (subscription.getUser() != null) {
            userId = subscription.getUser().getId();
        }
        return new SubscriptionEntity(subscription.getId(), userId, subscription.getStartDate());
    }

    public static Subscription toSubscription(SubscriptionEntity subscriptionEntity) {
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionEntity.getId());
        User user = new User();
        user.setId(subscriptionEntity.getUserId());
        subscription.setUser(user);
        subscription.setStartDate(subscriptionEntity.getStartDate());
        return subscription;
    }
}
