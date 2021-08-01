package kr.unlike.redis.auth;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.unlike.redis.dao.UserDao;
import kr.unlike.redis.domain.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {

	private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
	
    @Value("${security.jwt.token.expire}")
    private long expireMilliSecond;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    private Key key;

    private final UserDao userDao;
    
    private static final String TRACKER_USER = "TRACKER_USER";
    
	@Autowired
	private final RedisTemplate<String, User> userTemplate;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long id) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + expireMilliSecond);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String getUserId(String token) {
    	Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    	
    	return claims.getSubject();
    }
    
    public User getAuthentication(String token) {
    	HashOperations<String, String, User> hash = userTemplate.opsForHash();
		User user =  hash.get(TRACKER_USER, getUserId(token));
		
		if (user == null) {
			user = userDao.selectOne(Long.parseLong(getUserId(token)));
			hash.put(TRACKER_USER, user.getId().toString(), user);
		}
		
		return user;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
