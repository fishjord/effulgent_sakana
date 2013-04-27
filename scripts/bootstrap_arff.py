#!/usr/bin/env python

import sys
import random

if len(sys.argv) != 2:
    print "USAGE: bootstrap.py <input_file>"
    sys.exit(1)

print >>sys.stderr, "Scanning file to count patterns..."
patterns = 0
stream = open(sys.argv[1])

while True:
    line = stream.readline()
    if line == "@data\n" or line == "":
        break

if line == "":
    print >>sys.stderr, "Failed to find arff header"
    sys.exit(1)

header_end = stream.tell()
print >>sys.stderr, "Header located, data offset=%s" % header_end

while True:
    line = stream.readline()
    if line == "":
        break

    if line == "\n":
        print >>sys.stderr, "WARNING: line near %s is blank" % patterns
        continue

    patterns += 1
print >>sys.stderr, "Done, %s patterns found" % patterns

print >>sys.stderr, "Bootstrapping..."
selected = set()
for i in range(patterns):
    selected.add(random.randrange(patterns))

print >>sys.stderr, "Done, selected %s of %s (%.2f%%) of input patterns in bootstrapping" % (len(selected), patterns, len(selected) * 100 / float(patterns))
print >>sys.stderr, "Writing selected patterns..."

stream.seek(0)
sys.stdout.write(stream.read(header_end))
i = 0
while True:
    line = stream.readline()
    if line == "":
        break

    if line == "\n":
        continue

    if i in selected:
        sys.stdout.write(line)
    i += 1

sys.stdout.flush()
sys.stdout.close()
