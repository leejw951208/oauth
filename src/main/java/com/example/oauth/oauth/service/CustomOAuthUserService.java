package com.example.oauth.oauth.service;

import com.example.oauth.oauth.OAuth2Attribute;
import com.example.oauth.user.entity.User;
import com.example.oauth.user.entity.UserRoles;
import com.example.oauth.user.repository.UserRepository;
import com.example.oauth.user.repository.UserRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CustomOAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Map<String, Object> userAttributeMap = oAuth2Attribute.convertToMap();

        String email = (String) userAttributeMap.get("email");

        Optional<User> findUser = userRepository.findByEmail(email);

        List<SimpleGrantedAuthority> authorities;

        if (findUser.isEmpty()) {
            userAttributeMap.put("exist", false);
            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            userAttributeMap.put("exist", true);
            List<UserRoles> findRoles = userRolesRepository.findByUser(findUser.get());
            authorities = findRoles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
                    .toList();
        }

        return new DefaultOAuth2User(authorities, userAttributeMap, "email");
    }
}
