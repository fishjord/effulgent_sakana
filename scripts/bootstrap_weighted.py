#!/usr/bin/env python

import sys
import random
from utils import read_file

def bisecting_search(culm_prob, target, start, end):
    if end <= start:
        return start

    mid = int((end + start) / 2.0 + .5)
    #print >>sys.stderr, "start: %s, end: %s, mid: %s, val: %s, target: %s" % (start, end, mid, culm_prob[mid], target)
    if culm_prob[mid] < target:
        if culm_prob[mid + 1] > target:
            return mid
        else:
            return bisecting_search(culm_prob, target, mid + 1, end)
    else:
        return bisecting_search(culm_prob, target, start, mid - 1)

if len(sys.argv) != 3:
    print "USAGE: bootstrap.py <input_file> <labels>"
    sys.exit(1)

print >>sys.stderr, "Scanning file to count patterns..."
patterns = 0
label_prob = dict()
labels = read_file(sys.argv[2])
for line in open(sys.argv[1]):
    if line == "\n":
        print >>sys.stderr, "WARNING: line near %s is blank" % patterns
        continue

    label = labels[patterns]
    label_prob[label] = label_prob.get(label, 0) + 1

    patterns += 1

target = 1.0 / len(label_prob)
print >>sys.stderr, "Target class probability: %0.2f" % target
print >>sys.stderr, "label\tclass_counts\tper_instance_probability"

for label in sorted(label_prob.keys()):
    class_prob = target / label_prob[label]
    print >>sys.stderr, "%s\t%s\t%s" % (label, label_prob[label], class_prob)
    label_prob[label] = class_prob

print >>sys.stderr, "Done, %s patterns found" % patterns

culm_prob = []
p = 0
for i in range(patterns):
    p += label_prob[labels[i]]
    culm_prob.append(p)

print >>sys.stderr, culm_prob[-1]

print >>sys.stderr, "Bootstrapping..."
selected = set()
for i in range(patterns):
    n = random.random()
    j = bisecting_search(culm_prob, n, 0, patterns)
    #print >>sys.stderr, j
    selected.add(j)

print >>sys.stderr, "Done, selected %s of %s (%.2f%%) of input patterns in bootstrapping" % (len(selected), patterns, len(selected) * 100 / float(patterns))
print >>sys.stderr, "Writing selected patterns..."

i = 0
classes_out = dict()
for line in open(sys.argv[1]):
    if line == "\n":
        continue

    if i in selected:
        classes_out[labels[i]] = classes_out.get(labels[i], 0) + 1
        sys.stdout.write(line)
    i += 1

print >>sys.stderr, "class_label\tcount\tpct"
for label in sorted(label_prob.keys()):
    print >>sys.stderr, "%s\t%s\t%.2f" % (label, classes_out.get(label, 0), classes_out.get(label, 0) * 100.0 / len(selected))

sys.stdout.flush()
sys.stdout.close()
