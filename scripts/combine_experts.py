#!/usr/bin/env python

import sys
import re
import glob

if len(sys.argv) != 2 and len(sys.argv) != 3:
    print >>sys.stderr, "combine_experts.py <expert_prediction_stems> [labels]"
    sys.exit(1)

regex = re.compile("[^0-9]+")
stem = sys.argv[1]
if len(sys.argv) == 3:
    label_stream = open(sys.argv[2])
else:
    label_stream = None

attr_cv = "{%s}" % (",".join([str(x) for x in range(1, 165)]))

print "@RELATION %s" % stem
print

expert_prediction_streams = {}
for f in glob.glob(stem + "*.txt"):
    i = int(regex.sub("", f))
    expert_prediction_streams[i] = open(f)

sorted_expert_ids = sorted(expert_prediction_streams.keys())
print >>sys.stderr, "Sorted expert ids: %s" % sorted_expert_ids

for i in sorted_expert_ids:
    print "@ATTRIBUTE expert_%s %s" % (i, attr_cv)

print "@ATTRIBUTE label %s" % attr_cv
print
print "@data"

last = False
while True:
    s = []
    for i in sorted_expert_ids:
        l = expert_prediction_streams[i].readline()
        if l == "":
            if not last and len(s) != 0:
                raise IOError("Not all the files had the same number of predictions in them...")
            else:
                last = True
        else:
            s.append(l.strip())

    if label_stream == None:
        s.append("-2")
    else:
        l = label_stream.readline()
        if l == "" and not last:
            raise IOError("Label file had more labels in it than predictions")
        else:
            s.append(l.strip())

    if last:
        break

    print ",".join(s)
    
