package alex.valker91.service;

import alex.valker91.model.Subscription;
import alex.valker91.model.SubscriptionEntity;
import alex.valker91.service.mapper.EntityMapper;
import alex.valker91.service.repository.SubscriptionRepository;
import alex.valker91.service.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
public class DbSubscriptionService implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public DbSubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Subscription create(Subscription subscription) {
        if (subscription.getUser() == null || subscription.getUser().getId() == null) {
            throw new IllegalArgumentException("Subscription user with id not exist");
        }
        Long userId = subscription.getUser().getId();
        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        SubscriptionEntity entity = EntityMapper.toSubscriptionEntity(subscription);
        entity = subscriptionRepository.save(entity);
        return EntityMapper.toSubscription(entity);
    }

    @Override
    public Optional<Subscription> update(Subscription subscription) {
        if (subscription.getId() == null) {
            return Optional.empty();
        }
        return subscriptionRepository.findById(subscription.getId())
                .map(subscriptionEntity -> {
                    Long userId;
                    if (subscription.getUser() != null) {
                        userId = subscription.getUser().getId();
                    } else {
                        userId = null;
                    }
                    if (userId != null) {
                        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
                    }
                    subscriptionEntity.setUserId(userId);
                    subscriptionEntity.setStartDate(subscription.getStartDate());
                    return subscriptionRepository.save(subscriptionEntity);
                })
                .map(EntityMapper::toSubscription);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!subscriptionRepository.existsById(id)) {
            return false;
        }
        subscriptionRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id).map(EntityMapper::toSubscription);
    }

    @Override
    public List<Subscription> findAll() {
        return subscriptionRepository.findAll().stream()
                .map(EntityMapper::toSubscription)
                .collect(Collectors.toList());
    }
}
