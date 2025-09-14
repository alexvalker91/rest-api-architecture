package alex.valker91.service;

import alex.valker91.model.Subscription;
import alex.valker91.service.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class DbSubscriptionService implements SubscriptionService {

    private final UserRepository userRepository;

    public DbSubscriptionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Subscription create(Subscription subscription) {
        return null;
    }

    @Override
    public Optional<Subscription> update(Subscription subscription) {
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Subscription> findAll() {
        return List.of();
    }
}
