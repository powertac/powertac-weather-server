package org.powertac.weatherserver;

import java.sql.Date;
import java.util.Calendar;

public class DateString {
	private Date sqlDate;
	private Calendar date;
	private String localeString;

	public DateString(String input) {
		sqlDate = new Date(0);
		date = Calendar.getInstance();
		update(input);
	}

	public void update(String input) {
		// if (input.length() != 16){
		// System.out.println("Date string error "+ input + " : " +
		// input.length() );
		// }
		String hh = input.substring(0, 2);
		String dd = input.substring(2, 4);
		String mm = input.substring(4, 6);
		String yyyy = input.substring(6);
		date.set(Integer.parseInt(yyyy), Integer.parseInt(mm),
				Integer.parseInt(dd), Integer.parseInt(hh), 0);
		String tmp = String.format("%s-%s-%s %s:%s:%s", yyyy, mm, dd, hh, "00",
				"00");
		this.setLocaleString(tmp);

	}

	public Date getSqlDate() {
		Date sql = new Date(0);
		sql.setTime(date.getTime().getTime());
		return sql;// new Date(date.getTimeInMillis());
	}

	public void setSqlDate(Date sqlDate) {
		this.sqlDate = sqlDate;
	}

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

	public void shiftBackDay() {
		date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) - 1);
		String tmp = String.format("%s-%s-%s %s:%s:%s",
				date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1,
				date.get(Calendar.DAY_OF_MONTH),
				date.get(Calendar.HOUR_OF_DAY), "00", "00");
		this.setLocaleString(tmp);
	}

	public void shiftAheadDay() {
		date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) + 1);
		String tmp = String.format("%s-%s-%s %s:%s:%s",
				date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1,
				date.get(Calendar.DAY_OF_MONTH),
				date.get(Calendar.HOUR_OF_DAY), "00", "00");
		this.setLocaleString(tmp);
	}

}
