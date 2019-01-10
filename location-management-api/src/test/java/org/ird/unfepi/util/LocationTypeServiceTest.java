package org.ird.unfepi.util;

import static org.ird.unfepi.util.TestUtils.registerBoolean;
import static org.ird.unfepi.util.TestUtils.registerDate;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationType;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;

public class LocationTypeServiceTest {

	private List<LocationType> locationTypes;

	@Before
	public void setup() throws URISyntaxException, IOException {
		File csv = new File(this.getClass().getClassLoader().getResource("all_location_types.csv").toURI().getPath());
	
		CsvMapper mapper = new CsvMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		registerBoolean(mapper);
		registerDate(mapper);
		
		CsvSchema schema = CsvSchema.emptySchema().withHeader(); // use first row as header; otherwise defaults are fine
		MappingIterator<LocationType> it = mapper.readerFor(LocationType.class).with(schema).readValues(csv);
		
		locationTypes = it.readAll();
		
		System.out.println(new Gson().toJson(locationTypes));
	}
	
	@Test
	public void test() {
		
	}
}
