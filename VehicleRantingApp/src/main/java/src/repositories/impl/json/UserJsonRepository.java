package src.repositories.impl.json;

import src.models.User;
import src.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserJsonRepository implements UserRepository {
    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
