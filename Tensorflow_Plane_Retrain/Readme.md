#Plane Detection Using Tensorflow

Tensorflow is a convolutional neural network base solution for object detection.

This dataset and script can be used to retrain a model to detect air-crafts.

####Prerequisite
Build Tensorflow from source.

###Build Tensorflow
To build Tensorflow from the source, please refer to this [link](https://www.tensorflow.org/get_started/os_setup#installing_from_sources).

###Step 1
Then create a folder in the tensrflow root named as **Plane** and place the plane and human folders inside. 

###Step 2
Copy retrain.sh file inside the tensorflow root.

###Step 3
Go to the retrain.sh files properties and allow it to execute as a program.

retrain.sh will create the retrain model and retrain the inception's final layer to detect planes.
