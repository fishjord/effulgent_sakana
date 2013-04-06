#!/usr/bin/python

import sys

def parse_labels(labels_file):
    ret = [-1000] #want it to be 1 based.

    for line in open(labels_file):
        line = line.strip()
        if line != "":
            ret.append(int(line))

    return ret

def mode(l):
    cnts = {}

    for item in l:
        cnts[item] = cnts.get(item, 0) + 1

    best_item = None

    for item in cnts:
        if best_item == None or cnts[best_item] < cnts[item]:
            best_item = item

    return best_item

if len(sys.argv) != 4:
    print >>sys.stderr, "USAGE: analyze_knn.py <knn> <train_labels> <test_labels>"
    sys.exit(1)

train_labels = parse_labels(sys.argv[2])
test_labels = parse_labels(sys.argv[3])

max_k = 100

k_errors = [0 for x in range(max_k)]

test_pattern = 1
for line in open(sys.argv[1]):
    line = line.strip()
    if line == "":
        continue

    lexemes = line.split()
    expected = test_labels[test_pattern]
    neighbors = [train_labels[int(x.split(":")[0])] for x in lexemes]

    sys.stdout.write("%s\t%s" % (test_pattern, expected))

    for k in range(1, max_k):
        pred = mode(neighbors[:k])
        sys.stdout.write("\t%s" % pred)

        if pred != expected:
            k_errors[k] += 1

    sys.stdout.write("\n")

    test_pattern += 1

for i in range(1, max_k):
    print i, float(k_errors[i]) / test_pattern
