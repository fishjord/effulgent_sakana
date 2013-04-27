#!/usr/bin/env python

import sys
import re
import glob
import argparse
import random

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--labels", dest="label_file", help="Label file")
    parser.add_argument("--max-experts", dest="max_experts", help="Max expert per classifier", type=int)
    parser.add_argument("--csv", action="store_true", help="Export to csv and names instead of arff")
    parser.add_argument("expert_stem", nargs="+")

    args = parser.parse_args()

    if args.label_file:
        label_stream = open(args.label_file)
    else:
        label_stream = None

    regex = re.compile("[^0-9]+")

    attr_cv = "{%s}" % (",".join([str(x) for x in range(1, 165)]))

    expert_prediction_streams = {}
    for stem in args.expert_stem:
        expert_files = {}
        for f in glob.glob(stem + "*.txt"):
            i = stem + regex.sub("", f)
            expert_files[i] = f

        expert_keys = sorted(expert_files.keys())
        if args.max_experts and len(expert_keys) > args.max_experts:
            random.shuffle(expert_keys)
            expert_keys = expert_keys[:args.max_experts]
        
        for expert_key in expert_keys:
            expert_prediction_streams[expert_key] = open(expert_files[expert_key])

    sorted_expert_ids = sorted(expert_prediction_streams.keys())

    if not args.csv:
        print >>sys.stderr, sorted_expert_ids
        print "@RELATION combined_experts"
        print

    for i in sorted_expert_ids:
        if args.csv:
            print >>sys.stderr, "%s,integer,discrete" % i
        else:
            print "@ATTRIBUTE expert_%s %s" % (i, attr_cv)
            

    if label_stream != None:
        if args.csv:
            print >>sys.stderr, "label,integer,class"
        else:
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

        if label_stream != None:
            l = label_stream.readline()
            if l == "" and not last:
                raise IOError("Label file had fewer labels in it than predictions")
            else:
                s.append(l.strip())

        if last:
            break

        print ",".join(s)
    

if __name__ == "__main__":
    main()
