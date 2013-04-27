#!/bin/bash

set -e

wd=/home/fishjord/cse847/full_data/
training_data=/mnt/scratch/fishjord/cse847_full_data/scaled_training_libsvm.txt
train=/mnt/home/fishjord/cse847/liblinear-1.93/train
bootstrap=/mnt/home/fishjord/cse847/effulgent_sakana/scripts/bootstrap.py

bootstrap_out=/mnt/scratch/fishjord/bootstrap_${model}_${s}_${c}.txt

cd $wd
time $bootstrap $training_data > $bootstrap_out
#sleep 5s
time $train -s ${s} -c ${c} $bootstrap_out liblinear_${model}_s${s}_c${c}_full_training.model
#rm $bootstrap_out