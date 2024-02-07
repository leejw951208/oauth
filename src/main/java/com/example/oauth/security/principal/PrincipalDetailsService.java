package com.example.oauth.security.principal;

import com.example.oauth.user.entity.User;
import com.example.oauth.user.entity.UserRoles;
import com.example.oauth.user.repository.UserRepository;
import com.example.oauth.user.repository.UserRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user"));

        List<UserRoles> findRoles = userRolesRepository.findByUser(findUser);
        if (CollectionUtils.isEmpty(findRoles)) {
            throw new UsernameNotFoundException("Not found roles");
        }

        List<SimpleGrantedAuthority> authorities = findRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .toList();

        return new PrincipalDetails(findUser.getEmail(), findUser.getPassword(), authorities);
    }
}
