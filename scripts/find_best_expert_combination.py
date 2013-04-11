#!/usr/bin/env python

import sys
import re
import subprocess
import numpy

def read_file(fname):
    ret = []
    for line in open(fname):
        line = line.strip()
        if line == "":
            continue
        ret.append(line)
    return ret

def write_arff(out, labels, expert_predictions, use_these_experts):
    attr_cv = "{%s}" % (",".join([str(x) for x in range(1, 165)]))

    print >>out, "@RELATION expert_combintorics"
    print >>out

    for i in use_these_experts:
        print >>out, "@ATTRIBUTE expert_%s %s" % (i, attr_cv)

    print >>out, "@ATTRIBUTE label %s" % attr_cv
    print >>out
    print >>out, "@data"
    for i in range(len(labels)):
        s = []
        for expert in use_these_experts:
            s.append(expert_predictions[expert][i])
                    
        s.append(labels[i])

        print ",".join(s)

def cross_validate(labels, expert_predictions, use_these_experts):
    out = open("tmp.arff")
    write_arff(out, labels, expert_predictions, use_these_experts)
    out.flush()
    out.close()

    cmd = ["waffles_learn", "crossvalidate", "-reps", "1", "-folds", "2", "tmp.arff", "decisiontree"]
    waffles_out = subprocess.check_output(cmd).split("\n")

    #Attr: 100, Mean predictive accuracy: 0.24896, Deviation: 0.0015839191898581
    acc = float(waffles_out.split()[5][:-1])
    return acc

if len(sys.argv) < 3:
    print >>sys.stderr, "combine_experts.py <labels> <expert_prediction_files>"
    sys.exit(1)

regex = re.compile("[^0-9]+")
stem = sys.argv[1]
labels = read_file(sys.argv[2])

expert_predictions = {}
expert_labels = {}
for f in sys.argv[2:]:
    i = int(regex.sub("", f))
    expert_prediction[i] = read_file(f)
    expert_labels[i] = os.path.split(f)[-1]

experts = sorted(expert_predictions.keys())
print "Experts: %s" % experts
print "r\tbest_experts\tbest_error\taverage_error"
for r in range(1, len(experts)):
    for use_these_experts in itertools.combinations(experts, r):
        errors.append((use_these_experts, cross_validate(labels, expert_predictions, use_these_experts)))

    errors.sort(lambda x: x[1])
    average_error = numpy.average([x[1] for x in errors])
    best_experts = []
    for e in errors[-1][0]:
        best_experts.append(expert_labels[e])

    print "%s\t%s\t%s\t%0.2f" % (r, ",".join(best_experts), errors[-1][1], average_error)
