package kr.unlike.redis.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
	private Integer code;
	private String message;
	
	public Result ok() {
		this.code = HttpStatus.OK.value();
		this.message = "success";
		return this;
	}
}
