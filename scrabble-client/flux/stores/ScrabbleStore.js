/**
 * Created by akopylov on 14/09/16.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions'),
    RackStore = require('./RackStore');
const ENDPOINT_URL = '/scrabble/game/feedback/',
    MOVE_URL = '/scrabble/game/move/';

var ws;

class ScrabbleStore {
    constructor() {
        this.bootstrapRequest = false;
        this.wsReady = false;

        this.cells = [];

        this.bindListeners({
            handleMakeMove: Actions.MAKE_MOVE,
            handleGetField: Actions.GET_FIELD,
            handleBootstrapRequest: Actions.BOOTSTRAP_REQUEST,
            handleWsReady: Actions.WS_READY,
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
                var response = JSON.parse(event.data);
                console.log('*** response received');
                Actions.getField(response.cells);
                Actions.getRack(response.rackHuman);
                Actions.getScore({'human': response.scoreHuman, 'machine': response.scoreMachine});
                Actions.hideDialog(false);
            };
            ws.onopen = function(event) {
                console.log('*** ws open');
                Actions.wsReady();
            };
            ws.onerror = function() { console.log('*** ws.onerror')};
            ws.onclose = function(event) {
                this.wsReady = false;
                console.log('*** ws.onclose', event.wasClean);
            };
        }
    }

    handleMakeMove() {
        var field = this.cells.filter(function(cell) { return cell.availability == 'OCCUPIED';});
        let rackState = RackStore.getState();
        let rest = rackState.tiles
            .filter(tile => !tile.hidden)
            .map(tile => tile.letter)
            .reduce((prev, next) => prev + next, '');
        let shuffle = rackState.shuffle.reduce((prev, next) => prev + next, '');
        console.log("*** ScrabbleStore.handleMakeMove", field, rest, shuffle);
        let gameState = {
            'cells': field,
            'rest': rest,
            'shuffle': shuffle
        };
        new Promise(function(resolve) {
            $.ajax({
                method: 'POST',
                url: MOVE_URL,
                data: JSON.stringify(gameState),
                dataType: 'json',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                success: function(data) { resolve(data); }
            })
        }).then(function (response) {
            console.log('*** Promise responsed with ', response.success);
            Actions.handleResponse(response);
            return response.success;
        }).then(function (success) {
            // start machine move sequence or simply get field
            console.log('*** Promise.then', success);
            ws.send(success ? "MOVE" : "GET_FIELD");
        });

    }

    handleBootstrapRequest() {
        if (this.wsReady) {
            this.bootstrapRequest = false;
            ws.send("GET_FIELD");
        } else {
            this.bootstrapRequest = true;
        }
    }

    handleWsReady() {
        this.wsReady = true;
        if (this.bootstrapRequest) {
            ws.send("GET_FIELD");
        }
    }

    handleGetField(cells) {
        // cells.forEach(cell => console.log(`${cell.letter}; ${cell.state}`));
        this.cells = cells;
    }
}

module.exports = alt.createStore(ScrabbleStore, 'ScrabbleStore');