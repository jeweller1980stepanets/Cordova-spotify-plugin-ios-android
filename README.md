[![N|Solid](http://procoders.tech/art/powered.png)](http://procoders.tech/)

# Cordova Spotify Plugin
This plugin included all main functions of audio player for Spotify servise :smirk:

## Installation
> To communicate with Spotify you need to register your application’s bundle id section in the [Developer Portal][PlDb]. This value is used to authenticate your application against Spotify client.

![Image alt](https://github.com/jeweller1980stepanets/image/blob/master/1.png)

You may install latest version from master
```sh
cordova plugin add https://github.com/jeweller1980stepanets/Cordova-spotify-plugin-ios-android --variable URL_SCHEME="your custom url scheme"
```
### Removing the Plugin from project
```sh
cordova plugin rm cordova.plugin.spotify
```
## Supported Platforms
> - Android
> - iOS

### Platform specific
:warning:***for Android*** platform not implemented method `setVolume()` and `event onVolumeChanged()`
It will be fixed when this methods will be in Spotify SDK


:warning:***for iOS*** you must download *spotify_token_swap.rb*. You may download it [here] [PlGh].
To run the service, enter your client ID, client secret and client callback URL and run the project.

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
Spotify.login(appId, redirectURL);
```
> - *appId* - your application id in Spotify
> - *redirectURL* - White-listed addresses to redirect to after authentication success OR failure 

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
>Called when metadata for current changed.
 This event occurs when playback starts or changes to a different context,
 when a track switch occurs, etc. This is an informational event that does
 not require action, but should be used to keep the UI display updated with
 the latest metadata information.
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
Spotify.Events.onAudioFlush = function(args){};
```
> onAudioFlush playback to a given location in the current track.
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
[PlDb]:<https://developer.spotify.com>
[PlGh]:<https://github.com/spotify/ios-sdk/blob/master/Demo%20Projects/spotify_token_swap.rb>

[![N|Solid](http://procoders.tech/art/powered.png)](http://procoders.tech/)
