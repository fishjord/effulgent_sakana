#!/usr/bin/python

import sys

if len(sys.argv) != 3:
    print >>sys.stderr, "USAGE: evaluate_final_predictions.py <labels> <final_prediction_file>"
    sys.exit(1)

labels = []
for line in open(sys.argv[1]):
    line = line.strip()
    if line == "":
        continue

    labels.append(int(line))

unique_labels = set(labels)
class_precision = {}
for line in open(sys.argv[2]):
    lexemes = line.strip().split("\t")

    label = int(lexemes[0])
    pattern = int(lexemes[1]) - 1

    if label == labels[pattern]:
        class_precision[label] = class_precision.get(label, 0) + 1

score = 0
for label in class_precision:
    score += class_precision[label] / 100.0
score /= len(unique_labels)

print "MAP= %.02f" % score
