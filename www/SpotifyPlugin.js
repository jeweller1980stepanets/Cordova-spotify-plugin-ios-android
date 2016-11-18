var exec = require('cordova/exec');
               
module.exports = {
    login : function(a,b) {
        exec(
                     function() {},
                     function() {},
                     "SpotifyPlugin",
                     "login",
                     [a,b]
                     )
    },
play:function(val){
    exec(
                 function() {},
                 function() {},
                 "SpotifyPlugin",
                 "play",
                 [val]
                 )
},
    pause : function(){
        exec(
                     function(){},
                     function(){},
                     "SpotifyPlugin",
                     "pause",
                     []
                     )
    },
    next : function(){
        exec(
                     function(){},
                     function(){},
                     "SpotifyPlugin",
                     "next",
                     []
                     )
    },
    prev : function(){
        exec(
                     function(){},
                     function(){},
                     "SpotifyPlugin",
                     "prev",
                     []
                     )
    },
    logout : function(){
        exec(
                     function(){},
                     function(){},
                     "SpotifyPlugin",
                     "logout",
                     []
                     )
    },
    seek : function(val){
        exec(
                     function(){},
                     function(){},
                     "SpotifyPlugin",
                     "seek",
                     [val]
                     )
    },
    setVolume : function(val){
        exec(
                     function(){},
                     function(){},
                     "SpotifyPlugin",
                     "volume",
                     [val]
                     )
    },
    Events : {
        onPlayerPlay : function(args){},
        onMetadataChanged :function(args){},
        onPrev : function(args){
            //arg[0] - action
        },
        onNext : function(args){
            //arg[0] - action
        },
        onPause : function(args){
            //arg[0] - action
        },
        onPlay : function(args){
            //arg[0] - action
        },
        onAudioFlush : function(arg){
            //arg[0] - position (ms)
        },
        onTrackChanged : function(arg){
            //arg[0] - action
        },
        onPosition : function(arg){
            //arg[0] - position ms
        },
        onVolumeChanged : function(arg){
            //arg - volume betwen 0.0 ....1.0
        }
        
    }
    
};

