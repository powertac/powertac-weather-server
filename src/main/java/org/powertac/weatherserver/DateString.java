package org.powertac.weatherserver;

import java.sql.Date;
import java.util.Calendar;

public class DateString {
	private Date sqlDate;
	private Calendar date;
	public DateString(String input){
		sqlDate = new Date(0);
		date = Calendar.getInstance();
		update(input);
	}
	
	public void update(String input){
		//if (input.length() != 16){
		//	System.out.println("Date string error "+ input + " : " + input.length() );
		//}
		String hh = input.substring(0, 2);
		String dd = input.substring(2, 4);
		String mm = input.substring(4, 6);
		String yyyy = input.substring(6);
		date.set(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd), Integer.parseInt(hh), 0);
		System.out.println("Hour: " + hh);
		System.out.println("Day: " + dd);
		System.out.println("Month: " + mm);
		System.out.println("Year: " + yyyy);
	}
	
	public Date getSqlDate(){
		return new Date(date.getTimeInMillis());
	}
	
	public void setSqlDate(Date sqlDate){
		this.sqlDate = sqlDate;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

}
