package com.sivalabs.blog.users;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.sivalabs.blog.ApplicationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenHelper {
    private static final Logger log = LoggerFactory.getLogger(TokenHelper.class);
    private static final String TOKEN_ISSUER = "SivaLabs";
    private final ApplicationProperties props;

    public TokenHelper(ApplicationProperties props) {
        this.props = props;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = this.parseToken(token);
        String username = claims.getSubject();
        Date expiration = claims.getExpiration();
        return username != null
                && username.equals(userDetails.getUsername())
                && userDetails.isEnabled()
                && expiration.after(new Date());
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String prefix = "Bearer ";
        if (authHeader != null && authHeader.startsWith(prefix)) {
            return authHeader.substring(prefix.length());
        }
        return null;
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = this.parseToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(props.tokenSecret().getBytes(UTF_8));
        Date issuedAt = new Date();
        Date expiration = generateExpirationDate(issuedAt);
        return Jwts.builder()
                .issuer(TOKEN_ISSUER)
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    private Claims parseToken(String token) {
        String secretString = props.tokenSecret();
        SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(UTF_8));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    private Date generateExpirationDate(Date date) {
        return new Date(date.getTime() + props.tokenExpiresInMinutes() * 60 * 60 * 1000);
    }
}
