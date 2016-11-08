/**
 * Created by akopylov on 18/12/15.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions');
const RackSource = require('../sources/RackSource');

class RackStore {
    constructor() {
        this.tiles = [];
        this.shuffle = [];
        this.bindListeners({
            handleGetRack: Actions.GET_RACK,
            handleTileDroppedToField: Actions.TILE_DROPPED,
            handleTileReverted: Actions.TILE_REVERTED,
            handleTileDroppedToTrashcan: Actions.TILE_DROPPED_TO_TRASHCAN,
            handleTrashcanReverted: Actions.TRASHCAN_REVERTED,
            handleShuffle: Actions.SHUFFLE,
        });

        this.exportAsync(RackSource);
    }

    handleGetRack(letters) {
        this.tiles = letters;
    }

    handleTileReverted(letter) {
        var index = this.tiles.findIndex(tile => tile.hidden && tile.letter == letter.letter);
        if (index >= 0) {
            this.tiles[index].hidden = false;
        }
    }

    handleTileDroppedToField(letter) {
        let index = this.tiles.findIndex(tile => (tile.letter == letter.letter && !tile.hidden));
        this.tiles[index].hidden = true;
    }

    handleTileDroppedToTrashcan(letter) {
        let index = this.tiles.findIndex(tile => tile.letter == letter);
        this.tiles[index].hidden = true;
        this.shuffle.push(letter);
    }

    handleTrashcanReverted() {
        this.tiles.forEach(tile => tile.hidden = false);
        this.shuffle = [];
    }

    handleShuffle() {
        let rest = this.tiles
            .filter(tile => !tile.hidden)
            .map(tile => tile.letter)
            .reduce((prev, next) => prev + next, '');
        let shuffle = this.shuffle.reduce((prev, next) => prev + next, '');
        console.log("*** RackStore.handleShuffle", rest, shuffle);
        this.getInstance().shuffleRack(rest, shuffle);
    }
}

module.exports = alt.createStore(RackStore, 'RackStore');