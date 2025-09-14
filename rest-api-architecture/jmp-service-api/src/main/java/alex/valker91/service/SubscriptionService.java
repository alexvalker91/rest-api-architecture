package alex.valker91.service;

import alex.valker91.model.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    Subscription create(Subscription subscription);
    Optional<Subscription> update(Subscription subscription);
    boolean deleteById(Long id);
    Optional<Subscription> findById(Long id);
    List<Subscription> findAll();
}
