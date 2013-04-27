#!/bin/bash

set -e

if [ $# -ne 3 ]; then
    echo "USAGE: eval_models.sh <model_dir> <wd> <patterns>"
    exit 1
fi

model_dir=$(cd $1 &> /dev/null;pwd;cd - &> /dev/null)
wd=$2
data=$(cd `dirname $3` &> /dev/null; pwd; cd - &> /dev/null)/`basename $3`
pred=/home/fishjord/cse847/liblinear-1.93/predict

stem=`basename $data .txt`

cd $wd

for model in `ls $model_dir/*.model`
do
    echo "Evaluating $1 with model $model"
    time $pred $data $model ${stem}_`basename ${model}`_eval.txt
done
