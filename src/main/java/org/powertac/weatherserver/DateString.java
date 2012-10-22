package org.powertac.weatherserver;

import java.sql.Date;
import java.util.Calendar;


public class DateString {
	private Calendar date;
	private String localeString;

	public DateString (String input)
  {
		date = Calendar.getInstance();

		// Check if dates are integers
		try {
			long integerInput = Long.parseLong(input);

			// Check if dates are 10 characters long
			int result = 0;
			if ((result = 10 - input.length()) == 0) {
				update(input);
			} else {
				if (result > 0) {
					update(String.format("%010d", integerInput));
				} else {
					update(input.substring(Math.abs(result)));
				}
			}
		} catch (NumberFormatException e) {
			// If there is an error parsing the integer update with zero date
			//e.printStackTrace();
			
			update("0000000000");
		}
	}

	private void update (String input)
  {
		//System.out.println("Input String: " + input);
		String hh = input.substring(0, 2);
		String dd = input.substring(2, 4);
		String mm = input.substring(4, 6);
		String yyyy = input.substring(6);
		date.set(Integer.parseInt(yyyy),
				Integer.parseInt(mm)-1<0?0:Integer.parseInt(mm)-1,
				Integer.parseInt(dd)==0?1:Integer.parseInt(dd),
				Integer.parseInt(hh),
				0);
		String tmp = String.format("%04d-%02d-%02d %02d:%s:%s",
				Integer.parseInt(yyyy), 
				Integer.parseInt(mm)<1?1:Integer.parseInt(mm),
				Integer.parseInt(dd)<1?1:Integer.parseInt(dd),
				Integer.parseInt(hh), "00", "00");

		this.setLocaleString(tmp);
	}

	public Date getSqlDate ()
  {
		Date sql = new Date(0);
		sql.setTime(date.getTime().getTime());
		return sql;// new Date(date.getTimeInMillis());
	}

	public String getRestString ()
  {
		String tmp = String.format("%02d%02d%02d%04d",
				date.get(Calendar.HOUR_OF_DAY),
				date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1,
				date.get(Calendar.YEAR));
		return tmp;
	}

	public void shiftBackDay ()
  {
		date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) - 1);
		String tmp = String.format("%04d-%02d-%02d %02d:%s:%s",
				date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1,
				date.get(Calendar.DAY_OF_MONTH),
				date.get(Calendar.HOUR_OF_DAY), "00", "00");
		this.setLocaleString(tmp);
	}

	public void shiftAheadDay ()
  {
		date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) + 1);
		//date.roll(Calendar.DAY_OF_YEAR, 1);
		String tmp = String.format("%04d-%02d-%02d %02d:%s:%s",
				date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1,
				date.get(Calendar.DAY_OF_MONTH),
				date.get(Calendar.HOUR_OF_DAY), "00", "00");
		this.setLocaleString(tmp);
	}

	public void shiftAheadHour ()
  {
		date.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY) + 1);
		//date.roll(Calendar.HOUR_OF_DAY, 1);
		String tmp = String.format("%04d-%02d-%02d %02d:%s:%s",
				date.get(Calendar.YEAR),
				date.get(Calendar.MONTH) + 1,
				date.get(Calendar.DAY_OF_MONTH),
				date.get(Calendar.HOUR_OF_DAY), "00", "00");
		this.setLocaleString(tmp);
	}

	public void shiftBackHour ()
  {
		date.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY) - 1);
		String tmp = String.format("%04d-%02d-%02d %02d:%s:%s",
				date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1,
				date.get(Calendar.DAY_OF_MONTH),
				date.get(Calendar.HOUR_OF_DAY), "00", "00");
		this.setLocaleString(tmp);
	}

  //<editor-fold desc="Setters and Getters">
  public Calendar getDate() {
    return date;
  }
  public void setDate(Calendar date) {
    this.date = date;
  }

  public String getLocaleString() {
    return localeString;
  }
  public void setLocaleString(String localeString) {
    this.localeString = localeString;
  }
  //</editor-fold>
}
