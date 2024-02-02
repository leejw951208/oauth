package com.example.oauth.oauth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributeMap;
    private String attributeKey;
    private String nickname;
    private String picture;
    private String provider;

    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        if ("ofKakao".equals(provider)) {
            return ofKakao();
        }
        throw new RuntimeException();
    }

    private static OAuth2Attribute ofKakao() {
        return OAuth2Attribute.builder().build();
    }

    // OAuth2User 객체에 넣어주기 위해서 Map으로 값들을 반환해준다.
    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("nickname", nickname);
        map.put("provider", provider);

        return map;
    }
}
