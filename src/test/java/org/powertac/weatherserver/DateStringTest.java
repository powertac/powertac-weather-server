package org.powertac.weatherserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class DateStringTest {

	@Test
	public void testInitialization() {
		// Test basic initialization
		DateString ds = new DateString("2012010100");
		String localeString1 = ds.getLocaleString();
		assertNotNull("DateString object is null after initialization", ds);
		assertNotNull("Locale String is null", localeString1);
		assertEquals("Conversion between Rest String to Locale String failed 1",
				"2012-01-01 00:00:00", localeString1);

		// Test edge case initialization
		DateString dsZero = new DateString("0001010000");
		String localeStringZero = dsZero.getLocaleString();
		assertNotNull("DateString object is null after initialization", dsZero);
		assertNotNull("Locale String is null", localeStringZero);
		assertEquals("Conversion between Rest String to Locale String failed 2",
				"0001-01-01 00:00:00", localeStringZero);

		// Test invalid date string
		DateString dsInvalid = new DateString("asdfghyjjk");
		String localeStringInvalid = dsInvalid.getLocaleString();
		assertNotNull("DateString object is null after initialization",
				dsInvalid);
		assertNotNull("Locale String is null", localeStringInvalid);
		assertEquals("Conversion between Rest String to Locale String failed 3",
				"0001-01-01 00:00:00", localeStringInvalid);

		// Test edge case where Rest String < 10 (pads zeros to the left)
		DateString dsYear = new DateString("2012");
		DateString dsMonth = new DateString("201201");
		DateString dsDay = new DateString("20120101");

		assertNotNull("Null DateString", dsYear);
		assertNotNull("Null DateString", dsMonth);
		assertNotNull("Null DateString", dsDay);

		String localeYear = dsYear.getLocaleString();
		String localeMonth = dsMonth.getLocaleString();
		String localeDay = dsMonth.getLocaleString();

		assertNotNull("Null locale string", localeYear);
		assertNotNull("Null locale string", localeMonth);
		assertNotNull("Null locale string", localeDay);

		assertEquals("Rest String to Locale failed", "2012-01-01 00:00:00",
				localeYear);
		assertEquals("Rest String to Locale failed", "2012-01-01 00:00:00",
				localeMonth);
		assertEquals("Rest String to Locale failed", "2012-01-01 00:00:00",
				localeDay);

		// Test edge case where String > 10 (truncates extra least significant
		// digits on the left
		DateString dsGreater = new DateString("201202020000");
		String localeStringGreater = dsGreater.getLocaleString();
		assertNotNull("DateString object is null after initialization",
				dsGreater);
		assertNotNull("Locale String is null", localeStringGreater);
		assertEquals("Conversion between Rest String to Locale String failed 4",
				"2012-02-02 00:00:00", localeStringGreater);
	}

	@Test
	public void outlierCaseTest(){
		String testString = "2009101023";
		DateString dsOut = new DateString(testString);
		assertEquals("Conversion between Rest String to Locale String failed","2009-10-10 23:00:00",dsOut.getLocaleString());
	}

	@Test
	public void shiftDatesTest() {
		// Test edge case initialization
		DateString dsZero = new DateString("2010000000");
		String localeStringZero = dsZero.getLocaleString();
		assertNotNull("DateString object is null after initialization", dsZero);
		assertNotNull("Locale String is null", localeStringZero);
		assertEquals("Conversion between Rest String to Locale String failed 5",
				"2010-01-01 00:00:00", localeStringZero);
		
		// Shift one hour ahead
		dsZero.shiftAheadHour();
		assertEquals("Shift ahead 1 hour failed","2010-01-01 01:00:00", dsZero.getLocaleString());
		dsZero.shiftAheadHour();
		assertEquals("Shift ahead 1 hour failed","2010-01-01 02:00:00", dsZero.getLocaleString());
		
		// Shift one day ahead
		dsZero.shiftAheadDay();
		assertEquals("Shift ahead 1 day failed","2010-01-02 02:00:00", dsZero.getLocaleString());
		dsZero.shiftAheadDay();
		assertEquals("Shift ahead 1 day failed","2010-01-03 02:00:00", dsZero.getLocaleString());
		
		// Shift ahead one hour to roll-over to next day
		DateString roll = new DateString("2011123100");
		assertEquals("Roll start is not correct","2011-12-31 00:00:00", roll.getLocaleString());
		roll.shiftAheadDay();
		assertEquals("Roll ahead day failed", "2012-01-01 00:00:00", roll.getLocaleString());
	}
}