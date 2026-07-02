package com.sivalabs.blog.users;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    UserService(UserRepository userRepository,
                PasswordEncoder passwordEncoder,
                UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> findByEmail(String email) {
        return userRepository
                .findByEmailIgnoreCase(email)
                .map(userMapper::toUserDtoWithPassword);
    }

    @Transactional
    public UserDto createUser(User user) {
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        var saved = userRepository.save(user);
        return userMapper.toUserDto(saved);
    }
}
