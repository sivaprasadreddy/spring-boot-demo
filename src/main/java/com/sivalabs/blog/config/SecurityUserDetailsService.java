package com.sivalabs.blog.config;

import com.sivalabs.blog.users.UsersAPI;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
class SecurityUserDetailsService implements UserDetailsService {
    private final UsersAPI usersAPI;

    SecurityUserDetailsService(UsersAPI usersAPI) {
        this.usersAPI = usersAPI;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String userName) {
        var user = usersAPI
                .findByEmail(userName)
                .map(u -> new User(
                        u.email(), u.password(),
                        Set.of(new SimpleGrantedAuthority(u.role().name()))
                ));
        return user.orElseThrow(() -> new UsernameNotFoundException("Email " + userName + " not found"));
    }
}
