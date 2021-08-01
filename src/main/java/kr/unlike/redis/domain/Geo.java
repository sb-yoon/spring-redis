package kr.unlike.redis.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Geo {
	private String userId;
	private Double lat;
	private Double lng;
	private Double distance;
}
