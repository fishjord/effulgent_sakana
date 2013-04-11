#!/usr/bin/python

import sys

if len(sys.argv) != 2 and len(sys.argv) != 3:
    print >>sys.stderr, "USAGE: convert_and_sample.py <input_patterns> [labels]"
    sys.exit(1)

def write_pattern(pattern, label, out):
    out.write(label)

    for i in range(len(pattern)):
        if pattern[i] == "0":
            continue

        out.write(" %s:%s" % (i + 1, pattern[i]))

    out.write("\n")

def read_patterns(fname, label_file):
    num_features = -1
    data_stream = open(fname)
    if label_file != None:
        label_stream = open(label_file)
    else:
        label_stream = None

    while True:
        line = data_stream.readline()
        if label_stream == None:
            if line == "":
                label = ""
            else:
                label = "-2"
        else:
            label = label_stream.readline()

        if line == "" or label == "":
            if line != "" or label != "":
                raise IOError("No the same number of lines in data and labels file")
            break
        
        lexemes = line.strip().split()
        if len(lexemes) == 0:
            continue

        if len(lexemes) != num_features:
            if num_features == -1:
                num_features = len(lexemes)
            else:
                raise IOError("Unexpected number of tokens on line '%s'" % line)
        
        yield lexemes, label.strip()

if len(sys.argv) == 2:
    label_file = None
else:
    label_file = sys.argv[2]

for pattern, label in read_patterns(sys.argv[1], label_file):
    write_pattern(pattern, label, sys.stdout)
