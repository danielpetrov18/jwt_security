package com.daniel.security_jwt.service;

import java.security.Key;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class JwtService {

    private static final String SECRET_KEY = "108c38ac60ffeb990d47204931c5461248df8a38e11c842926b83a09ecae1611";
    private static final Integer TIME_UNTIL_EXPIRATION = 1000 * 60 * 24;

    public String extractUsername(final String jwt) {
        return this.extractClaim(jwt, Claims::getSubject);
    }

    public String generateToken(final UserDetails userDetails) {
        return this.generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            final Map<String, Object> claims,
            final UserDetails userDetails)
    {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TIME_UNTIL_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(final String token, final UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(final String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] byteStream = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(byteStream);
    }

}