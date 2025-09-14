package alex.valker91.service;

import alex.valker91.model.User;
import alex.valker91.model.UserEntity;
import alex.valker91.service.mapper.EntityMapper;
import alex.valker91.service.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (user.getId() == null) {
            return Optional.empty();
        }
        return userRepository.findById(user.getId())
                .map(existing -> {
                    existing.setName(user.getName());
                    existing.setSurname(user.getSurname());
                    existing.setBirthday(user.getBirthday());
                    return userRepository.save(existing);
                })
                .map(EntityMapper::toUser);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(EntityMapper::toUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .map(EntityMapper::toUser)
                .collect(Collectors.toList());
    }
}
