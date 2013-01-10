package org.powertac.weatherserver;

import java.sql.Date;
import java.util.Calendar;


public class DateString {
	private Calendar date;
	private String localeString;

	public DateString (String input)
  {
		date = Calendar.getInstance();

		// Check if dates are integers, and 10 chars long
		try {
			long integerInput = Long.parseLong(input);
      if (input.length() == 10) {
        update(input);
      } else if (input.length() < 10) {
        update(String.format("%-10s", integerInput).replace(' ', '0'));
      } else {
        update(input.substring(0, 10));
      }
		} catch (NumberFormatException e) {
			// If there is an error parsing the integer update with zero date
			update("0000000000");
		}
	}

	private void update (String input)
  {
    String yyyy = input.substring(0, 4);
    int year = Math.min(Math.max(Integer.parseInt(yyyy), 1), 3000);
    String mm = input.substring(4, 6);
    int month = Math.min(Math.max(Integer.parseInt(mm), 1), 12);
		String dd = input.substring(6, 8);
    int day = Math.min(Math.max(Integer.parseInt(dd), 1), 31);
    String hh = input.substring(8);
    int hour = Math.min(Math.max(Integer.parseInt(hh), 0), 23);

		date.set(year, month - 1, day, hour, 0);
    updateLocateString();
	}

	public Date getSqlDate ()
  {
		Date sql = new Date(0);
		sql.setTime(date.getTime().getTime());
		return sql;
	}

	public String getRestString ()
  {
		return String.format("%04d%02d%02d%02d",
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH) + 1,
        date.get(Calendar.DAY_OF_MONTH),
        date.get(Calendar.HOUR_OF_DAY));
	}

	public void shiftBackDay ()
  {
    date.add(Calendar.DATE, -1);
    updateLocateString();
	}

	public void shiftAheadDay ()
  {

    date.add(Calendar.DATE, 1);
    updateLocateString();
	}

	public void shiftAheadHour ()
  {
    date.add(Calendar.HOUR, 1);
    updateLocateString();
	}

	public void shiftBackHour ()
  {
    date.add(Calendar.HOUR, -1);
    updateLocateString();
	}

  private void updateLocateString ()
  {
    localeString = String.format("%04d-%02d-%02d %02d:00:00",
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH) + 1,
        date.get(Calendar.DAY_OF_MONTH),
        date.get(Calendar.HOUR_OF_DAY));
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
