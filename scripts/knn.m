#!/usr/local/bin/octave -qf


function [neighborIds neighborDistances] = kNearestNeighbors(dataMatrix, queryMatrix, k)
%--------------------------------------------------------------------------
% Program to find the k - nearest neighbors (kNN) within a set of
% points.
% Distance metric used: Euclidean distance
%
% Usage:
% [neighbors distances] = kNearestNeighbors(dataMatrix,
% queryMatrix, k);
% dataMatrix  (N x D) - N vectors with dimensionality D (within
% which we search for the nearest neighbors)
% queryMatrix (M x D) - M query vectors with dimensionality D
% k           (1 x 1) - Number of nearest neighbors desired
%
% Example:
% a = [1 1; 2 2; 3 2; 4 4; 5 6];
% b = [1 1; 2 1; 6 2];
% [neighbors distances] = kNearestNeighbors(a,b,2);
%
% Output:
% neighbors =
%      1     2
%      1     2
%      4     3
%
% distances =
%          0    1.4142
%     1.0000    1.0000
%     2.8284    3.0000
%--------------------------------------------------------------------------

neighborIds = zeros(size(queryMatrix,1),k);
neighborDistances = neighborIds;

numDataVectors = size(dataMatrix,1);
numQueryVectors = size(queryMatrix,1);
for i=1:numQueryVectors,
    dist = sum((repmat(queryMatrix(i,:),numDataVectors,1)- ...
                dataMatrix).^2,2);
    [sortval sortpos] = sort(dist,'ascend');
    neighborIds(i,:) = sortpos(1:k);
    neighborDistances(i,:) = sqrt(sortval(1:k));
end
end

function error = eval_knn_error(n, labels, t_labels)
max_k = size(n, 2);
error = [];

for k = 1:max_k
    k_n = n(:, 1:k);
    pred = mode(labels(k_n), 2);

    error(end+1) = sum(pred ~= t_labels) / length(t_labels);
end

return;
end

arg_list = argv;

if (length(arg_list) != 4)
    printf("USAGE: knn.m <training> <train_labels> <testing> ...
           <test_labels>\n");
    quit();
end

train = load(arg_list{1});
train_labels = load(arg_list{2});

test = load(arg_list{3});
test_labels = load(arg_list{4});

[neighbors, dists] = kNearestNeighbors(train, test, 100);

neighbors_out = strcat(arg_list{3}, "_neighbors.txt");
dists_out = strcat(arg_list{3}, "_dists.txt");

dlmwrite(neighbors_out, neighbors, '\t');
dlmwrite(dists_out, dists, '\t');

errors = eval_knn_error(neighbors, train_labels, test_labels);

errors_out = strcat(arg_list{3}, "_knn_errors.txt");

dlmwrite(errors_out, errors, '\t');