#!/scratch/fishjord/apps/octave-3.6.4/run-octave -qf

function [p, c, D] = pca(X, d)
% Perform PCA on the data set X.
% [p, c] = pca(X)
% Each row of X corresponds to a pattern. So, for the entire
% Iris data set, X will of size 150 by 4.
% d is the number of features to be extracted.
% For output,
%   p is the direction for projection; each column is one direction
%   c is the mean of X
% So, suppose you have another set of pattern stored in the matrix
% Y,
%   where each row in Y corresponds to a pattern.
% You should reduce the dimensionality of Y by:
%
%   yy = ( (Y - repmat(c, size(Y,1), 1) ) * p )';
%
% Written by Martin law lawhiu@cse.msu.edu
% Jan 6 2005


c = sum(X,1) / size(X,1);
X = X - repmat(c, size(X,1), 1);
cov = X' * X;
opt.disp = 0;
if d == -1
    [p, D] = eig(cov);
else
    [p, D] = eigs(cov, d, 'LA', opt);
end

return;
end;

arg_list = argv();

if(length(arg_list) != 2)
    fprintf(stderr(), "USAGE: compute_pca.m <data matrix> <output_stem>\n");
    quit();
endif

data = load(arg_list{1});
[p, c, D] = pca(data, -1);

D = diag(D);

[sorted_eigvalues, idx] = sort(D, 'descend');
sorted_eigs = p(idx, :);

eigvalue_sum = sum(sorted_eigvalues);

printf('#x\teigenvalue\t%% variance\n');
for i = 1:length(sorted_eigvalues)
    printf('%d\t%f\t%f\n', i, sorted_eigvalues(i), sum(sorted_eigvalues(1:i)) / eigvalue_sum);
end

eigvector_out = strcat(arg_list{2}, ".eigenvectors.txt");
eigvalue_out = strcat(arg_list{2}, ".eigenvalues.txt");
means_out = strcat(arg_list{2}, ".means.txt");

dlmwrite(eigvector_out, sorted_eigs, '\t');
dlmwrite(eigvalue_out, sorted_eigvalues, '\t');
dlmwrite(means_out, c, '\t');
