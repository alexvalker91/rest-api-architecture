package alex.valker91.service;

import alex.valker91.model.Subscription;
import alex.valker91.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class InMemorySubscriptionService implements SubscriptionService {

    private final Map<Long, Subscription> subscriptionsById = new HashMap<>();
    private final UserService userService;

    public InMemorySubscriptionService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Subscription create(Subscription subscription) {
        User user = userService.findById(subscription.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + subscription.getUser().getId()));
        subscription.setUser(user);
        if (subscription.getStartDate() == null) {
            subscription.setStartDate(LocalDate.now());
        }
        long id = subscriptionsById.size() + 1;
        subscription.setId(id);
        subscriptionsById.put(id, subscription);
        return subscription;
    }

    @Override
    public Optional<Subscription> update(Subscription subscription) {
        if (subscription.getId() == null || !subscriptionsById.containsKey(subscription.getId())) {
            return Optional.empty();
        }
        User user = userService.findById(subscription.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + subscription.getUser().getId()));
        subscription.setUser(user);
        subscriptionsById.put(subscription.getId(), subscription);
        return Optional.of(subscription);
    }

    @Override
    public boolean deleteById(Long id) {
        Subscription subscription = subscriptionsById.remove(id);
        return subscription != null;
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return Optional.ofNullable(subscriptionsById.get(id));
    }

    @Override
    public List<Subscription> findAll() {
        return new ArrayList<>(subscriptionsById.values());
    }
}
