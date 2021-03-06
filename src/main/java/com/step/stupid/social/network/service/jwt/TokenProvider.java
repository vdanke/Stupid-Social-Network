package com.step.stupid.social.network.service.jwt;

import com.step.stupid.social.network.configuration.AppProperties;
import com.step.stupid.social.network.model.User;
import com.step.stupid.social.network.model.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
//@PropertySources({
//        @PropertySource(value = "classpath:/token.properties")
//})
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final AppProperties appProperties;
//    private final Environment environment;
    private final UserDetailsService userDetailsServiceImpl;

    public String createToken(Authentication authentication) {
//        final String tokenExpiration = "token.expiration";
//        final String tokenSecret = "token.secret.key";

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
//
//        String hours = environment.getProperty(tokenExpiration);
//        String token = environment.getProperty(tokenSecret);

        Date now = new Date();
        Date expiration = Date.from(now.toInstant()
                .plus(Duration.ofHours(appProperties.getAuth().getExpiration())));

        return Jwts.builder()
                .setSubject(principal.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getToken())
                .compact();
    }

    public String createToken(User user) {
        Date now = new Date();
        Date expiration = Date.from(now.toInstant()
                .plus(Duration.ofHours(appProperties.getAuth().getExpiration())));

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getToken())
                .compact();
    }

    public UUID getIdFromToken(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getToken())
                .parseClaimsJws(token)
                .getBody();

        String subject = body.getSubject();

        return UUID.fromString(subject);
    }

    public Optional<String> getTokenFromRequest(HttpServletRequest request) {
        int bearerTokenStartsFrom = 7;

        String bearerToken = request.getHeader("Authorization");

        if (!StringUtils.isEmpty(bearerToken) && bearerToken.startsWith("Bearer")) {
            return Optional.of(bearerToken.substring(bearerTokenStartsFrom, bearerToken.length()));
        }
        return Optional.empty();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getToken())
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error(e.getMessage());
        } catch (MalformedJwtException e) {
            log.error(e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
