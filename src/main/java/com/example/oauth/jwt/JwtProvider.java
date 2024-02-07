package com.example.oauth.jwt;

import com.example.oauth.security.principal.PrincipalDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(JwtProperties.SECRET));

    public GeneratedTokenDto generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .subject(authentication.getName())
                .claim("auth", authorities)
                .signWith(secretKey)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(JwtProperties.ACCESS_TOKEN_EXPIRATION_HOURS, ChronoUnit.HOURS)))
                .compact();

        String refreshToken = Jwts.builder()
                .subject(authentication.getName())
                .signWith(secretKey)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(JwtProperties.REFRESH_TOKEN_EXPIRATION_HOURS, ChronoUnit.HOURS)))
                .compact();

        return new GeneratedTokenDto(accessToken, refreshToken);
    }

    public void verify(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException e) {
//            throw new CustomResponseStatusException(ErrorCode.JWT_TOKEN_NOT_SUPPORTED);
        } catch (ExpiredJwtException e) {
//            throw new CustomResponseStatusException(ErrorCode.JWT_TOKEN_EXPIRED);
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        String userId = claims.getSubject();

        if (!StringUtils.hasText(userId) || claims.get("auth") == null) {
//            throw new CustomResponseStatusException(ErrorCode.JWT_TOKEN_NOT_SUPPORTED);
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        PrincipalDetails principal = new PrincipalDetails(userId, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException e) {
//            throw new CustomResponseStatusException(ErrorCode.JWT_TOKEN_NOT_SUPPORTED);
        } catch (ExpiredJwtException e) {
//            throw new CustomResponseStatusException(ErrorCode.JWT_TOKEN_EXPIRED);
        }
        return null;
    }

}
