package cordova.plugin.spotify;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import com.spotify.sdk.android.player.Config;

import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.SpotifyPlayer;

/**
 * Created by Aleksey on 10/10/2016.
 */
public class SpotifyPlugin extends CordovaPlugin implements
Player.NotificationCallback, ConnectionStateCallback{
    private static final String TAG = "CordovaSpotifyPlugin";

    private static final String ACTION_AUTH = "auth";

    private static final String ACTION_LOGIN = "login";
    private static final String ACTION_PLAY = "play";
    private static final String ACTION_PAUSE = "pause";
    private static final String ACTION_RESUME = "resume";
    private static final String ACTION_NEXT = "next";
    private static final String ACTION_PREV = "prev";
    private static final String ACTION_PLAY_ALBUM = "playAlbum";
    private static final String ACTION_PLAY_PLAYLIST = "playPlayList";
    private static final String ACTION_LOG_OUT = "logout";
    private static final String ACTION_SEEK = "seek";
    private static final String ACTION_VOLUME = "setVolume";
    private static final String METHOD_SEND_TO_JS_OBJ = "window.cordova.plugins.SpotifyPlugin.Events.";
    private static final String ACTION_GET_POSITON = "getPosition";
    private static final String ACTION_GET_TOKEN = "getToken";
    private static final int REQUEST_CODE = 1337;
    
    private String clientId;// ="4eb7b5c08bee4d759d34dbc1823fd7c5";
    private String redirectUri;// = "testshema://callback";
    
    
    private CallbackContext loginCallback;
    private String currentAccessToken;
    private Boolean isLoggedIn = false;
    
    private SpotifyPlayer currentPlayer;
    private PlaybackState mCurrentPlaybackState;
    
    private Metadata mMetaData;
    
    private CordovaWebView mWebView;
    private  CordovaInterface mInterface;
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        Log.i(TAG, "Initializing...");
        mWebView = webView;
        mInterface = cordova;
        
        
        int cbResId = cordova.getActivity().getResources().getIdentifier("redirect_uri", "string", cordova.getActivity().getPackageName());
        
        Log.i(TAG, "cb ID ID" + cbResId);
        
        redirectUri = cordova.getActivity().getString(cbResId) + "://callback";
        
        Log.i(TAG, "Set up local vars" + clientId + redirectUri);
        super.initialize(cordova,webView);
        
        
    }
    
    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext)  {
        Boolean success = false;
        
        if(ACTION_LOGIN.equalsIgnoreCase(action)) {
            Log.i(TAG, "LOGIN");
            JSONArray scopes = new JSONArray();
            Boolean fetchTokenManually = false;
            
            /*   try {
             scopes = data.getJSONArray(0);
             // fetchTokenManually = data.getBoolean(1);
             } catch(JSONException e) {
             Log.e(TAG, e.toString());
             }*/
            
            cordova.setActivityResultCallback(this);
            loginCallback = callbackContext;
            String uri = "";
            
            try {
                uri = data.getString(0);
            } catch(JSONException e) {
                Log.e(TAG, e.toString());
            }
            this.login(uri);
            success = true;
        } else if(ACTION_PLAY.equalsIgnoreCase(action)) {
            String uri = "";
            
            try {
                uri = data.getString(0);
            } catch(JSONException e) {
                Log.e(TAG, e.toString());
            }
            
            this.play(uri);
            success = true;
        }else if(ACTION_AUTH.equalsIgnoreCase(action)){
            String token = "";
            String id = "";
            try {
                token = data.getString(0);
                id = data.getString(1);
            }catch (JSONException e){
                Log.e(TAG, e.toString());
            }
            this.auth(token,id);
        }
        else if(ACTION_PAUSE.equalsIgnoreCase(action)) {
            this.pause();
            success = true;
        } else if(ACTION_RESUME.equalsIgnoreCase(action)) {
            // this.resume();
            success = true;
        } else if(ACTION_NEXT.equalsIgnoreCase(action)){
            this.next();
            success = true;
        } else if(ACTION_PREV.equalsIgnoreCase(action)){
            this.prev();
            success=true;
        }else if(ACTION_PLAY_ALBUM.equalsIgnoreCase(action)){
            String uri = "";
            
            try {
                uri = data.getString(0);
            } catch(JSONException e) {
                Log.e(TAG, e.toString());
            }
            this.playAlbum(uri);
            success = true;
        }else if(ACTION_PLAY_PLAYLIST.equalsIgnoreCase(action)){
            String uri = "";
            
            try {
                uri = data.getString(0);
            } catch(JSONException e) {
                Log.e(TAG, e.toString());
            }
            this.playPlayList(uri);
            success=true;
        }else if(ACTION_LOG_OUT.equalsIgnoreCase(action)){
            this.logout();
            success = true;
        } else if(ACTION_SEEK.equalsIgnoreCase(action)){
            int   val=0;
            try {
                val = data.getInt(0);
            } catch(JSONException e) {
                Log.e(TAG, e.toString());
            }
            Log.d(TAG, String.valueOf(val));
            this.seek(val);
            success = true;
        } else if(ACTION_VOLUME.equalsIgnoreCase(action)){
            int   val=0;
            try {
                val = data.getInt(0);
            } catch(JSONException e) {
                Log.e(TAG, e.toString());
            }
            this.setVolume(val);
            success = true;
        }else if(ACTION_GET_POSITON.equalsIgnoreCase(action)){
            this.getPosition();
            success = true;
        }else if(ACTION_GET_TOKEN.equalsIgnoreCase(action)){
        this.getToken(callbackContext);
        success = true;
        }
    
        return success;
    }
    private void auth(String token, String id){
        Log.d(TAG,"auth()");
        if (currentPlayer == null) {

            Config playerConfig = new Config(cordova.getActivity(), token, id);
// Since the Player is a static singleton owned by the Spotify class, we pass "this" as
// the second argument in order to refcount it properly. Note that the method
// Spotify.destroyPlayer() also takes an Object argument, which must be the same as the
// one passed in here. If you pass different instances to Spotify.getPlayer() and
// Spotify.destroyPlayer(), that will definitely result in resource leaks.
            currentPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {

                @Override
                public void onInitialized(SpotifyPlayer player) {
                    Log.d(TAG,"-- Player initialized --");
                    currentPlayer.setConnectivityStatus(mOperationCallback, getNetworkConnectivity(cordova.getActivity().getApplicationContext()));
                    currentPlayer.addNotificationCallback(SpotifyPlugin.this);
                    currentPlayer.addConnectionStateCallback(SpotifyPlugin.this);
// Trigger UI refresh
                }

                @Override
                public void onError(Throwable error) {
                    Log.d(TAG,"Error in initialization: " + error.getMessage());
                    JSONArray array = new JSONArray();
                    array.put("invalid access token");
                    sendUpdate("onPlayError", new Object[]{array});
                }
            });


        } else {
            currentPlayer.login(token);
        }
    }

    private void getToken(CallbackContext callbackContext){
        callbackContext.success( this.currentAccessToken);
        Log.d(TAG,"getToken()"+callbackContext);
    }
    private void setVolume(int value) {
        Log.d(TAG, "Volume = " + value);
        cordova.getActivity().setVolumeControlStream(value);//TODO: volume
        
    }
    
    private void getPosition(){
        int x = (int)currentPlayer.getPlaybackState().positionMs;
        Log.d(TAG, "position = " + x);
        sendUpdate("onPosition",new Object[]{x});
        
    }
    private void seek(int val) {
        mMetaData = currentPlayer.getMetadata();
        final long dur = mMetaData.currentTrack.durationMs;
        currentPlayer.seekToPosition(mOperationCallback,(int)dur*val/100);
        Log.d(TAG,mMetaData.toString());
    }
    
    /*private final Player.NotificationCallback mNotificationCallback = new Player.NotificationCallback(){
     
     @Override
     public void onPlaybackEvent(PlayerEvent playerEvent) {
     Log.d(TAG,"NotificationCallback OK! ");
     }
     
     @Override
     public void onPlaybackError(Error error) {
     Log.d(TAG,"NotificationCallback ERROR:" + error);
     }
     };*/
    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
    @Override
    public void onSuccess() {
    Log.d(TAG,"OK!");
}

