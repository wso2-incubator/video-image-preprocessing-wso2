#Training Cascades.

Cascade files in OpenCV is a way that we can save a pre trained model where we can use for object detection purposes. There are three types of features can be considered when training a cascade classifier. They are, Haar-like features , Histogram of Oriented Gradients(HOG) and Local Binary Pattern(LBP) features.

OpenCV provides the facility to train custom cascades to detect object. This project is developed to train a cascade to detect aircrafts using opencv traincascade method.

####Prerequisites
OpenCV

bin will consists with a .pl file which will help to create multiple positive files with using few positive files and more negative files.

tools folder consists with a .py file which will help to create one vector file by combining several vector files.

###Instructions.

<b>1.Download the Training_Cascade folder</b>

<b>2.Create a list of positive images.</b>
 
```
  $ find ./positive_images -iname "*.jpg" > positives.txt
```
<b>3.Create a list of negative images</b>

```
$ find ./negative_images -iname "*.jpg" > negatives.txt
```
<b>4.Create positive samples using opencv_createsamples method.</b>
```
$ mkdir samples   
$ perl bin/createsamples.pl positives.txt negatives.txt samples 1500 "opencv_createsamples -bgcolor 0 -bgthresh 0 -maxxangle 1.1 -maxyangle 1.1 maxzangle 0.5 -maxidev 40 -w 80 -h 40"
```
In this code number of samples that needed to generate can be changed to desired number.
Width and height of a sample can be feed using -w and -h 
    
Delete all the empty vector files if their is any. 

<b>5.Merge the created sample vec files into one vec file</b>

```
$ python ./tools/mergevec.py -v samples/ -o samples.vec
```
<b>6.Train the classifier using opencv_traincascade.</b>

``` 
$ mkdir classifier
$ opencv_traincascade -data classifier -vec samples.vec -bg negatives.txt -numStages 20 -minHitRate 0.999 -maxFalseAlarmRate 0.5 -numPos 1000 -numNeg 600 -w 80 -h 40 -mode ALL -precalcValBufSize 1024 -precalcIdxBufSize 1024
```
Number of negative images and positive images can be changed by changing the values in -numNeg and -numPos.

Number of stages that the classifier should train can be set by using -numStages

Hights and width of training data set can be set by using -h and -w tags.

Feature type can be set by using -featureType

There are so many other parameters that can be changed. 
For more 
http://docs.opencv.org/2.4/doc/user_guide/ug_traincascade.html
