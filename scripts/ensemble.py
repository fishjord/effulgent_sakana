#!/usr/bin/env python

import argparse
import re
import glob
import random

def read_single_column(fname, t=int):
    ret = []
    for line in open(fname):
        if line.strip() == "":
            continue
        ret.append(t(line))

    return ret

def predict(args):
    if args.labels:
        expected_labels = read_single_column(args.labels)
    else:
        expected_labels = None

    if args.weights:
        expert_weights = read_single_column(args.weights, t=float)
    else:
        expert_weights = None

    lineno = 0
    correct = 0

    header = "#pattern\tprediction\tprediction_weight\tprediction_prob"
    if expected_labels:
        header += "\texpected_label\tcorrect?\texpected_label_weighted_sum"

    print header

    for line in open(args.expert_predictions):
        line = line.strip()
        if line == "":
            continue

        expert_predictions = [int(x) for x in line.split(",")]
        lineno += 1
        if expert_weights == None:
            expert_weights = [1.0] * len(expert_predictions)
        elif len(expert_weights) != len(expert_predictions):
            raise IOError("Mismatched number of expert_weights [%s] and expert_predictions [%s] (line=%s)" % (len(expert_weights), len(expert_predictions), lineno))

        weighted_sums = dict()
        for expert in range(len(expert_predictions)):
            expert_prediction = expert_predictions[expert]
            weighted_sums[expert_prediction] = weighted_sums.get(expert_prediction, 0) + expert_weights[expert]

        best_prediction = []
        for c in weighted_sums:
            if len(best_prediction) == 0 or weighted_sums[c] == weighted_sums[best_prediction[0]]:
                best_prediction.append(c)
            elif weighted_sums[c] > weighted_sums[best_prediction[0]]:
                best_prediction = [c]

        pattern = lineno - 1
        pred = random.choice(best_prediction)
        s = "%s\t%s\t%0.3f\t%0.3f" % (lineno, pred, weighted_sums[pred], weighted_sums[pred] / sum(expert_weights))
        if expected_labels:
            s += "\t%s\t%s\t%0.3f\t%.3f" % (expected_labels[pattern], pred == expected_labels[pattern], weighted_sums.get(expected_labels[pattern], 0), weighted_sums.get(expected_labels[pattern], 0) / sum(expert_weights))
            if pred == expected_labels[pattern]:
                correct += 1
        print s

    if expected_labels:
        print "Accuracy: %s of %s (%0.2f%%)" % (correct, lineno, correct * 100 / float(lineno))

def main():
    parser = argparse.ArgumentParser()

    parser.add_argument("--labels", help="Labels for input examples for computing accuracy")
    parser.add_argument("--weights", help="Read expert weights from file")

    parser.add_argument("expert_predictions")

    args = parser.parse_args()
    predict(args)

if __name__ == "__main__":
    main()
