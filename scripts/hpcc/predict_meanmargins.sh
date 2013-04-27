#!/bin/bash

cd /mnt/scratch/fishjord/new_meanmargin_models
for f in `ls *.waffles`
do 
    echo $f
    time /mnt/home/fishjord/cse847/waffles/bin/waffles_learn predict $f /mnt/scratch/fishjord/cse847_full_data/training.arff > t.arff;/mnt/home/fishjord/cse847/waffles/bin/waffles_transform keeponlycolumns t.arff '*0' > /mnt/home/fishjord/cse847/full_data/training_predictions_`basename ${f} .waffles`_pred.txt
done

for f in `ls *_pred.txt`
do 
    tail -n '+6' $f > t
    mv t $f
done