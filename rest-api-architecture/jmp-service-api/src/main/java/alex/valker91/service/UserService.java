package alex.valker91.service;

import alex.valker91.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);
    Optional<User> update(User user);
    boolean deleteById(Long id);
    Optional<User> findById(Long id);
    List<User> findAll();
}
