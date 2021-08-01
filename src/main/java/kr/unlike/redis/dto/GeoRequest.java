package kr.unlike.redis.dto;

import lombok.Data;

@Data
public class GeoRequest {
	private Double lat;
	private Double lng;
}
