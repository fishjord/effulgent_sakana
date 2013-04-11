#!/usr/bin/python

import sys

if len(sys.argv) != 3:
    print >>sys.stderr, "USAGE: convert_and_sample.py <input_patterns> <labels>"
    sys.exit(1)

def write_pattern(pattern, label, out):
    s = "{ "

    for i in range(len(pattern)):
        if pattern[i] == 0:
            continue

        s += "%s %s, " % (i + 1, pattern[i])

    s += " %s %s}" % (len(pattern), label)
    print >>out, s.strip()

def read_labels(fname):
    labels = []
    for line in open(fname):
        line = line.strip()
        if line != "":
            labels.append(int(line))

    return labels

def read_patterns(fname):
    patterns = []
    for line in open(sys.argv[1]):
        lexemes = line.strip().split()
        if len(lexemes) == 0:
            continue

        lexemes = [float(x) for x in lexemes]
        
        if len(patterns) > 0 and len(lexemes) != len(patterns[0]):
            raise IOError("Unexpected number of tokens on line '%s'" % line)
        
        patterns.append(lexemes)

    return patterns
    
patterns, labels = read_patterns(sys.argv[1]), read_labels(sys.argv[2])

for i in range(len(patterns)):
    write_pattern(patterns[i], labels[i], sys.stdout)

