//package com.costumemania.msgateway.configuration.security.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//
//@Service
//public class JWTUtil {
////    @Value("${jwt.secret}")
////    private String secret;
//    private String secretKey = "2a12dQNObNyF/E4NC8ZM/ktoVU31KSpZNMhTNHDEjvh/2z7zrfkoN6";
////    private String timeExpiration = "86480000";
//
////    private Key key;
////
////    public JWTUtil(Key key) {
////        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
////    }
//
//    public Claims getClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignatureKey()).build()
//                .parseClaimsJws(token).getBody();
//    }
//
//    public Date getExpirationDate(String token) {
//        return getClaims(token).getExpiration();
//    }
//
//    public boolean isExpired(String token) {
//        return getExpirationDate(token).before(new Date());
//    }
//
//    public Key getSignatureKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//}
