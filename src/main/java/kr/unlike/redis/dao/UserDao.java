package kr.unlike.redis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.unlike.redis.domain.User;


@Repository
@Mapper
public interface UserDao {
	User selectOne(Long id);
	User selectOneByEmail(String email);
}
