var exec = require('cordova/exec');
               
module.exports = {
    login : function(a,b,url) {
        let swap = url+'/swap';
        let refresh = url+ '/refresh';
        exec(
                     function() {},
                     function() {},
                     "SpotifyPlugin",
                     "login",
                     [a,b,swap,refresh]
                     )
    },
     auth : function(token,id){
                   exec(
                        function() {},
                        function() {},
                        "SpotifyPlugin",
                        "auth",
                        [token,id]
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
     getToken : function(success,error){
               exec(
                   success,// function(res){alert(res);},//res - TOKEN
                    error,//function(){console.log("error");},
                    "SpotifyPlugin",
                    "getToken",
                    []
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
        },
         onLogedIn :function(arg){
            alert(arg);
        },
        onDidNotLogin:function(arg){
            alert(arg);
        }
        
    }
    
};

