#!/bin/bash --login

for i in {6..20}
do
    qsub -m abe -l mem=72000mb,walltime=1:00:00 -j oe -v model=$i /mnt/home/fishjord/cse847/effulgent_sakana/scripts/hpcc/train_meanmarginstree.sh
done

