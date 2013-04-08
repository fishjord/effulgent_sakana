function [p, c, D] = pca(X, d)
% Perform PCA on the data set X.
% [p, c] = pca(X)
% Each row of X corresponds to a pattern. So, for the entire 
% Iris data set, X will of size 150 by 4.
% d is the number of features to be extracted.
% For output,
%   p is the direction for projection; each column is one direction
%   c is the mean of X
% So, suppose you have another set of pattern stored in the matrix Y,
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
