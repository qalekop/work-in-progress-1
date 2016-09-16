/**
 * Created by akopylov on 14/09/16.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions');
const ENDPOINT_URL = '/scrabble/game/feedback/';

var ws;

class ScrabbleStore {
    constructor() {
        this.bootstrapRequest = false;
        this.wsReady = false;

        this.bindListeners({
            handleBootstrapRequest: Actions.BOOTSTRAP_REQUEST,
            handleWsReady: Actions.WS_READY
        });

        var endpointUrl = (window.location.protocol == 'http:' ? 'ws://' : 'wss://')
            + window.location.host + ENDPOINT_URL;
        if ('WebSocket' in window) {
            ws = new WebSocket(endpointUrl);
        } else if ('MozWebSocket' in window) {
            ws = new MozWebSocket(endpointUrl);
        } else {
            console.log('WebSocket is not supported by this browser.');
        }
        console.log('*** endpoint url=' + endpointUrl + "; " + ws);
        if (!!ws) {
            ws.onmessage = function(event) {
                // todo implement me
                var response = JSON.parse(event.data);
                console.log('*** response received: ' + response.value);
            };
            ws.onopen = function(event) {
                console.log('*** ws open');
                Actions.wsReady();
            };
            ws.onerror = function() { console.log('*** ws.onerror')};
            ws.onclose = function() { console.log('*** ws.onclose')};
        }
    }

    handleBootstrapRequest() {
        if (this.wsReady) {
            this.bootstrapRequest = false;
            ws.send('waiting for machine move');
            // Actions.getField();
        } else {
            this.bootstrapRequest = true;
        }
    }
    handleWsReady() {
        this.wsReady = true;
        if (this.bootstrapRequest) {
            ws.send('waiting for machine move');
            // Actions.getField();
        }
    }
}

module.exports = alt.createStore(ScrabbleStore, 'ScrabbleStore');