#!/usr/bin/python

import sys
from utils import read_file

if len(sys.argv) < 3:
    print >>sys.stderr, "USAGE: compute_accuracy.py <labels> <predictions file>..."
    sys.exit(1)

labels = read_file(sys.argv[1])
unique_labels = sorted(set(labels))
num_patterns = float(len(labels))

label_counts = {}
for label in labels:
    label_counts[label] = label_counts.get(label, 0) + 1

accs = []
error_profiles = []
pred_files = sys.argv[2:]
for pred_file in pred_files:
    error_profile = {}
    errors = 0
    pred_labels = read_file(pred_file)

    if len(pred_labels) != len(labels):
        raise IOError("File %s doesn't have the same number of labels as the label file" % pred_file)

    for i in range(len(labels)):
        expected = labels[i]
        pred = pred_labels[i]

        if pred != expected:
            error_profile[expected] = error_profile.get(expected, 0) + 1
            errors += 1

    error_profiles.append(error_profile)

    accs.append(errors / num_patterns)

sys.stdout.write("label")
for i in range(len(pred_files)):
    sys.stdout.write("\t")
    sys.stdout.write(pred_files[i])

sys.stdout.write("\naccuracy")
for i in range(len(pred_files)):
    sys.stdout.write("\t%0.2f%%" % (100 - accs[i] * 100))

for label in unique_labels:
    sys.stdout.write("\n%s" % label)
    for i in range(len(pred_files)):
        sys.stdout.write("\t%.02f%%" % (error_profiles[i].get(label, 0) * 100 / label_counts[label]))

sys.stdout.write("\n")
        
