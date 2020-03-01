package ru.vsu.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.vsu.jpa.domain.User;
import ru.vsu.jpa.domain.UserRoleEnum;
import ru.vsu.services.UserService;

import java.util.Optional;
import java.util.Set;

/**
 * @author Ivan Rovenskiy
 * 24 February 2020
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        if (StringUtils.isNumeric(id)) {
            final Optional<User> user = userService.findUserById(Integer.parseInt(id));

            if (user.isEmpty()) {
                throw new UsernameNotFoundException("Can't found user with id: " + id);
            }

            return new org.springframework.security.core.userdetails.User(
                    String.valueOf(user.get().getId()),
                    user.get().getPasswordHash(),
                    Set.of(new SimpleGrantedAuthority(UserRoleEnum.Admin.name()))); // FIXME [RIP]: 24/02/2020 every user - admin
        } else {
            throw new UsernameNotFoundException("Not valid user id");
        }
    }
}