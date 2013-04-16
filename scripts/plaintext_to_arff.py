#!/usr/bin/python

import sys

if len(sys.argv) != 3:
    print >>sys.stderr, "USAGE: convert_and_sample.py <input_patterns> <labels>"
    sys.exit(1)

def write_pattern(pattern, label, out):
    print >>out, "%s,%s" % (pattern, label)

def read_labels(fname):
    labels = []
    for line in open(fname):
        line = line.strip()
        if line != "":
            labels.append(int(line))

    return labels

def read_patterns(fname):
    for line in open(fname):
        yield ",".join(line.strip().split())

labels = read_labels(sys.argv[2])
header_written = False
j = 0
for pattern in read_patterns(sys.argv[1]):
    if not header_written:
        header_written = True
        print "@RELATION %s" % sys.argv[1]
        print
        for i in range(len(pattern.split(","))):
            print "@ATTRIBUTE feature_%s NUMERIC" % i

        print
        print "@ATTRIBUTE label {%s}" % (",".join([str(i) for i in sorted(set(labels))]))
        print
        print "@data"

    print "%s,%s" % (pattern, labels[j])
    j += 1