@Override
public void onError(Error error) {
Log.d(TAG,"ERROR:" + error);
    JSONArray array = new JSONArray();
    array.put(error);
    sendUpdate("onPlayError", new Object[]{array});
}
};


private void login(String val) {
clientId = val;
final AuthenticationRequest request = new AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri)
.setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private","user-read-email", "streaming"})
.build();
AuthenticationClient.openLoginActivity(cordova.getActivity(), REQUEST_CODE, request);
}

private void logout() {
AuthenticationClient.clearCookies(cordova.getActivity());

this.clearPlayerState();
isLoggedIn = false;
currentAccessToken = null;

}

private void clearPlayerState() {
if(currentPlayer != null) {
currentPlayer.pause(mOperationCallback);
currentPlayer.logout();
}

//this.login(clientId);
}

public   void playAlbum(String id){
Log.d(TAG, "PLAYING ALBUM");
this.play(id);
}

public   void playPlayList(String id){
Log.d(TAG, "PLAYING playListPlay");
this.play(id);
}
public  void next(){
Log.d(TAG,"NEXT java");
currentPlayer.skipToNext(mOperationCallback);

}
public void prev(){
Log.d(TAG,"PREV java");
currentPlayer.skipToPrevious(mOperationCallback);

}
private void play(String uri) {
if(currentPlayer == null) {
    JSONArray array = new JSONArray();
    array.put("player did not initialize");
    sendUpdate("onPlayError", new Object[]{array});
    return;
}
if(!currentPlayer.isLoggedIn()) {
Log.e(TAG, "Current Player is initialized but player is not logged in, set access token manually or call login with fetchTokenManually : false");
return;
}

Log.i(TAG, "Playing URI: " + uri);

currentPlayer.playUri(mOperationCallback, uri,0,0);
}




