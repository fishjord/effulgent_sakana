#!/usr/bin/env python

import sys
import random
import subprocess

train = "/home/fishjord/cse847/liblinear-1.93/train"
pred = "/home/fishjord/cse847/liblinear-1.93/predict"

def liblinear(training_file, model_out_file, additional_liblinear_opts):
    cmd = [train]
    cmd.extend(additional_liblinear_opts)
    cmd.extend([training_file, model_out_file])

    subprocess.check_call(cmd, stdout=open(model_out_file + "_train.log", "w"))

def test_liblinear(model_file, test_file):
    cmd = [pred, test_file, model_file, model_file + "_eval.log"]
    subprocess.check_call(cmd)

def main(learner, training_file, model_out_file):
    print >>sys.stderr, "Training a %s learner from %s" % (learner, training_file)
    if learner == "svm":
        liblinear(training_file, model_out_file, ["-s", "2"])
        test_liblinear(model_out_file, training_file)
    elif learner == "logistic":
        liblinear(training_file, model_out_file, ["-s", "7"])
        test_liblinear(model_out_file, training_file)
    else:
        raise Exception("Unknown learner %s" % learner)

if __name__ == "__main__":
    if len(sys.argv) != 4 or sys.argv[1] not in ["svm, logistic", "random"]:
        print >>sys.stderr, "USAGE: train_wl.py <svm,logistic,decisiontree,random> <training_file> <model_out>"
        sys.exit(1)

    if sys.argv[1] == "random":
        learner = random.choice(["svm", "logistic"])
    else:
        learner = sys.argv[1]
    main(learner, sys.argv[2], sys.argv[3])
    
