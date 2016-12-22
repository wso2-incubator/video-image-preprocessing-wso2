#Video and Image Processing Support in WSO2 Platform

##OpenCV
OpenCV is the core library which was used in all these projects. So this tutorial will guide you through the installation process
of OpenCV 2.4.13 in to a ubuntu machine. For further details please refer to following link.
###Install  OpenCV 2.4.13
####Prerequisites 
- Cmake / cmake-gui.
- Python-dev.
- Java (JAVA_HOME and java PATH should be set up). 

**1.Install cmake-gui, python-dev, and other prerequisites.**
```
$ sudo apt-get install build-essential python-dev cmake-gui 
```
**2.Clone OpenCV from github.**
```
$ git clone https://github.com/opencv/opencv.git
```
**3.After the clone is completed move into the folder and create a new folder named build.**
```
$ cd opencv
$ mkdir build
```
**4.Build the installation using cmake.**

```
$ cmake-gui 
```
 
In the gui, select the cloned opencv folder as the source path.

Select the created build folder as the build path.

Check the "Grouped" checkbox.

Then click the "Configure" button.

After configuration finished configuration done notification will appear in cmake-gui. 

To build OpenCV with multi thread support tick the **BUILD_TBB **box from the BUILD drop down list and from “WITH” drop down list, select **WITH_TBB**. Keep other things unchanged.

Then click the generate button.
After generating complete close the cmake-gui.

**5.Move into the build folder and run following commands.**
```
$ cd build
$ make -j8
$ sudo make install
```
This will install the OpenCV in to the machine.

###[To train a cascade using OpenCV](https://github.com/wso2-incubator/video-image-preprocessing-wso2/tree/master/Training_Cascade#training-cascades)

##Tensorflow
###[Plane Detection Using Tensorflow](https://github.com/wso2-incubator/video-image-preprocessing-wso2/tree/master/Tensorflow_Plane_Retrain#plane-detection-using-tensorflow)
