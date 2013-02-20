"""
Generate deltas for the required targets.
Be careful, its very inefficient, could run for days.
"""

from datetime import datetime, timedelta
import math
import MySQLdb
import random

OUT_FILE = "deltas.txt"
MAX_ERROR = 0.0001
TOTAL = 1000000
DELTAS = [
    0.5161445829244465,
    0.4997949220914800,
    0.4888900890981265,
    0.4130704994139208,
    0.4253848976635191,
    0.4184687781309460,
    0.3936649202161004,
    0.3588710293244263,
    0.4231553114393557,
    0.2889082972475748,
    0.1702626162506641,
    0.1689796096722179,
    0.1883929096181789,
    0.18962579311684136,
    0.1305896052644996,
    0.11633757757456899,
    0.12243322493106974,
    0.12050860605514514,
    0.12994755220882373,
    0.08640391531140598,
    0.09885577242432499,
    0.10596546947835507,
    0.10847755148571396,
    0.0638920547195091
]
TARGETS = [
    0.5161290323,
    0.7188940092,
    0.8698156682,
    0.9631336406,
    1.0529953917,
    1.133640553,
    1.1993087558,
    1.2523041475,
    1.3214285714,
    1.3536866359,
    1.3859447005,
    1.418202765,
    1.4550691244,
    1.4919354839,
    1.5080645161,
    1.5241935484,
    1.5403225806,
    1.5564516129,
    1.5771889401,
    1.5887096774,
    1.599078341,
    1.6094470046,
    1.6152073733,
    1.6221198157
]


def loop(current):
    forecasts = []

    for _ in range(TOTAL):
        tau = 0
        temp = []
        for i in range(current+1):
            tau = tau + random.gauss(0, DELTAS[i])
            temp.append(tau)
        forecasts.append(temp)

    error = 0.0
    for forecast in forecasts:
        error += forecast[current] ** 2
    error = math.sqrt(error / len(forecasts))

    return error


def main():
    if len(DELTAS) < len(TARGETS):
        print "DELTAS smaller than TARGETS"
        return

    errors = [0.0] * len(TARGETS) 
    current = 0

    with open(OUT_FILE, 'w') as f:
        f.write("")

    while True:
        error = loop(current)

        diff = error - TARGETS[current]
        change = 1 - max(MAX_ERROR, abs(diff) / 5)

        print
        print current, DELTAS[current], change
        print error, TARGETS[current]
        print ("%.6f" % diff), 
        if diff > MAX_ERROR:
            print "diff larger"
            DELTAS[current] = DELTAS[current] * change
        elif diff < -MAX_ERROR:
            print "diff smaller"
            DELTAS[current] = DELTAS[current] / change
        else:   
            print "diff OK"
            with open(OUT_FILE, 'a') as f:
                f.write("%d %.16f\n" % (current, DELTAS[current]))
            errors[current] = error
            current += 1

        if current >= len(TARGETS):
            break

    print
    print len(errors), len(DELTAS)
    print
    print errors
    print
    print DELTAS

if __name__ == "__main__":
    main()

