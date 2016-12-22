#!/bin/bash

#Using this script inception's final layer can be retrained for a new category
#1st command will build the retrainer 
bazel build -c opt --copt=-mavx tensorflow/examples/image_retraining:retrain
#When the first command is completed retrainer can be run using this command.
##Before run this, set up the image folder path to Plane
bazel-bin/tensorflow/examples/image_retraining/retrain --image_dir ~/Plane
