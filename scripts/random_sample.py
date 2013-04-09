#!/usr/bin/python

import sys
import random

if len(sys.argv) != 3 and len(sys.argv) != 4:
    print >>sys.stderr, "USAGE: random_sample.py <input_patterns> <training_ratio> [labels]"
    sys.exit(1)

patterns = open(sys.argv[1]).read().split("\n")
training_ratio = float(sys.argv[2])
if len(sys.argv) == 4:
    labels = open(sys.argv[3]).read().split("\n")
else:
    labels = None

num_training = int(len(patterns) * training_ratio + .5)

idxes = [x for x in range(len(patterns))]
random.shuffle(idxes)

out = open("training_patterns.txt", "w")
if labels != None:
    labels_out = open("training_labels.txt", "w")

for i in range(num_training):
    pattern = idxes[i]
    print >>out, patterns[pattern]
    if labels != None:
        print >>labels_out, labels[pattern]

out.close()
if labels != None:
    labels_out.close()

out = open("testing_patterns.txt", "w")
if labels != None:
    labels_out = open("testing_labels.txt", "w")

for i in range(num_training, len(patterns)):
    pattern = idxes[i]
    print >>out, patterns[pattern]
    if labels != None:
        print >>labels_out, labels[pattern]

out.close()
if labels != None:
    labels_out.close()
