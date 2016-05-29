package info.thomazo.findgas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Station {

	private String id;

	private String name;
	private String address;
	private String cp;
	private String city;

	private String location;
}
