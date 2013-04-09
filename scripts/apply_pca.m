#!/usr/local/bin/octave -qf

arg_list = argv();

if(length(arg_list) != 3)
    fprintf(stderr(), "USAGE: compute_pca.m <data matrix> <pca_stem> ...
            <target_variance>\n");
    quit();
endif

eigvector_out = strcat(arg_list{2}, ".eigenvectors.txt");
eigvalue_out = strcat(arg_list{2}, ".eigenvalues.txt");
means_out = strcat(arg_list{2}, ".means.txt");
variance_retained = str2num(arg_list{3});

p = load(eigvector_out);
c = load(means_out);
eigvalues = load(eigvalue_out);

data = load(arg_list{1});

eigvalue_sum = sum(eigvalues);
eig_sum = 0

for i = 1:length(eigvalues)
    eig_sum = eig_sum + eigvalues(i);

    if ((eig_sum / eigvalue_sum) > variance_retained)
        printf('%d eigenvalues account for %f%% of the variance\n', ...
               i, sum(eigvalues(1:i)) / eigvalue_sum * 100);
        eig_vectors = p(:, 1:i);
        pca_data = ( (data - repmat(c, size(data,1), 1) ) * eig_vectors ...
                     );
        pca_out = strcat(arg_list{1}, ".pca_", num2str(i), ".txt");
        dlmwrite(pca_out, pca_data, '\t');        
        break;
    endif
end
