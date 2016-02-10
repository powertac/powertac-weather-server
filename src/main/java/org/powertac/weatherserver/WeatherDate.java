package org.powertac.weatherserver;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class WeatherDate
{
  private Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
  private Properties properties = new Properties();

  public WeatherDate (String input, boolean validate)
  {
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
      date = getMinDate();
    }

    if (validate) {
      validateDate();
    }
  }

  public WeatherDate (ResultSet resultSet, String column)
  {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
      Date dateTime = sdf.parse(String.format("%s %s",
          resultSet.getDate(column).toString(),
          resultSet.getTime(column).toString().substring(0, 5)));
      date.setTime(dateTime);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    validateDate();
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
  }

  private void validateDate ()
  {
    Calendar minDate = getMinDate();
    Calendar maxDate = getMaxDate();

    if (date.before(minDate)) {
      date = minDate;
    }

    if (date.after(maxDate)) {
      date = maxDate;
    }
  }

  public String getSmallString ()
  {
    return String.format("%04d%02d%02d%02d",
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH) + 1,
        date.get(Calendar.DAY_OF_MONTH),
        date.get(Calendar.HOUR_OF_DAY));
  }

  public String getMediumString ()
  {
    return String.format("%04d-%02d-%02d %02d:00",
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH) + 1,
        date.get(Calendar.DAY_OF_MONTH),
        date.get(Calendar.HOUR_OF_DAY));
  }

  public String getFullString ()
  {
    return String.format("%04d-%02d-%02d %02d:00:00",
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH) + 1,
        date.get(Calendar.DAY_OF_MONTH),
        date.get(Calendar.HOUR_OF_DAY));
  }

  public long getTime ()
  {
    return date.getTimeInMillis();
  }

  public void shiftBackDay ()
  {
    date.add(Calendar.DATE, -1);
  }

  public void shiftAheadDay ()
  {

    date.add(Calendar.DATE, 1);
  }

  public void shiftAheadHour ()
  {
    date.add(Calendar.HOUR, 1);
  }

  private Calendar getMinDate ()
  {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
      sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

      Calendar minDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
      minDate.setTime(sdf.parse(properties.getProperty("publicDateMin")));
      return minDate;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private Calendar getMaxDate ()
  {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
      sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

      Calendar maxDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
      maxDate.setTime(sdf.parse(properties.getProperty("publicDateMax")));
      return maxDate;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String getMinDateFull ()
  {
    Calendar minDate = getMinDate();

    return String.format("%04d-%02d-%02d %02d:00:00",
        minDate.get(Calendar.YEAR),
        minDate.get(Calendar.MONTH) + 1,
        minDate.get(Calendar.DAY_OF_MONTH),
        minDate.get(Calendar.HOUR_OF_DAY));
  }
}