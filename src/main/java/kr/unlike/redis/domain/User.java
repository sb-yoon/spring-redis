package kr.unlike.redis.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
   
	private static final long serialVersionUID = 3808300009986451626L;
	
	private Long id;
    private String email;
    private String nickname;
    private Date regDate;
    private Date modDate;
    
    private Double lat;
    private Double lng;
    private Double distance;
	private Date locationUpdateDate;
}
