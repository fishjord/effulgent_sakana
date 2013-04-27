#!/usr/bin/env python

import sys
import argparse
from utils import read_file, LibSVMPredReader

def predict(args):
    readers = []
    prediction_probs = []
    for fname in args.expert_predictions:
        readers.append(LibSVMPredReader(fname))

    weight = 1.0 / len(readers)
    print >>sys.stderr, "Reading probability files"
    while True:
        probs = None
        for reader in readers:
            pred, prob = reader.next_line()

            if pred == None:
                break

            if prob == None:
                raise IOError("Prediction probabilities expected for every classifier")

            if probs == None:
                probs = [0] * len(prob)
            for i in range(len(prob)):
                probs[i] += prob[i] * weight

        if pred == None:
            break
        prediction_probs.append(probs)

    print >>sys.stderr, "Finding best class labels"
    for c in range(1, len(prediction_probs[0])):
        patterns = []
        print >>sys.stderr, "\tclass %s" % c
        for pattern in range(len(prediction_probs)):
            patterns.append((pattern, prediction_probs[pattern][c]))

        patterns.sort(key=lambda x: x[1], reverse=True)

        for pattern, prob in patterns[:args.num_predict]:
            print "%s\t%s\t%s" % (c, pattern + 1, prob)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--num_predict", type=int, default=100)
    parser.add_argument("expert_predictions", nargs="+")
    args = parser.parse_args()
    predict(args)

if __name__ == "__main__":
    main()
