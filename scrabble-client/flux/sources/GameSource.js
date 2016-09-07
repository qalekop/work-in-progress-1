/**
 * Created by alexeikopylov on 16.02.16.
 */

const Actions = require('../actions/Actions');
const GAME_URL = '/scrabble/game/game/',
      MOVE_URL = '/scrabble/game/move/';

const GameSource = {
    getField() {
        return {
            remote(state) {
                console.log('*** Source.getField');
                return new Promise(function(resolve) {
                    $.ajax({
                        method: 'GET',
                        url: GAME_URL,
                        success: function(data) { resolve(data); },
                        dataType: 'json'
                    });
                })
            }
            , local() { return null; }
            , success: Actions.getField
        }
    }

    , makeMove() {
        return {
            remote(state, field) {
                console.log('*** Source.makeMove');
                return new Promise(function(resolve) {
                    $.ajax({
                        method: 'POST',
                        url: MOVE_URL,
                        data: JSON.stringify(field),
                        dataType: 'json',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        success: function(data) { resolve(data); }
                    })
                })
            }
            , local() { return null; }
            , success: Actions.handleResponse
        }
    }
};

module.exports = GameSource;