private void pause() {
if(clientId == null || isLoggedIn == false || currentAccessToken == null || currentPlayer == null) return;
if(currentPlayer.getPlaybackState().isPlaying) {
currentPlayer.pause(mOperationCallback);
}else{
currentPlayer.resume(mOperationCallback);
}
}
/*
 private void resume() {
 currentPlayer.resume(mOperationCallback);
 }
 */
@Override
public void onActivityResult(int requestCode, int resultCode, Intent intent) {
super.onActivityResult(requestCode, resultCode, intent);

Log.i(TAG, "ACTIVITY RESULT: ");
Log.i(TAG, "Request Code " + requestCode);
Log.i(TAG, "Result Code " + resultCode);

JSONObject ret = new JSONObject();

// Check if result comes from the correct activity
if (requestCode == REQUEST_CODE) {
AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
switch(response.getType()) {
case TOKEN :
isLoggedIn = true;
Log.i(TAG, "TOKEN " + response.getAccessToken() );
currentAccessToken = response.getAccessToken();
onAuthenticationComplete(response);
JSONArray array1 = new JSONArray();
    array1.put("logged in");
    sendUpdate("onLogedIn", new Object[]{array1});

break;
case CODE :
isLoggedIn = false;
Log.i(TAG, "RECEIVED CODE" + response.getCode());

try {
ret.put("authCode", response.getCode());
} catch(JSONException e) {
Log.e(TAG, e.getMessage());
}

loginCallback.success(ret);
loginCallback = null;
break;
case ERROR :
Log.e(TAG, response.getError());
loginCallback.error(response.getError());
JSONArray array = new JSONArray();
    array.put("did not login");
    sendUpdate("onDidNotLogin", new Object[]{array});
break;
default:JSONArray array3 = new JSONArray();
array3.put("did not login");
sendUpdate("onDidNotLogin", new Object[]{array3});
break;
}
}


}
private Connectivity getNetworkConnectivity(Context context) {
ConnectivityManager connectivityManager;
connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
if (activeNetwork != null && activeNetwork.isConnected()) {
Log.d(TAG,"active network" + activeNetwork.isConnected());
return Connectivity.fromNetworkType(activeNetwork.getType());
} else {
return Connectivity.OFFLINE;
}
}

