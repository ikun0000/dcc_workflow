package com.example.dccworkflow.service;

import com.example.dccworkflow.dto.RoleDto;
import com.example.dccworkflow.dto.UserDto;
import com.example.dccworkflow.entity.QUser;
import com.example.dccworkflow.entity.Role;
import com.example.dccworkflow.entity.User;
import com.example.dccworkflow.exception.OldPasswordErrorException;
import com.example.dccworkflow.exception.UserNotFoundException;
import com.example.dccworkflow.repository.RoleRepository;
import com.example.dccworkflow.repository.UserRepository;
import com.example.dccworkflow.utils.LikeWrap;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " does not found!"));
    }

    @Transactional
    public User changePwd(Long userId, String oldPassword, String newPassword)
            throws UserNotFoundException, OldPasswordErrorException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new OldPasswordErrorException();
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return user;
    }

    @Transactional
    public User getUserById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getUserDto(String search, Pageable pageable) {
        QUser qUser = QUser.user;

        BooleanBuilder whereCase = new BooleanBuilder();
        whereCase.or(qUser.username.like(LikeWrap.like(search)))
                .or(qUser.email.like(LikeWrap.like(search)))
                .or(qUser.phone.like(LikeWrap.like(search)));

        Page<User> userPage = userRepository.findAll(whereCase, pageable);

        List<UserDto> content = userPage.getContent()
                .stream().map(user -> {
                    UserDto dto = new UserDto();
                    BeanUtils.copyProperties(user, dto);
                    return dto;
                })
                .toList();

        return new PageImpl<>(content, pageable, userPage.getTotalElements());
    }

    @Transactional
    public User addUser(String username,
                        String password,
                        String email,
                        String phone,
                        Boolean enable) {
        User user = new User();
        user.setUsername(username);
        if (password == null || password.isBlank()) {
            user.setPassword(passwordEncoder.encode(username));
        } else {
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setEmail(email);
        user.setPhone(phone);
        user.setEnabled(enable);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long userId,
                           String username,
                           String password,
                           String email,
                           String phone,
                           Boolean enable) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setEmail(email);
        user.setPhone(phone);
        user.setEnabled(enable);
        return user;
    }

    @Transactional
    public void removeUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public User editUserRole(Long userid, List<Long> roleIds) throws UserNotFoundException {
        List<Role> roleList = roleRepository.findAllById(roleIds);

        User user = userRepository.findById(userid)
                .orElseThrow(UserNotFoundException::new);
        user.setRoles(roleList.stream().collect(Collectors.toSet()));

        return user;
    }

    @Transactional(readOnly = true)
    public List<RoleDto> getUserHasRoleDto(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return user.getRoles().stream()
                .map(role -> {
                    RoleDto dto = new RoleDto();
                    BeanUtils.copyProperties(role, dto);
                    return dto;
                })
                .toList();
    }
}
