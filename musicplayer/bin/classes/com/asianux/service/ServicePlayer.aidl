 package com.asianux.service;
  

 interface ServicePlayer{
     void play();
     void pause();
     void stop();
     void previous();
     void next();
     void playMode(int mode);
     int getDuration();
     int getCurrentTime();
     int getPosition();
     int getAudioSessionId();
     void setCurrent(int cur);
     boolean isPlaying();
     String getPlayingUrl();
     String getPlayingInfo();
     List<String > getMp3info();
 }