private void onAuthenticationComplete(AuthenticationResponse response) {
// Once we have obtained an authorization token, we can proceed with creating a Player.

if (currentPlayer == null) {
Config playerConfig = new Config(cordova.getActivity(), response.getAccessToken(), clientId);
// Since the Player is a static singleton owned by the Spotify class, we pass "this" as
// the second argument in order to refcount it properly. Note that the method
// Spotify.destroyPlayer() also takes an Object argument, which must be the same as the
// one passed in here. If you pass different instances to Spotify.getPlayer() and
// Spotify.destroyPlayer(), that will definitely result in resource leaks.
currentPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {

@Override
public void onInitialized(SpotifyPlayer player) {
Log.d(TAG,"-- Player initialized --");
currentPlayer.setConnectivityStatus(mOperationCallback, getNetworkConnectivity(cordova.getActivity().getApplicationContext()));
currentPlayer.addNotificationCallback(SpotifyPlugin.this);
currentPlayer.addConnectionStateCallback(SpotifyPlugin.this);
// Trigger UI refresh
}

@Override
public void onError(Throwable error) {
Log.d(TAG,"Error in initialization: " + error.getMessage());
    JSONArray array = new JSONArray();
    array.put(error);
    sendUpdate("onPlayError", new Object[]{array});
}
});


} else {
currentPlayer.login(response.getAccessToken());
}

}

@Override
public void onLoggedIn() {
Log.d("MainActivity", "User logged in");
JSONArray array = new JSONArray();
array.put("logged in");
sendUpdate("onLogedIn", new Object[]{array});
}

@Override
public void onLoggedOut() {
Log.d("MainActivity", "User logged out");
}

@Override
public void onLoginFailed(int i) {
Log.d("MainActivity", "Login failed" + i);
}


@Override
public void onTemporaryError() {
Log.d("MainActivity", "Temporary error occurred");
}

@Override
public void onConnectionMessage(String message) {
Log.d("MainActivity", "Received connection message: " + message);
}





@Override
public void onDestroy() {
super.onDestroy();
}


private static final String EVENT_AUDIO_FLASH = "kSpPlaybackEventAudioFlush";

private static final String EVENT_DELIVERY_DOUN = "kSpPlaybackNotifyAudioDeliveryDone";

private static final String EVENT_BECAME_ACTIVE ="kSpPlaybackNotifyBecameActive";
private static final String EVENT_BECAME_INACTIVE = "kSpPlaybackNotifyBecameInactive";
private static final String EVENT_CONTEXT_CHANGED ="kSpPlaybackNotifyContextChanged";
private static final String EVENT_LOST_PERMISSION = "kSpPlaybackNotifyLostPermission";
private static final String EVENT_METADATA_CHANGED = "kSpPlaybackNotifyMetadataChanged";
private static final String EVENT_NOTIFY_NEXT ="kSpPlaybackNotifyNext";
private static final String EVENT_NOTIFY_PAUSE = "kSpPlaybackNotifyPause";
private static final String EVENT_NOTIFY_PLAY ="kSpPlaybackNotifyPlay";
private static final String EVENT_NOTIFY_PREV = "kSpPlaybackNotifyPrev";
private static final String EVENT_REPEAT_OFF="kSpPlaybackNotifyRepeatOff";
private static final String EVENT_REPEAT_ON="kSpPlaybackNotifyRepeatOn";
private static final String EVENT_SHUFLE_OFF="kSpPlaybackNotifyShuffleOff";
private static final String EVENT_SHUFLE_ON = "kSpPlaybackNotifyShuffleOn";
private static final String EVENT_TRACK_CHANGED = "kSpPlaybackNotifyTrackChanged";
private static final String EVENT_TRACK_DELIVERED = "kSpPlaybackNotifyTrackDelivered";

