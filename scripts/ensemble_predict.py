#!/usr/bin/env python

import sys
import argparse
from utils import read_file, LibSVMPredReader

def predict(args):
    if args.labels:
        expected_labels = read_file(args.labels)
    else:
        expected_labels = None

    lineno = 0
    correct = 0

    header = "#pattern\tprediction\t\tprediction_prob"
    if expected_labels:
        header += "\texpected_label\tcorrect?\texpected_label_prob"

    print header

    readers = []
    for fname in args.expert_predictions:
        readers.append(LibSVMPredReader(fname))

    weight = 1.0 / len(readers)
    while True:
        probs = dict()
        for reader in readers:
            pred, prob = reader.next_line()
            if pred == None:
                break

            if prob == None:
                probs[pred] = probs.get(pred, 0) + 1
            else:
                for i in range(len(prob)):
                    probs[i] = probs.get(i, 0) + prob[i] * weight

        if pred == None:
            break

        final_pred = None
        for label in probs.keys():
            if final_pred == None or probs[label] > probs[final_pred]:
                final_pred = label

        pattern = lineno
        s = "%s\t%s\t%0.3f" % (pattern, final_pred, probs[final_pred])
        if expected_labels:
            s += "\t%s\t%s\t%0.3f" % (expected_labels[pattern], pred == expected_labels[pattern], probs.get(expected_labels[pattern], 0))
            if pred == expected_labels[pattern]:
                correct += 1
        print s
        lineno += 1

    if expected_labels:
        print "Accuracy: %s of %s (%0.2f%%)" % (correct, lineno, correct * 100 / float(lineno))

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--labels", help="Labels for input examples for computing accuracy")

    parser.add_argument("expert_predictions", nargs="+")

    args = parser.parse_args()
    predict(args)

if __name__ == "__main__":
    main()
