"""
This script checks 2 things :
- Are all reports and forecasts available for given location and period
- Calculates the standard deviations for the different forecast lengths
Slow, because it uses http instead of direct DB access, in 2 runs.
"""

from datetime import datetime, timedelta
import math
import urllib
import sys
from xml.dom import minidom
from xml.dom.minidom import Document


LOCATION   = "rotterdam"
START_DATE = "20090101" # YYYYMMDD of earliest report
END_DATE   = "20111229" # YYYYMMDD of last report

BASE_URL = "http://localhost:8080/WeatherServer/faces/index.xhtml"\
           "?weatherDate=%s&weatherLocation=%s"

RANGE_24 = range(24)
RANGE_24_24 = [range(24)] * 24


def check_all_dates():
    end_date = datetime.strptime(END_DATE, "%Y%m%d")
    origin = datetime.strptime(START_DATE, "%Y%m%d")

    while origin < end_date:
        date_string = origin.strftime("%Y%m%d00")
        url = BASE_URL % (date_string, LOCATION)
        dom = minidom.parse(urllib.urlopen(url))

        weathers = []
        origins = []
        forecasts = []
        forecast_ids = []
        for node in dom.getElementsByTagName('weatherReport'):
            date_string = node.getAttribute("date")
            date = datetime.strptime(date_string, "%Y-%m-%d %H:00")
            diff = (date - origin).seconds / 3600
            weathers.append(diff)
   
        for node in dom.getElementsByTagName('weatherForecast'):
            id_string = node.getAttribute("id")
            org_string = node.getAttribute("origin")
            date_string = node.getAttribute("date")
            org  = datetime.strptime(org_string,  "%Y-%m-%d %H:00")
            date = datetime.strptime(date_string, "%Y-%m-%d %H:00")

            org_diff = (org - origin).seconds / 3600
            if not org_diff in origins:
                origins.append(org_diff)

            diff = (date - org).seconds / 3600 + (date - org).days * 24
            while True:
                try:
                    forecasts[org_diff]
                    break
                except:
                    forecasts.append([])
            forecasts[org_diff].append(diff - 1)

            while True:
                try:
                    forecast_ids[org_diff]
                    break
                except:
                    forecast_ids.append([])
            forecast_ids[org_diff].append(int(id_string))

        any_error = False
        if sorted(weathers) != RANGE_24:
            print "Weather alarm !!!!!"
            print origin
            print weathers
            any_error = True

        if sorted(origins) != RANGE_24:
            print "Origins alarm !!!!!"
            print origin
            print origins
            any_error = True

        if sorted(forecasts) != RANGE_24_24:
            print "Forecasts alarm !!!!!"
            print origin
            print forecasts
            any_error = True

        if sorted(forecast_ids) != RANGE_24_24:
            print "Forecast_ids alarm !!!!!"
            print origin
            print forecast_ids
            any_error = True

        if any_error:
            print

        origin += timedelta(days=1)


def verify_data():
    end_date = datetime.strptime(END_DATE, "%Y%m%d")
    origin = datetime.strptime(START_DATE, "%Y%m%d")

    means_temp      = [0] * 24
    errors_temp     = [0] * 24
    means_speed     = [0] * 24
    errors_speed    = [0] * 24
    means_dir       = [0] * 24
    errors_dir      = [0] * 24
    means_cloud     = [0] * 24
    errors_cloud    = [0] * 24
    totals = 0

    origin += timedelta(days=1)
    date_string = origin.strftime("%Y%m%d00")
    url = BASE_URL % (date_string, LOCATION)
    dom = minidom.parse(urllib.urlopen(url))

    while origin < end_date:
        weathers = {}
        forecasts = {}

        for node in dom.getElementsByTagName('weatherReport'):
            weathers[node.getAttribute("date")] = node
    
        for node in dom.getElementsByTagName('weatherForecast'):
            index= node.getAttribute("origin") + "_" + node.getAttribute("date")
            forecasts[index] = node

        date_string = (origin + timedelta(days=1)).strftime("%Y%m%d00")
        url = BASE_URL % (date_string, LOCATION)
        dom = minidom.parse(urllib.urlopen(url))

        for node in dom.getElementsByTagName('weatherReport'):
            weathers[node.getAttribute("date")] = node
   
        for i in range(24):
            try:
                weather_date = origin + timedelta(hours=i)
                weather = weathers[str(weather_date)[:-3]]
                for j in range(0, 24):
                    forecast_date = weather_date + timedelta(hours=(j+1))
                    index = str(weather_date)[:-3] + "_" + str(forecast_date)[:-3]
                    forecast = forecasts[index]

                    error_temp  = float(weather.getAttribute("temp")) - \
                            float(forecast.getAttribute("temp"))
                    error_speed = float(weather.getAttribute("windspeed")) - \
                            float(forecast.getAttribute("windspeed"))
                    error_dir   = float(weather.getAttribute("winddir")) - \
                            float(forecast.getAttribute("winddir"))
                    error_cloud = float(weather.getAttribute("cloudcover")) - \
                            float(forecast.getAttribute("cloudcover"))

                    means_temp[j]   += error_temp
                    errors_temp[j]  += error_temp ** 2
                    means_speed[j]  += error_speed
                    errors_speed[j] += error_speed ** 2
                    means_dir[j]    += error_dir
                    errors_dir[j]   += error_dir ** 2
                    means_cloud[j]  += error_cloud
                    errors_cloud[j] += error_cloud ** 2
            except:
                pass

            totals += 1

        origin += timedelta(days=1)

    print
    print "Average diff from mean for ascending forecast period"
    print "temp    speed   dir     cloud"
    for i in range(24):
        temp  = "%.3f" % (means_temp [i] / totals)
        speed = "%.3f" % (means_speed[i] / totals)
        dir   = "%.3f" % (means_dir  [i] / totals)
        cloud = "%.3f" % (means_cloud[i] / totals)

        print "%s %s %s %s" % (
                temp. rjust(6, ' '),
                speed.rjust(8, ' '),
                dir.  rjust(8, ' '),
                cloud.rjust(8, ' '))

    print
    print "Average std dev for ascending forecast period"
    print "temp    speed   dir     cloud"
    for i in range(24):
        temp  = "%.3f" % math.sqrt(errors_temp [i] / totals)
        speed = "%.3f" % math.sqrt(errors_speed[i] / totals)
        dir   = "%.3f" % math.sqrt(errors_dir  [i] / totals)
        cloud = "%.3f" % math.sqrt(errors_cloud[i] / totals)

        print "%s %s %s %s" % (
                temp. rjust(6, ' '),
                speed.rjust(8, ' '),
                dir.  rjust(8, ' '),
                cloud.rjust(8, ' '))


def main():
    check_all_dates()
    verify_data()

if __name__ == "__main__":
    main()