@Override
public void onPlaybackEvent(PlayerEvent playerEvent) {
mMetaData = currentPlayer.getMetadata();
mCurrentPlaybackState = currentPlayer.getPlaybackState();
JSONArray array = new JSONArray();

Log.d("MainActivity", "Playback event received: " + playerEvent.name());

if(playerEvent.name().equals(EVENT_NOTIFY_PAUSE)){
Log.d(TAG,"player paused");
array.put("player paused");
sendUpdate("onPause", new Object[]{array});

} else if(playerEvent.name().equals(EVENT_NOTIFY_NEXT)){
Log.d(TAG,"player next");
array.put("player next");
sendUpdate("onNext", new Object[]{array});

} else if(playerEvent.name().equals(EVENT_NOTIFY_PREV)){
Log.d(TAG,"player prev");
array.put("previos");
sendUpdate("onPrev", new Object[]{array});

} else if(playerEvent.name().equals(EVENT_NOTIFY_PLAY)){
Log.d(TAG,"player play");
array.put("player play");
sendUpdate("onPlay", new Object[]{array});

} else if(playerEvent.name().equals(EVENT_AUDIO_FLASH)){
Log.d(TAG,"player audio flush" + mCurrentPlaybackState.positionMs + "ms");
array.put(mCurrentPlaybackState.positionMs);
sendUpdate("onAudioFlush", new Object[]{array});

} else if(playerEvent.name().equals(EVENT_METADATA_CHANGED)){
Log.d(TAG,"player metadata changed" + mMetaData);
array.put(mMetaData.currentTrack.name);
array.put(mMetaData.currentTrack.artistName);
array.put(mMetaData.currentTrack.albumName);
array.put(mMetaData.currentTrack.durationMs);

sendUpdate("onMetadataChanged",new Object[]{array});

} else if(playerEvent.name().equals(EVENT_CONTEXT_CHANGED)){
Log.d(TAG,"player context changed " + mMetaData.contextName);
} else if(playerEvent.name().equals(EVENT_DELIVERY_DOUN)) {
Log.d(TAG, "player EVENT_DELIVERY_DOUN ");

}else if(playerEvent.name().equals(EVENT_BECAME_ACTIVE)) {
Log.d(TAG, "player EVENT_BECAME_ACTIVE ");

}else if(playerEvent.name().equals(EVENT_BECAME_INACTIVE)) {
Log.d(TAG, "player EVENT_BECAME_INACTIVE ");

}else if(playerEvent.name().equals(EVENT_LOST_PERMISSION)) {
Log.d(TAG, "player EVENT_LOST_PERMISSION ");

}else if(playerEvent.name().equals(EVENT_REPEAT_OFF)) {
Log.d(TAG, "player EVENT_REPEAT_OFF ");

}else if(playerEvent.name().equals(EVENT_REPEAT_ON)) {
Log.d(TAG, "player EVENT_REPEAT_ON ");

}else if(playerEvent.name().equals(EVENT_SHUFLE_OFF)) {
Log.d(TAG, "player EVENT_SHUFLE_OFF ");

}else if(playerEvent.name().equals(EVENT_SHUFLE_ON)) {
Log.d(TAG, "player EVENT_SHUFLE_ON ");

}else if(playerEvent.name().equals(EVENT_TRACK_CHANGED)) {
Log.d(TAG, "player EVENT_TRACK_CHANGED ");
array.put("track changed");
sendUpdate("onTrackChanged", new Object[]{array});

}else if(playerEvent.name().equals(EVENT_TRACK_DELIVERED)) {
Log.d(TAG, "player EVENT_TRACK_DELIVERED ");

}
// Remember kids, always use the English locale when changing case for non-UI strings!
// Otherwise you'll end up with mysterious errors when running in the Turkish locale.
// See: http://java.sys-con.com/node/46241

}

@Override
public void onPlaybackError(Error error) {
Log.d("MainActivity", "Playback error received: " + error.toString());
    JSONArray array = new JSONArray();
    array.put(error);
    sendUpdate("onPlayError", new Object[]{array});
}
public void sendUpdate(final String action, final Object[] params) {
String method = String.format("%s%s", METHOD_SEND_TO_JS_OBJ, action);
final StringBuilder jsCommand = new StringBuilder();

jsCommand.append("javascript:").append(method).append("(");
int nbParams = params.length;
for (int i = 0; i < nbParams;) {
Log.d(TAG,"sendUpdete" + params[i]);
jsCommand.append(params[i++]);
if (i != nbParams) {
jsCommand.append(",");
}
}
jsCommand.append(")");

Log.d(TAG, "sendUpdate jsCommand : " + jsCommand.toString());

mWebView.getView().post(new Runnable(){
public void run(){

mWebView.loadUrl(jsCommand.toString());

}
});

}



}
