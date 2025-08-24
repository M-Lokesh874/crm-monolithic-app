package com.crm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for JWT (JSON Web Token) operations.
 * 
 * This class provides methods for generating, parsing, and validating JWT tokens
 * used for user authentication in the CRM system. It handles token creation,
 * extraction of claims, and validation against user details.
 * 
 * Key features include:
 * <ul>
 *   <li>JWT token generation with configurable expiration</li>
 *   <li>Token validation and claim extraction</li>
 *   <li>Username extraction from tokens</li>
 *   <li>Expiration date checking</li>
 *   <li>Configurable secret key and expiration time</li>
 * </ul>
 * 
 * The class uses the io.jsonwebtoken library for JWT operations and
 * Spring Security for user details integration.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see io.jsonwebtoken.Jwts
 */
@Component
public class JwtUtil {
    
    /**
     * Secret key used for JWT signing and verification.
     * Injected from application configuration.
     */
    @Value("${jwt.secret}")
    private String secret;
    
    /**
     * JWT token expiration time in milliseconds.
     * Injected from application configuration.
     */
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * Generates a JWT token for the specified username.
     * 
     * This method creates a new JWT token containing the username as a claim.
     * The token is signed using the configured secret key and has the
     * configured expiration time.
     * 
     * @param username The username to include in the token
     * @return The generated JWT token as a string
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
    
    /**
     * Generates a JWT token for the specified user details.
     * 
     * This method creates a new JWT token containing the username from
     * the UserDetails object. The token is signed using the configured
     * secret key and has the configured expiration time.
     * 
     * @param userDetails The UserDetails object containing user information
     * @return The generated JWT token as a string
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }
    
    /**
     * Creates a JWT token with the specified claims and subject.
     * 
     * This private method handles the actual JWT token creation process,
     * including setting the issuer, subject, issued date, expiration date,
     * and signing the token with the secret key.
     * 
     * @param claims The claims to include in the token
     * @param subject The subject (username) for the token
     * @return The created JWT token as a string
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Gets the signing key for JWT operations.
     * 
     * This method creates a SecretKey object from the configured secret
     * string, ensuring it's properly formatted for JWT signing.
     * 
     * @return The SecretKey for JWT signing
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    /**
     * Extracts the username from a JWT token.
     * 
     * This method parses the JWT token and extracts the subject claim,
     * which contains the username.
     * 
     * @param token The JWT token to extract the username from
     * @return The username extracted from the token, or null if extraction fails
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extracts the expiration date from a JWT token.
     * 
     * This method parses the JWT token and extracts the expiration claim.
     * 
     * @param token The JWT token to extract the expiration from
     * @return The expiration date extracted from the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extracts a specific claim from a JWT token.
     * 
     * This generic method allows extraction of any claim from the JWT token
     * using a claims resolver function.
     * 
     * @param token The JWT token to extract the claim from
     * @param claimsResolver Function to resolve the specific claim
     * @param <T> The type of the claim to extract
     * @return The extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extracts all claims from a JWT token.
     * 
     * This method parses the JWT token and returns all claims contained
     * within it. The token is verified using the secret key.
     * 
     * @param token The JWT token to extract claims from
     * @return Claims object containing all token claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Checks if a JWT token has expired.
     * 
     * This method extracts the expiration date from the token and
     * compares it with the current time to determine if the token
     * is still valid.
     * 
     * @param token The JWT token to check for expiration
     * @return true if the token has expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Validates a JWT token against user details.
     * 
     * This method checks if the token is valid by verifying that:
     * <ul>
     *   <li>The username in the token matches the provided user details</li>
     *   <li>The token has not expired</li>
     * </ul>
     * 
     * @param token The JWT token to validate
     * @param userDetails The UserDetails object to validate against
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * Validates a JWT token against a username.
     * 
     * This method checks if the token is valid by verifying that:
     * <ul>
     *   <li>The username in the token matches the provided username</li>
     *   <li>The token has not expired</li>
     * </ul>
     * 
     * @param token The JWT token to validate
     * @param username The username to validate against
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }
    
    public Date getExpirationDate(String token) {
        return extractExpiration(token);
    }
}
