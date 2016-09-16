/**
 * Created by akopylov on 29/08/16.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions');
const ENDPOINT_URL = '/scrabble/game/feedback/';

var ws;

class DialogStore {
    constructor() {
        this.modalShown = false;
        this.showCloseButton = false;
        this.text = '';

        this.bindListeners({
            showDialog: Actions.MAKE_MOVE,
            hideDialog: Actions.HIDE_DIALOG,
            proceedWithDialog: Actions.HANDLE_RESPONSE
        });

/*
        var endpointUrl = (window.location.protocol == 'http:' ? 'ws://' : 'wss://')
            + window.location.host + ENDPOINT_URL;
        if ('WebSocket' in window) {
            ws = new WebSocket(endpointUrl);
        } else if ('MozWebSocket' in window) {
            ws = new MozWebSocket(endpointUrl);
        } else {
            console.log('WebSocket is not supported by this browser.');
        }
        if (!!ws) ws.onmessage = function(event) {
            console.log('*** response received: ' + event.data);
            Actions.hideDialog();
            //Actions.getField();
        };
*/
    }

    hideDialog() {
        this.modalShown = false;
    }

    showDialog() {
        this.modalShown = true;
        this.showCloseButton = false;
        this.text = 'Please wait...';
    }

    proceedWithDialog(response) {
        if (!response.success) {
            console.log('*** Error:' + response.message);
            this.showCloseButton = true;
            this.text = response.message;
        } else {
            if (!!ws) ws.send('waiting for machine move');
        }
    }
}

module.exports = alt.createStore(DialogStore, 'DialogStore');
