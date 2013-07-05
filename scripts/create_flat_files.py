#!/usr/bin/env python

"""
This script produces flatfiles for the weather-server
"""

from datetime import datetime, timedelta
import os


START       = datetime(2009,  1,  1)
END         = datetime(2011, 12, 31)
LOCATIONS   = ["rotterdam"]

START       = datetime(2008,  8,  1)
END         = datetime(2010, 10,  3)
LOCATIONS   = ["minneapolis", "cleveland", "seattle", "san francisco",
               "palm springs", "phoenix", "memphis", "miami", "denver"]

BASE_URL    = "http://localhost:8080/"\
              "WeatherServer/faces/index.xhtml"\
              "?weatherDate=%s&weatherLocation=%s"
DIR         = "/home/govert/Dropbox/flat_files/%s/%s.%s.xml"


def wget_date(date, location):
    date_string = date.strftime("%Y%m%d00")
    url = BASE_URL % (date_string, location) 
    file_name = DIR % (location.replace(" ", "_"), date_string, location)

    print
    print date, date_string, url, file_name
    print 'wget "%s" -O %s' % (url, file_name)
    os.system('wget "%s" -O %s' % (url, file_name))


def main():
    for location in LOCATIONS:
        date = START
        while (True):
            wget_date(date, location)

            date += timedelta(1)
            if date > END:
                break


if __name__ == "__main__":
    main()

