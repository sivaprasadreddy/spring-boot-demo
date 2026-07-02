package com.sivalabs.blog.users;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserContextUtils {
    private final UserService userService;

    UserContextUtils(UserService userService) {
        this.userService = userService;
    }

    public Long getCurrentUserIdOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Access denied");
        }
        var principal = authentication.getPrincipal();
        String emailId = null;
        if (principal instanceof String email) {
            emailId = email;
        } else if(principal instanceof UserDetails userDetails) {
            emailId = userDetails.getUsername();
        }
        if (emailId != null) {
            return userService.findByEmail(emailId)
                    .map(UserDto::id)
                    .orElseThrow(()-> new AccessDeniedException("Access denied"));
        }
        throw new AccessDeniedException("Access denied");
    }
}
