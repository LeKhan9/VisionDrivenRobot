# VisionDrivenRobot


This is visual driven Hexapod that operates through a Neural Network running on an Android application. 
The applications takes pictures and processes them, feeds them into a trained neural network and sends
a movement command to the Arduino through Bluetooth. The Arduino then sends a wired signal to a STAMP
microprocessor which dictates the hexapod (robot) movement.

More information can be found here:

https://sites.google.com/a/conncoll.edu/mkhan/neural-network-trained-and-vision-driven-hexapod

There are several portions of this project following consecutive steps:
1. Take picture with Android Phone utilizing an application that uses Camera and Bluetooth API
2. Compress Picture into 9x9 pixel frame 
3. Use the luminosity technique to obtain 1 or 0 for RGB mapping of picture for each pixel at middle row 
4. Obtain overall string for this middle row
5. Feed each pixel or binary value as an input to 2-hidden-layer feed-forward neural network
6. Obtain output of neural network and package into a Bluetooth outgoing string
7. Receive Bluetooth message with Bluetooth Receiver, HC-05
8. Send data through TX and RX pins to Arduino
9. Using this string, send HIGH or LOW values through pins to Stamp
10. With expert system, activate different methods depending on pin states
-->These methods dictate Hexapod movement through servo activation:
