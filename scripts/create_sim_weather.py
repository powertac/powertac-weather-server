"""
This script produces a weatherfile for running an offline or repeated sim
"""


from datetime import datetime, timedelta
import urllib
import sys
from xml.dom import minidom
from xml.dom.minidom import Document


DAYS = 125
LOCATION = "rotterdam" # only available for now
BASE_URL = "http://wolf-08.fbk.eur.nl:8080/"\
           "WeatherServer/faces/index.xhtml"\
           "?weatherDate=%s&weatherLocation=" + LOCATION


def parse_start_date():
    if len(sys.argv) == 1:
        print "\nYou need to give the start date"
        print "The format is yyyymmdd\n"
        return

    try:
        start_date = datetime.strptime(sys.argv[1], "%Y%m%d")
    except:
        print '\nThe format of the start date is wrong'
        print "The format is yyyymmdd\n"
        return

    return start_date


def prettify(elem):
    rough_string = ElementTree.tostring(elem, 'utf-8')
    reparsed = minidom.parseString(rough_string)
    return reparsed.toprettyxml(indent="")


def loop_days(doc, reports, forecasts, energys, start_date):
    for i in range(DAYS):
        date_string = start_date.strftime("%Y%m%d00")
        get_day(doc, reports, forecasts, energys, date_string)
        start_date += timedelta(days=1)


def get_day(doc, reports, forecasts, energys, date_string):
    url = BASE_URL % date_string
    dom = minidom.parse(urllib.urlopen(url))

    for node in dom.getElementsByTagName('weatherReport'):
        # Not used right now, remove for space efficiency
        node.removeAttribute("location")
        reports.appendChild(node)
    
    for node in dom.getElementsByTagName('weatherForecast'):
        # Both not used right now, remove for space efficiency
        node.removeAttribute("location")
        node.removeAttribute("date")
        forecasts.appendChild(node)

    # For now no energy reports


def main():
    start_date = parse_start_date()
    if not start_date:
        return

    doc = Document()

    root_element = doc.createElement('data')
    doc.appendChild(root_element)

    reports = doc.createElement('weatherReports')
    root_element.appendChild(reports)
    forecasts = doc.createElement('weatherForecasts')
    root_element.appendChild(forecasts)
    energys = doc.createElement('energyReports')
    root_element.appendChild(energys)

    loop_days(doc, reports, forecasts, energys, start_date)

    f = open('weather.xml', 'w')
    f.write(doc.toprettyxml(indent=''))
    f.close()


if __name__ == "__main__":
    main()

