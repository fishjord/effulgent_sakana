#!/bin/bash

set -e

wd=/home/fishjord/cse847/full_data/
training_data=/mnt/scratch/fishjord/cse847_full_data/training_with_labels.csv
yadt=/home/fishjord/cse847/YaDT/bin/dTcmd64
bootstrap=/mnt/home/fishjord/cse847/effulgent_sakana/scripts/bootstrap.py

bootstrap_out=/mnt/scratch/fishjord/bootstrap_${model}.txt

cd $wd
time $bootstrap $training_data > ${bootstrap_out}
sleep 5s
time $yadt -c4.5 -fm /home/fishjord/cse847/yadt_names.txt -fd ${bootstrap_out} -tb decisiontree_${model}.waffles
rm $bootstrap_out