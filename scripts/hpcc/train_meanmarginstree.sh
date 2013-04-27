#!/bin/bash

set -e

wd=/home/fishjord/cse847/full_data/
training_data=/mnt/scratch/fishjord/cse847_full_data/training.arff
waffles=/home/fishjord/cse847/waffles/bin/waffles_learn
bootstrap=/mnt/home/fishjord/cse847/effulgent_sakana/scripts/bootstrap_arff.py
libsvm2arff=/mnt/home/fishjord/cse847/effulgent_sakana/scripts/plaintext_to_arff.py

bootstrap_out=/mnt/scratch/fishjord/bootstrap_${model}.arff

cd $wd
time $bootstrap $training_data > ${bootstrap_out}
time $waffles train $bootstrap_out meanmarginstree > meanmarginstree_${model}.waffles
rm $bootstrap_out