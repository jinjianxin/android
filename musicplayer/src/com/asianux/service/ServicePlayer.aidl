 package com.asianux.service;
 
 interface ServicePlayer{
     void play();
     void pause();
     void stop();
     int getDuration();
     int getCurrentTime();
     void setCurrent(int cur);
     boolean isPlaying();
 }
