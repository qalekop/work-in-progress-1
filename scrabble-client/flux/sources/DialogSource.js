/**
 * Created by akopylov on 02/09/16.
 */

const Actions = require('../actions/Actions');
const ENDPOINT_URL = 'scrabble/game/feedback/';

const DialogSource = {
    waitForResponse() {
        return {
            remote(state, user) {
                console.log('*** waiting for response for ' + user);
                return new Promise(function(resolve) {
                    const endpointURL = ENDPOINT_URL + user;
                    var ws = null;
                    if ('WebSocket' in window) {
                        ws = new WebSocket(endpointURL);
                    } else if ('MozWebSocket' in window) {
                        ws = new MozWebSocket(endpointURL);
                    } else {
                        console.log('WebSocket is not supported by this browser.');
                        return;
                    }
                    ws.onmessage = function(event) {
                        resolve(event.data);
                    }
                })
            }
            , local() { return null;}
            , success: Actions.getField
        }
    }
};

module.exports = DialogSource;