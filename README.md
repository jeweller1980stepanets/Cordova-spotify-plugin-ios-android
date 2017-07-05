[![N|Solid](http://procoders.tech/art/powered.png)](http://procoders.tech/)

# Cordova Spotify Plugin
This plugin included all main functions of audio player for Spotify servise :smirk:

## Installation
> To communicate with Spotify you need to register your application’s bundle id section in the [Developer Portal](http://developers.deezer.com/sdk/ios). This value is used to authenticate your application against Spotify client.



You may install latest version from master
```sh
cordova plugin add https://github.com/jeweller1980stepanets/Cordova-spotify-plugin-ios-android
```
### Removing the Plugin from project
```sh
cordova plugin rm cordova.plugin.spotify
```
## Supported Platforms
> - Android
> - iOS

### Platform specific
:warning: for Android platform not implemented method `setVolume()` and `event onVolumeChanged()`
It will be fixed when this methods will be in Spotify SDK

# Using the plugin
> **You must have premium account from Deezer servise for playing music** :exclamation:

After device is ready you must defined the main variable:
```javascript
var Spotify = window.cordova.plugins.SpotifyPlugin;
```
:thumbsup: *After this you may use all method in your code.*

## Methods
All methods returning promises, but you can also use standard callback functions.

```javascript
Spotify.login(appId, redirectURL, mode);
```
> - *appId* - your application id in Spotify
> - *redirectURL* - White-listed addresses to redirect to after authentication success OR failure 
> - *mode* - The mode of debugging, if you use Xcode emulator its value should be "debug" else empty string


```javascript
Spotify.auth(token,clientId);
```
> - token - spotify access token;
> - clientId - your application id in Spotify.

```javascript
Spotify.play(value);
```
> - *value* - track id or album id or playlist id

**Exemple:**
> Spotify.play("spotify:track:3qRNQHagYiiDLdWMSOkPGG");
> Spotify.play("spotify:album:75Sgdm3seM5KXkEd46vaDb");
> Spotify.play("spotify:user:spotify:playlist:2yLXxKhhziG2xzy7eyD4TD");


```javascript
Spotify.pause();
Spotify.next();
Spotify.prev();
Spotify.logout();
```
```javascript
Spotify.seek(position);
```
> *position* - value between 0...100 %

```javascript
Spotify.setVolume(val1,val2);
```
> *val1,val2* - the volume for the left and right channel (between 0...100%)


## Events
```javascript
Spotify.Events.onMetadataChanged = function(args){};
```
> *args[0]* - current track;
> *args[1]* - artist name;
> *args[2]* - album name;
> *args[3]* - track duration.

```javascript
Spotify.Events.onPlayerPlay = function(arg){};
```
> *arg* - name of event (string)

```javascript
Spotify.Events.onPrev = function(args){};
```
> *args[0]*  - action name;

```javascript
Spotify.Events.onNext = function(args){};
```
> *args[0]*  - action name;        
     
```javascript
Spotify.Events.onPause = function(args){};
```
> *args[0]*  - action name;    

```javascript
Spotify.Events.onPlay = function(args){};
```
> *args[0]*  - action name; 
      
```javascript
Spotify.Events.onAudioFlush = function(args){};
```
> *args[0]*  - position (ms); 
   
```javascript
Spotify.Events.onTrackChanged = function(args){};
```
> *args[0]*  - action name;      
  
```javascript
Spotify.Events.onPosition = function(args){};
```
> *args[0]*  - position (ms);      
      
```javascript
Spotify.Events.onVolumeChanged = function(args){};
```
> *args[0]*  - volume betwen 0.0 ....1.0
    
### Authors
 - Aleksey Stepanets


[![N|Solid](http://procoders.tech/art/powered.png)](http://procoders.tech/)
