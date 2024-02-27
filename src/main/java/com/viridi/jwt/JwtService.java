package com.viridi.jwt;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.viridi.entity.User;
import com.viridi.repo.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Autowired
	private TokenRepository tokenRepository;
	
	
	public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;
	
	private static final String SECRET_KEY = "499b8a24b82a2236a9f5918120bf4471b3f27bf97064c06534c45d6727d36dde";
	
	
	
	
	public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);

        
        boolean validToken = tokenRepository
                .findByToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);
		
        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken; //&& validToken;
    }
	
	private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
	
	private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
	
	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }
	
	private Claims extractAllClaims(String token) {
		return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
	}
		
	
	public String generateToken(User user) {
		
		String token = Jwts
				.builder()
				.subject(user.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000 ))
				.signWith(getSigninKey())
				.compact();
		
		return token;
		
	}

	private SecretKey getSigninKey() {
		
		byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
