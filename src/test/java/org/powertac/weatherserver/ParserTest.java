package org.powertac.weatherserver;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.junit.Test;
import org.powertac.weatherserver.database.Database;


public class ParserTest {
		

	@Test
	public void testNullParams() {
		assertEquals("Parser needs to return correct error", "Error null params",Parser.parseRestRequest(null));
	}
	
	@Test
	public void testBasicQueries(){
		/*
		Map<String,String[]> params = new HashMap<String,String[]>();
		String[] date = new String[1];
		date[0] = "0001012010";
		params.put("weatherDate", date);
		String result = Parser.parseRestRequest(params);
		assertTrue("weatherReports xml root not present",result.indexOf("<weatherReports>")>0);
		*/
				
	}

}
