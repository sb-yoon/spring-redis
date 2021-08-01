package kr.unlike.redis.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.unlike.redis.auth.JwtTokenProvider;
import kr.unlike.redis.domain.User;
import kr.unlike.redis.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthAspect {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer ";

	private final JwtTokenProvider tokenProvider;

	@Around("@annotation(isAuthenticated)")
	public Object loginCheck(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		Object[] args = joinPoint.getArgs();

		String token = null;
		String bearer = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
			token = bearer.substring(7);
		}

		if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
			User user = tokenProvider.getAuthentication(token);
			if (user == null)
				throw new InvalidTokenException("유효하지 않는 토큰입니다.");
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof User) {
					args[i] = user;
					break;
				}
			}
		} else {
			throw new InvalidTokenException("유효하지 않는 토큰입니다.");
		}

		return joinPoint.proceed(args);
	}
}
