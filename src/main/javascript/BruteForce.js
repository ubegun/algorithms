'use strict';

function BruteForce() {

    var key;
    var charSet;
    var charSetLength;
    var keyLength;

    this.hash = function (s){
        var h = 7;
        var letters = "acdegilmnoprstuw";
        for(var i = 0; i < s.length; i++) {
            h = (h * 37 + letters.indexOf(s[i]))
        }
        return h;
    };

    this.calc = function(charSet, key) {
        this.key = key;
        this.charSet = charSet;
        charSetLength = charSet.length;
        keyLength =  key.length;

        this.forEach(0,  (c) => {
            this.key[0] = c;
            if(this.testString()) {
                return true;
            }
    });
    }

    this.forEach = function(index, sequence) {
        if(index == keyLength){
            for(var i = 0; i < charSetLength; i++){
                if(sequence(this.charSet[i]))
                    break;
            }
            return;
        }
        var thisIndex = index++;
        this.forEach(index, (c) => {
            this.key[thisIndex]= c;
            for (var i = 0; i < charSetLength; i++) {
                if(sequence(this.charSet[i]))
                    break;
            }
    });
    }
}

var findString = function () {
    var start = new Date().getTime();
    BruteForce.prototype.testString = function() {
        var res = this.key.join('');
        if (this.hash(res) == 680131659347){//ee
            console.log('found string: ' + res);
            return true;
        }
        return false;
    };

    var bf = new BruteForce();

    bf.calc('qwertyuiopasdfghjklzxcvbnm', new Array(7)); //qwertyuiopasdfghjklzxcvbnm
    var end = new Date().getTime();
    var time = end - start;
    console.log('Execution time: ' + time);
};

findString();