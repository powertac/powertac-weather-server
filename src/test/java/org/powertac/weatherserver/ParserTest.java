package org.powertac.weatherserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ParserTest {
	@Test
	public void testNullParams() {
		assertEquals("Parser needs to return correct error",
        "Error null params", Parser.parseRestRequest(null));
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
