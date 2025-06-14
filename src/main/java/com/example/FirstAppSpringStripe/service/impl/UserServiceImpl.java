package com.example.FirstAppSpringStripe.service.impl;

import com.example.FirstAppSpringStripe.dto.UserRequest;
import com.example.FirstAppSpringStripe.model.Role;
import com.example.FirstAppSpringStripe.model.User;
import com.example.FirstAppSpringStripe.repository.RoleRepository;
import com.example.FirstAppSpringStripe.repository.UserRepository;
import com.example.FirstAppSpringStripe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void register(UserRequest req) {
        if(userRepository.findByLogin(req.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Error...");
        }
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new IllegalStateException("There is no role... ROLE_USER"));
        User u = User.builder()
                .id(UUID.randomUUID().toString())
                .login(req.getLogin())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(Set.of(userRole))
                .build();
        userRepository.save(u);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findByIdAndIsActiveTrue(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLoginAndIsActiveTrue(login);
    }

    @Override
    public void addRoleToUser(String userId, String roleName) {
        Optional<User> user = userRepository.findByIdAndIsActiveTrue(userId);
        if(user.isEmpty()) {
            throw new IllegalStateException("User " + userId + " not found.");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role " + roleName + " not found."));
        user.get().getRoles().add(role);
        userRepository.save(user.get());
    }

    @Override
    public void removeRoleFromUser(String userId, String roleName) {
        Optional<User> user = userRepository.findByIdAndIsActiveTrue(userId);
        if(user.isEmpty()) {
            throw new IllegalStateException("User " + userId + " not found.");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role " + roleName + " not found."));
        user.get().getRoles().remove(role);
        userRepository.save(user.get());
    }

    @Override
    public void deleteById(String id) {
        Optional<User> user = userRepository.findByIdAndIsActiveTrue(id);
        if(user.isEmpty()) {
            throw new IllegalStateException("User " + id + " not found.");
        } else {
            user.get().setActive(false);
            user.get().getRoles().clear();
            userRepository.save(user.get());
        }
        //userRepository.deleteById(id);
    }
}