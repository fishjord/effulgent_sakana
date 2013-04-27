#!/bin/bash --login

for i in {1..5}
do
    qsub -m abe -l mem=6000mb,walltime=12:00:00 -j oe -v model=$i,s=2,c=1 /mnt/home/fishjord/cse847/full_data/train_libsvm_model.sh
    qsub -m abe -l mem=6000mb,walltime=12:00:00 -j oe -v model=$i,s=4,c=1 /mnt/home/fishjord/cse847/full_data/train_libsvm_model.sh
    qsub -m abe -l mem=6000mb,walltime=12:00:00 -j oe -v model=$i,s=5,c=1 /mnt/home/fishjord/cse847/full_data/train_libsvm_model.sh
    qsub -m abe -l mem=6000mb,walltime=12:00:00 -j oe -v model=$i,s=2,c=5 /mnt/home/fishjord/cse847/full_data/train_libsvm_model.sh
    qsub -m abe -l mem=6000mb,walltime=12:00:00 -j oe -v model=$i,s=4,c=5 /mnt/home/fishjord/cse847/full_data/train_libsvm_model.sh
    qsub -m abe -l mem=6000mb,walltime=12:00:00 -j oe -v model=$i,s=5,c=5 /mnt/home/fishjord/cse847/full_data/train_libsvm_model.sh
done

