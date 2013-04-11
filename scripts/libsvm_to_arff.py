#!/usr/bin/python

import sys

if len(sys.argv) != 3:
    print >>sys.stderr, "USAGE: convert_and_sample.py <# features> <input_patterns>"
    sys.exit(1)

num_features = int(sys.argv[1])
patterns_file = sys.argv[2]

print "@RELATION %s" % patterns_file
print
for i in range(1, num_features + 1):
    print "@ATTRIBUTE feature_%s NUMERIC" % i

print "@ATTRIBUTE label {%s}" % (",".join([str(x) for x in range(1, 165)]))

print
print "@data"

for line in open(patterns_file):
    line = line.strip()
    if line == "":
        continue

    lexemes = line.split()

    sys.stdout.write("{")
    for lexeme in lexemes[1:]:
        if ":" not in lexeme:
            continue

        vals = lexeme.split(":")
        i = int(vals[0])
        sys.stdout.write("%s %s," % (i - 1, vals[1]))
    sys.stdout.write("%s %s}" % (num_features, lexemes[0]))

