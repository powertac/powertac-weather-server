package org.powertac.weatherserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class WeatherDateTest
{
	@Test
	public void testInitialization ()
  {
		// Test basic initialization
		WeatherDate wd = new WeatherDate("2012010100", false);
		String localeString1 = wd.getFullString();
		assertNotNull("WeatherDate object is null after initialization", wd);
		assertNotNull("Locale String is null", localeString1);
		assertEquals("Conversion between Rest String to Locale String failed 1",
				"2012-01-01 00:00:00", localeString1);

    // Test edge case initialization
    WeatherDate wdZero = new WeatherDate("0001010000", false);
		String localeStringZero = wdZero.getFullString();
		assertNotNull("WeatherDate object is null after initialization", wdZero);
		assertNotNull("Locale String is null", localeStringZero);
		assertEquals("Conversion between Rest String to Locale String failed 2",
				"0001-01-01 00:00:00", localeStringZero);

		// Test invalid date string
    WeatherDate wdInvalid = new WeatherDate("asdfghyjjk", false);
		String localeStringInvalid = wdInvalid.getFullString();
		assertNotNull("WeatherDate object is null after initialization",
				wdInvalid);
		assertNotNull("Locale String is null", localeStringInvalid);
		assertEquals("Conversion between Rest String to Locale String failed 3",
        wdInvalid.getMinDateFull(), localeStringInvalid);

		// Test edge case where Rest String < 10 (pads zeros to the left)
    WeatherDate wdYear = new WeatherDate("2012", false);
    WeatherDate wdMonth = new WeatherDate("201201", false);
    WeatherDate wdDay = new WeatherDate("20120101", false);

		assertNotNull("Null WeatherDate", wdYear);
		assertNotNull("Null WeatherDate", wdMonth);
		assertNotNull("Null WeatherDate", wdDay);

		String localeYear = wdYear.getFullString();
		String localeMonth = wdMonth.getFullString();
		String localeDay = wdMonth.getFullString();

		assertNotNull("Null locale string", localeYear);
		assertNotNull("Null locale string", localeMonth);
		assertNotNull("Null locale string", localeDay);

		assertEquals("Rest String to Locale failed",
        "2012-01-01 00:00:00", localeYear);
		assertEquals("Rest String to Locale failed",
        "2012-01-01 00:00:00", localeMonth);
		assertEquals("Rest String to Locale failed",
        "2012-01-01 00:00:00", localeDay);

		// Test edge case where String > 10 (truncates extra least significant
		// digits on the left
    WeatherDate wdGreater = new WeatherDate("201202020000", false);
		String localeStringGreater = wdGreater.getFullString();
		assertNotNull("WeatherDate object is null after initialization",
				wdGreater);
		assertNotNull("Locale String is null", localeStringGreater);
		assertEquals("Conversion between Rest String to Locale String failed 4",
				"2012-02-02 00:00:00", localeStringGreater);
	}

	@Test
	public void outlierCaseTest ()
  {
		String testString = "2009101023";
    WeatherDate wdOut = new WeatherDate(testString, false);
		assertEquals("Conversion between Rest String to Locale String failed 5",
        "2009-10-10 23:00:00", wdOut.getFullString());
	}

	@Test
	public void shiftDatesTest ()
  {
		// Test edge case initialization
    WeatherDate wdZero = new WeatherDate("2010000000", false);
		String localeStringZero = wdZero.getFullString();
		assertNotNull("WeatherDate object is null after initialization", wdZero);
		assertNotNull("Locale String is null", localeStringZero);
		assertEquals("Conversion between Rest String to Locale String failed 5",
				"2010-01-01 00:00:00", localeStringZero);
		
		// Shift one hour ahead
		wdZero.shiftAheadHour();
		assertEquals("Shift ahead 1 hour failed",
        "2010-01-01 01:00:00", wdZero.getFullString());
		wdZero.shiftAheadHour();
		assertEquals("Shift ahead 1 hour failed",
        "2010-01-01 02:00:00", wdZero.getFullString());
		
		// Shift one day ahead
		wdZero.shiftAheadDay();
		assertEquals("Shift ahead 1 day failed",
        "2010-01-02 02:00:00", wdZero.getFullString());
		wdZero.shiftAheadDay();
		assertEquals("Shift ahead 1 day failed",
        "2010-01-03 02:00:00", wdZero.getFullString());
		
		// Shift ahead one hour to roll-over to next day
    WeatherDate roll = new WeatherDate("2011123100", false);
		assertEquals("Roll start is not correct",
        "2011-12-31 00:00:00", roll.getFullString());
		roll.shiftAheadDay();
		assertEquals("Roll ahead day failed",
        "2012-01-01 00:00:00", roll.getFullString());
	}
}