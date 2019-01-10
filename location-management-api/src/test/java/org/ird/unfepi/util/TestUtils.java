package org.ird.unfepi.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationType;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class TestUtils {
	
	public static void registerParentLocation(ObjectMapper mapper) {
		mapper.addHandler(new DeserializationProblemHandler() {
			@Override
			public Object handleMissingInstantiator(DeserializationContext ctxt, Class<?> instClass, ValueInstantiator valueInsta, JsonParser p, String msg) throws IOException {
				if(instClass.equals(Location.class) && p.hasCurrentToken()) {
					return new Location(p.getIntValue());
				}
				return super.handleMissingInstantiator(ctxt, instClass, valueInsta, p, msg);
			}
		});
	}
	
	public static void registerLocationType(ObjectMapper mapper) {
		mapper.registerModule(new SimpleModule().addDeserializer(LocationType.class, new JsonDeserializer<LocationType>() {
			@Override
			public LocationType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
				return new LocationType(p.getIntValue());
			}
		}));
	}
	
	public static void registerDate(ObjectMapper mapper) {
		mapper.registerModule(new SimpleModule().addDeserializer(Date.class, new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String date = p.getText();
		        if(p.getText() == null || p.getText().trim().equals("")) {
		        	return null;
		        }
		        try {
		            return format.parse(date);
		        } catch (ParseException e) {
		            throw new RuntimeException(e);
		        }
			}
		}));
	}

	public static void registerBoolean(ObjectMapper mapper) {
		mapper.registerModule(new SimpleModule().addDeserializer(boolean.class, new JsonDeserializer<Boolean>() {
			@Override
			public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
				return !"0".equals(p.getText());
			}
		}));
		mapper.registerModule(new SimpleModule().addDeserializer(Boolean.class, new JsonDeserializer<Boolean>() {
			@Override
			public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
				return !"0".equals(p.getText());
			}
		}));
	}
}
