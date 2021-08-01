package kr.unlike.redis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.unlike.redis.auth.JwtTokenProvider;
import kr.unlike.redis.dao.UserDao;
import kr.unlike.redis.domain.User;
import kr.unlike.redis.dto.TokenResponse;
import kr.unlike.redis.dto.UserRequest;
import kr.unlike.redis.exception.BizException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserDao userDao;
	private final JwtTokenProvider tokenProvider;
	
	@Transactional
	public TokenResponse login(UserRequest request) {
		User user = userDao.selectOneByEmail(request.getEmail());
		if (user == null) {
			throw new BizException("존재하지 않는 회원");
		}

		String token = tokenProvider.generateToken(user.getId());
		return TokenResponse.builder().token(token).build();
	}
}
