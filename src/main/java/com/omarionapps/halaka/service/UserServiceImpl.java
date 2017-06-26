package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Role;
import com.omarionapps.halaka.model.User;
import com.omarionapps.halaka.repository.RoleRepository;
import com.omarionapps.halaka.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by Omar on 22-Apr-17.
 */
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserById(long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public void saveUser(User user) {
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //user.setStatus(true);
        user.setId(user.getId());
        System.out.println("User:" + user.toString());

        Role userRole = roleRepository.findByRole("ADMIN");
        System.out.println("Role: " + userRole.toString());
        userRole.setId(userRole.getId());
        userRole.setRole(userRole.getRole());

        Set<User> roleUsers = userRole.getUsers();
        if (roleUsers.contains(user)) roleUsers.add(user);
        userRole.setUsers(roleUsers);
        user.setRoles(user.getRoles());
        userRepository.save(user);
        roleRepository.save(userRole);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user  == null)
            throw new UsernameNotFoundException(email);

        return new UserDetailsImpl(user);
    }

    @Override
    public User findUserByUserDetails() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        User user = null;
        if (userDetails instanceof UserDetails) {
            username = ((UserDetails) userDetails).getUsername();
        } else {
            username = userDetails.toString();
        }
        if (username != null) {
            user = this.findUserByEmail(username);
        }
        return user;
    }
}
