package info.thomazo.findgas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patch {

	private String id;
	private String date;

	private String station;
	private String stationName;

	private String location;
	private String address;
	private String cp;
	private String city;

	private String comment;
	private String name;

}