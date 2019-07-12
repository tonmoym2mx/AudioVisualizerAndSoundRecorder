# AudioVisualizerAndSoundRecorder

![GIF-190712_150026_001 (1)](https://user-images.githubusercontent.com/41751781/61117284-fcdfb300-a4b7-11e9-907e-ddb58b37f270.gif)

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
    ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
 
```gradle
dependencies {
    implementation 'com.github.tonmoyv2mx:AudioVisualizerAndSoundRecorder:1.2'
}
```
Put permissions in Android manifest

```xml
 <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
Add this in your layout:

```xml
<com.tonmoym2mx.audiovisualizer.AudioVisualizer
      android:id="@+id/audioVisualizer"
      android:layout_width="match_parent"
      android:layout_height="300dp"
     />
```
Initial Audio Visualizer
```java
AudioVisualizer audioVisualizer = findViewById(R.id.audioVisualizer);
```
Initial Audio Recorder
```java

String filePath = getExternalCacheDir().getAbsolutePath()+"/audio.mp3";
  
AudioRecorder audioRecorder = new AudioRecorder(MainActivity.this,filePath); 
  
```
Start Record Audio 
```java

if(!audioRecorder.isRecord){ //isRecord is boolean , for check recoder is recoding are not 
  audioRecorder.startRecord();
}
  
```
Stop Record Audio 
```java

if(audioRecorder.isRecord){
   audioRecorder.stopRecord();
}
   
```
Set Visualizer Amplitud 
```java

audioRecorder.setMaxAmplitudeListener(new MaxAmplitudeListener() {

     @Override
     public void getMaxAmplitude(int amplitude) {
     
         audioVisualizer.addAmplitude(amplitude);
         
     }
            
},50);
        
```

Set Timer Update Listener
```java

 audioRecorder.setTimerUpdateListener(new TimerUpdateListener() {
       @Override
       public void onTimeChange(long time) {
       
           String d = new SimpleDateFormat("mm:ss").format(time);
           textView.setText(d);
           
       }
});
        
```

Audio Player 

<img src="https://user-images.githubusercontent.com/41751781/61119347-6f529200-a4bc-11e9-9e9b-a41bec21a867.jpg" width="200">


```java
AudioPlayerDialog playerDialog = new AudioPlayerDialog(MainActivity.this, filePath);

playerDialog.show();

```

   
  
