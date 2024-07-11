package ru.cft.template.core.service.user;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.api.dto.user.CreateUserDTO;
import ru.cft.template.api.dto.user.PatchUserDTO;
import ru.cft.template.api.dto.user.UserDTO;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.service.ServiceException;
import ru.cft.template.core.mapper.UserMapper;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        User savedUser = userRepository.save(
                userMapper.mapToUser(
                        createUserDTO, BCrypt.hashpw(createUserDTO.password(), BCrypt.gensalt()))
        );
        walletRepository.save(new Wallet(null, savedUser, 100L));
        return userMapper.mapToUserDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO patchUser(Long id, PatchUserDTO userPayload) {
        User user = findById(id);
        User patchedUser = userMapper.mapToUserPatch(user, userPayload);
        return userMapper.mapToUserDTO(patchedUser);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToUserDTO)
                .toList();
    }

    @Override
    public UserDTO findUserById(Long id) {
        return userMapper.mapToUserDTO(findById(id));
    }

    @Override
    public User findUserByIdEntity(Long id) {
        return findById(id);
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        return userMapper.mapToUserDTO(
                userRepository.findByEmail(email).orElseThrow(() -> new ServiceException("Пользователь с email - %s не найден".formatted(email)))
        );
    }

    @Override
    public UserDTO findUserByPhoneNumber(String phoneNumber) {
        return userMapper.mapToUserDTO(
                userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new ServiceException("Пользователь с phoneNumber - %s не найден.".formatted(phoneNumber)))
        );
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ServiceException("Пользователь с id - %d не найден.".formatted(id)));
    }
}
