package alex.valker91.service;

import alex.valker91.model.User;
import alex.valker91.model.UserEntity;
import alex.valker91.service.mapper.EntityMapper;
import alex.valker91.service.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class DbUserService implements UserService {

    private final UserRepository userRepository;

    public DbUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        UserEntity userEntity = EntityMapper.toUserEntity(user);
        userEntity = userRepository.save(userEntity);
        return EntityMapper.toUser(userEntity);
    }

    @Override
    public Optional<User> update(User user) {
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }
}
