package org.ird.unfepi.util;

import static org.ird.unfepi.util.TestUtils.registerBoolean;
import static org.ird.unfepi.util.TestUtils.registerDate;
import static org.ird.unfepi.util.TestUtils.registerLocationType;
import static org.ird.unfepi.util.TestUtils.registerParentLocation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.List;

import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationNode;
import org.ird.unfepi.model.LocationTree;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;

public class LocationUtilsTest {
	
	private List<Location> locations;

	/** Before starting we have all our locations in a list sorted by level and then locationId
	 * This would ensure lesser searches for a location as parent of location would already be available
	 * when traversing. Eventually we would endup with a tree with this top to bottom tree building approach
	 */
	@Before
	public void setup() throws URISyntaxException, IOException {
		File csv = new File(this.getClass().getClassLoader().getResource("all_locations_by_level_locationid.csv").toURI().getPath());
	
		CsvMapper mapper = new CsvMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		registerLocationType(mapper);
		registerBoolean(mapper);
		registerDate(mapper);
		registerParentLocation(mapper);
		
		CsvSchema schema = CsvSchema.emptySchema().withHeader(); // use first row as header; otherwise defaults are fine
		MappingIterator<Location> it = mapper.readerFor(Location.class).with(schema).readValues(csv);
		
		locations = it.readAll();
		
		//System.out.println(new Gson().toJson(locations));
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionIfFirstLocationIsNotRoot() throws IllegalAccessException, InvocationTargetException {
		// change location order to make first entry non root i.e. add last entry as first
		locations.add(0, locations.get(locations.size()-1));
		
		LocationUtils.toTree(locations);
	}
	
	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionIfRootEncounteredInMid() throws IllegalAccessException, InvocationTargetException {
		// change location order to make first entry non root i.e. add last entry as first
		locations.get(10).setParentLocation(null);
		
		LocationUtils.toTree(locations);
	}
		
	@Test
	public void shouldHaveRootLocationsCorrectlyAssigned() throws IllegalAccessException, InvocationTargetException {
		LocationTree tree = LocationUtils.toTree(locations);
		final LocationNode c = tree.get(0).getChildren().get(0);
		tree.get(0).removeChildren();
		tree.get(0).addChild(c);
		System.out.println(new Gson().toJson(tree));
		//assertEquals(tree.getLocation().getLocationId().intValue(), 506);
	}
	
	public void testLocationTree() {
		// pakistan -  	null parent
		//		sindh - 	pak parent
		// 			karachi
		//			SBA
		// 		punjab -	pak parent
		// 
	}
}
