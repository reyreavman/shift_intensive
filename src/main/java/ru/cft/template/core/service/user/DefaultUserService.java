package ru.cft.template.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.api.payload.user.PatchUserPayload;
import ru.cft.template.api.payload.user.UserPayload;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.mapper.UserMapper;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO createUser(UserPayload userPayload) {
        User savedUser = this.userRepository.save(userMapper.mapToUser(userPayload, passwordEncoder));
        this.walletRepository.save(new Wallet(null, savedUser, 100L));
        return userMapper.mapToUserDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO patchUser(Long id, PatchUserPayload userPayload) {
        User user = userRepository.findById(id).orElseThrow();
        User patchedUser = userMapper.mapToUserPatch(user, userPayload);
        return userMapper.mapToUserDTO(patchedUser);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream().map(userMapper::mapToUserDTO).toList();
    }

    @Override
    public UserDTO findUserById(Long id) {
        return userMapper.mapToUserDTO(userRepository.findById(id).orElseThrow());
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        return userMapper.mapToUserDTO(userRepository.findByEmail(email).orElseThrow());
    }

    @Override
    public UserDTO findUserByPhoneNumber(String phoneNumber) {
        return userMapper.mapToUserDTO(userRepository.findByPhoneNumber(phoneNumber).orElseThrow());
    }
}
