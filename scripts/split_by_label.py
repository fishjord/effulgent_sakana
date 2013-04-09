#!/usr/bin/python

import sys
import random

if len(sys.argv) != 3 and len(sys.argv) != 4:
    print >>sys.stderr, "USAGE: random_sample.py <input_patterns> <labels>"
    sys.exit(1)

patterns = open(sys.argv[1]).read().strip().split("\n")
labels = open(sys.argv[2]).read().strip().split("\n")

pattern_streams = {}
label_streams = {}

for i in range(len(patterns)):
    label = labels[i]
    if label not in pattern_streams:
        pattern_streams[label] = open("class_%s_patterns.txt" % label, "w")
        label_streams[label] = open("class_%s_labels.txt" % label, "w")

    print >>pattern_streams[label], patterns[i]
    print >>label_streams[label], label
