package com.example.oauth.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributeMap;
    private String attributeKey;
    private String email;
    private String name;
    private String picture;
    private String provider;

    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributeMap) {
        if ("kakao".equals(provider)) {
            return ofKakao(provider,"email", attributeMap);
        } else if ("google".equals(provider)) {
            return ofGoogle(provider, attributeKey, attributeMap);
        } else if ("naver".equals(provider)) {
            return ofNaver(provider, "id", attributeMap);
        }
        throw new RuntimeException("Unsupported Provider = " + provider);
    }

    private static OAuth2Attribute ofKakao(String provider, String attributeKey, Map<String, Object> attributeMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> kakaoAccount = objectMapper.convertValue(attributeMap.get("kakao_account"), Map.class);

        return OAuth2Attribute.builder()
                .email((String) kakaoAccount.get("email"))
                .provider(provider)
                .attributeMap(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attribute ofGoogle(String provider, String attributeKey, Map<String, Object> attributeMap) {
        return OAuth2Attribute.builder()
                .email((String) attributeMap.get("email"))
                .provider(provider)
                .attributeMap(attributeMap)
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attribute ofNaver(String provider, String attributeKey, Map<String, Object> attributeMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> response = objectMapper.convertValue(attributeMap.get("response"), Map.class);

        return OAuth2Attribute.builder()
                .email((String) response.get("email"))
                .provider(provider)
                .attributeMap(attributeMap)
                .attributeKey(attributeKey)
                .build();
    }

    // OAuth2User 객체에 넣어주기 위해서 Map으로 값들을 반환해준다.
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("provider", provider);

        return map;
    }
}
