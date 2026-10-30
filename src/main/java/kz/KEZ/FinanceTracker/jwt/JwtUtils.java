package kz.KEZ.FinanceTracker.jwt;



import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import kz.KEZ.FinanceTracker.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt_secret}")
    private String secret;

    @Value("${jwt_expiration}")
    private long expiration;


    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.err.println("Неверная подпись токена: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("Некорректный токен: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("Токен просрочен: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Неподдерживаемый токен: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Пустой токен: " + e.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
