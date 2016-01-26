/**
 * Created by akopylov on 18/12/15.
 */

var Actions = require('../actions/Actions');
var RACK_URL = '/scrabble/game/rack/';
var MOVE_URL = '/scrabble/game/move/';

var RackSource = {
    getRack() {
        return {
            remote(state, letters) {
                console.log('*** Source.getRack ' + letters);
                return new Promise(function(resolve) {
                    $.ajax({
                        method: 'POST',
                        url: RACK_URL,
                        data: {'letters': letters},
                        success: function(data) { resolve(data); },
                        dataType: 'json'
                    });
                })
            }
            , local() { return null; }
            , success: Actions.getRack
        }
    }

    , makeMove() {
        return {
            remote(state, field) {
                console.log('*** Source.makeMove ' + field);
                return new Promise(function(resolve) {
                    $.ajax({
                        method: 'POST',
                        url: MOVE_URL,
                        data: field,
                        dataType: 'json',
                        success: function(data) {resolve(data);}
                    })
                })
            }
            , local() { return null; }
            , success: Actions.success
        }
    }
};
module.exports = RackSource;
