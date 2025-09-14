package alex.valker91.service;

import alex.valker91.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InMemoryUserService implements UserService {

    private final Map<Long, User> usersById = new HashMap<>();

    @Override
    public User create(User user) {
        long id = usersById.size() + 1;
        user.setId(id);
        usersById.put(id, user);
        return user;
    }

    @Override
    public Optional<User> update(User user) {
        if (user.getId() == null || !usersById.containsKey(user.getId())) {
            return Optional.empty();
        }
        usersById.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public boolean deleteById(Long id) {
        User user = usersById.remove(id);
        return user != null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(usersById.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }
}
