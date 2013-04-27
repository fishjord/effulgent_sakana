#!/usr/bin/python

import sys

if len(sys.argv) != 2:
    print >>sys.stderr, "USAGE: make_final_preditions.py <predictions file>"
    sys.exit(1)

predictions = {}
i = 0
for line in open(sys.argv[1]):
    lexemes = line.strip().split(",")
    if len(lexemes) != 2:
        raise IOError("Expected only two lexemes")

    label = int(lexemes[0])
    conf = float(lexemes[1])

    if label not in predictions:
        predictions[label] = []

    predictions[label].append((i, conf))
    i += 1

for label in sorted(predictions.keys()):
    class_predictions = sorted(predictions[label], key=lambda x: x[1])
    class_predictions.reverse()

    for i in range(min(100, len(class_predictions))):
        print "%s\t%s\t%s" % (label, class_predictions[i][0], class_predictions[i][1])
