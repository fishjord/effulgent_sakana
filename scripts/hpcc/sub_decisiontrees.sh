#!/bin/bash --login

for i in {1..5}
do
    qsub -m abe -l mem=4000mb,walltime=8:00:00 -j oe -v model=$i /mnt/home/fishjord/cse847/full_data/train_decisiontree.sh
done

