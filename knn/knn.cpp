#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <stdlib.h>
#include <math.h>

struct neighbor {
  int train_pattern;
  float dist;
};

bool scan_file(const char* fname, int* num_lines_out, int* num_features_out) {
  std::ifstream in(fname);
  std::string line;
  *(num_lines_out) = 0;
  int features;
  float tmp;

  while(std::getline(in, line)) {
    if(line == "") {
      continue;
    }

    std::istringstream iss(line);
    features = 0;
    while(iss >> tmp) {
      features++;
    }

    if(features == 0) {
      break;
    }

    if(*(num_lines_out) == 0) {
      *(num_features_out) = features;
    } else if(features != *(num_features_out)) {
      return false;
    }
    *(num_lines_out) += 1;
  }

  return true;
}

bool read_data(const char* fname, const int* num_patterns, const int* num_features, float* buf) {
  std::ifstream in(fname);
  std::string line;

  for(int line_num = 0;line_num < *num_patterns;line_num++) {  
    if(!std::getline(in, line)) {
      return false;
    }

    if(line == "") {
      continue;
    }

    std::istringstream iss(line);
    for(int feature = 0;feature < *num_features;feature++) {
      if(!(iss >> buf[line_num * *(num_features) + feature])) {
	return false;
      }
    }
  }

  return true;
}

void l2_dist(float* pattern1, float* pattern2, int *num_features, neighbor* neighbor_out) {
  neighbor_out->dist = 0;
  float tmp;

  for(int feature = 0;feature < *(num_features);feature++) {
    neighbor_out->dist += (pattern1[feature] - pattern2[feature]) * (pattern1[feature] - pattern2[feature]);
  }
  //neighbor_out->dist = sqrt(neighbor_out->dist);
}

void insertion_sort(neighbor* neighbors, int idx) {
  neighbor tmp;
  while(idx > 0 && neighbors[idx].dist < neighbors[idx - 1].dist) {
    tmp = neighbors[idx];
    neighbors[idx] = neighbors[idx - 1];
    neighbors[idx - 1] = tmp;
    idx--;
  }
}

int main(int argc, char** argv) {
  if(argc != 3 && argc != 4) {
    std::cerr << "USAGE: knn <k> <train_file> [test_patterns]" << std::endl;
    return 1;
  }

  int num_train_patterns, num_train_features;
  int num_eval_patterns, num_eval_features;
  const char* train_file = argv[2];
  const char* eval_file;
  float* train_data;
  float* eval_data;

  if(!scan_file(train_file, &num_train_patterns, &num_train_features)) {
    std::cerr << "Failed to scan information from " << train_file << ", failed on line " << num_train_patterns << std::endl;
    return 2;
  }
  std::cerr << "Train file " << train_file << " contains " << num_train_patterns << " patterns containing " << num_train_features << " features each" << std::endl;

  train_data = (float*)new float[num_train_patterns * num_train_features];
  read_data(train_file, &num_train_patterns, &num_train_features, train_data);

  if(argc == 3) {
    eval_file = train_file;
    num_eval_patterns = num_train_patterns;
    num_eval_features = num_train_features;
    eval_data = train_data;
  } else {
    eval_file = argv[3];
    if(!scan_file(eval_file, &num_eval_patterns, &num_eval_features)) {
      std::cerr << "Failed to scan information from " << eval_file << ", failed on line " << num_eval_patterns << std::endl;
      return 3;
    }
    std::cerr << "Evalution file " << eval_file << " contains " << num_eval_patterns << " patterns containing " << num_eval_features << " features each" << std::endl;

    eval_data = (float*)new float[num_eval_patterns * num_eval_features];
    read_data(eval_file, &num_eval_patterns, &num_eval_features, eval_data);
  }

  if(num_eval_features != num_train_features) {
    std::cerr << "Evaluation file has " << num_eval_features << " features but the training file has " << num_train_features << std::endl;
    return 4;
  }

  float* eval;
  const int k = std::min(atoi(argv[1]), num_train_patterns);
  neighbor* neighbors = new neighbor[k + 1];
  int curr_k;
  const char* busy = "-\\|/";

  std::cerr << "Computing " << k << " nearest neighbors of " << num_eval_patterns << " evaluation patterns to " << num_train_patterns << " training patterns containing " << num_train_features << " features" << std::endl;

  for(int eval_pattern = 0;eval_pattern < num_eval_patterns;eval_pattern++) {
    eval = eval_data + (eval_pattern * num_eval_features);
    curr_k = 0;
    std::cerr << "Evaluation pattern " << eval_pattern + 1 << "...";
    for(int training_pattern = 0;training_pattern < num_train_patterns;training_pattern++) {
      std::cerr << busy[training_pattern % 4] << "\b";
      neighbors[curr_k].train_pattern = training_pattern + 1;
      l2_dist(eval, train_data + (training_pattern * num_train_features), &num_train_features, &neighbors[curr_k]);
      insertion_sort(neighbors, curr_k);
      if(curr_k < k) {
	curr_k++;
      }
    }

    for(int index = 0;index < k;index++) {
      std::cout << neighbors[index].train_pattern << ":" << neighbors[index].dist << " ";
    }
    std::cout << std::endl;
    std::cerr << "Done." << std::endl;
  }


  delete neighbors;
  if(train_data != eval_data) {
    delete eval_data;
  }

  delete train_data;
  return 0;
}
