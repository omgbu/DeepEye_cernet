# DeepEye
一个简单的基于TCP的图片传输程序，实现了图片流的接收和发送。

Prerequisites
--
* JSON library for C
* OpenCV 2.X or OpenCV 3.X
* Darknet
--
Darknet is an open source neural network framework written in C and CUDA. It is fast, easy to install, and supports CPU and GPU computation.

For more information see the [Darknet project website](http://pjreddie.com/darknet).

For questions or issues please use the [Google Group](https://groups.google.com/forum/#!forum/darknet).

Complie Instruction
--
    gcc qbr.c -o qbr -lpthread -ljson
    gcc qbr_s.c -o qbr_s -lpthread -ljson

Run
--
    ./qbr


