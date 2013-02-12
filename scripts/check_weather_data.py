from datetime import datetime, timedelta
import MySQLdb


LOCATIONS=["rotterdam"]
START_DATE="20090101"   # YYYYMMDD of earliest report
END_DATE="20111231"     # YYYYMMDD of last report

DB_db    = "localhost"
DB_table = "powertac_weather"
DB_user  = "localUsername"
DB_pass  = "localPassword"


def check_location(location):
    con = MySQLdb.connect(DB_db, DB_user, DB_pass, DB_table)
    cur = con.cursor(MySQLdb.cursors.DictCursor)

    start_date = datetime.strptime(START_DATE, "%Y%m%d")
    end_date = datetime.strptime(END_DATE, "%Y%m%d") + timedelta(days=1)

    sql = "SELECT * FROM reports WHERE location = '%s' "\
            "AND weatherDate >= '%s' "\
            "AND weatherDate <= '%s'" %(location, start_date, end_date)
    cur.execute(sql)

    result = []
    delta = timedelta(hours=1)
    last = datetime.strptime(START_DATE, "%Y%m%d") - delta

    rows = cur.fetchall()
    for row in rows:
        date = row['weatherDate']

        if not date == (last + delta):
            result.append(date) 

        last = date

    end_date = datetime.strptime(END_DATE, "%Y%m%d") + timedelta(hours=23)
    if len(result) != 0:
        print
        print "There are missing reports for %s for the following dates :\n"
        for count, date in enumerate(result):
            print "", date
            if count > 10:
                print " ......."
                break
        print
        print "In total %s reports were missing" % len(result)
        print 
    elif len(rows) == 0:
        print
        print "For location '%s', period %s - %s : no reports found !!" % (
                location, START_DATE, END_DATE)
        print
    elif last != end_date:
        print
        print "There reports for %s ended early : %s" % (location, date)
        print 
    else:
        print
        print "For location '%s', period %s - %s : all reports present" % (
                location, START_DATE, END_DATE)
        print

    con.close() 


def main():
    for location in LOCATIONS:
        check_location(location)


if __name__ == "__main__":
    main()
