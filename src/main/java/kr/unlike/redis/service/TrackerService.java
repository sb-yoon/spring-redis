package kr.unlike.redis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import kr.unlike.redis.domain.Geo;
import kr.unlike.redis.domain.User;
import kr.unlike.redis.dto.GeoRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackerService {
	private static final String TRACKER_GEO = "TRACKER_GEO";
	private static final String TRACKER_USER = "TRACKER_USER";

	@Autowired
	private RedisTemplate<String, String> geoTemplate;

	@Autowired
	private RedisTemplate<String, User> userTemplate;

	/**
	 * 트래커 위치 전송
	 * 
	 * @param request
	 * @param user
	 */
	public void save(GeoRequest request, User user) {
		BoundGeoOperations<String, String> bgop = geoTemplate.boundGeoOps(TRACKER_GEO);
		Point point = new Point(request.getLng(), request.getLat());
		bgop.add(point, user.getId().toString());

		HashOperations<String, String, User> hash = userTemplate.opsForHash();
		user.setLocationUpdateDate(new Date());
		hash.put(TRACKER_USER, user.getId().toString(), user);
	}

	/**
	 * 트래커 종료
	 * 
	 * @param user
	 */
	public void delete(User user) {
		geoTemplate.boundGeoOps(TRACKER_GEO).remove(user.getId().toString());
	}

	/**
	 * 트래커 현재위치 기반 반경 회원조회
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	public List<User> getGeoUserList(GeoRequest request, User currentUser) {
		Point point = new Point(request.getLng(), request.getLat());
		Metric metric = RedisGeoCommands.DistanceUnit.METERS;
		Distance distance = new Distance(200, metric);
		Circle circle = new Circle(point, distance);

		List<Geo> geos = new ArrayList<Geo>();

		RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
				.includeDistance()
				.includeCoordinates()
				.sortAscending()
				.limit(100);

		GeoResults<GeoLocation<String>> results = geoTemplate.opsForGeo().radius(TRACKER_GEO, circle, args);

		if (results != null) {
			for (GeoResult<GeoLocation<String>> result : results) {
				Geo geo = Geo.builder()
						.userId(result.getContent().getName())
						.lat(result.getContent().getPoint().getY())
						.lng(result.getContent().getPoint().getX())
						.distance(result.getDistance().getValue())
						.build();

				geos.add(geo);
			}
		}

		HashOperations<String, String, User> hash = userTemplate.opsForHash();
		List<User> users = hash.multiGet(TRACKER_USER,
				geos.stream()
						.filter(v -> !v.getUserId().equals(currentUser.getId().toString()))
						.map(v -> v.getUserId().toString())
						.collect(Collectors.toList()));

		for (User user : users) {
			for (Geo geo : geos) {
				if (geo.getUserId().equals(user.getId().toString())) {
					user.setLat(geo.getLat());
					user.setLng(geo.getLng());
					user.setDistance(geo.getDistance());
					break;
				}
			}
		}

		return users;
	}
}
