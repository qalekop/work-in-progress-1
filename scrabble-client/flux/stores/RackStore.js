/**
 * Created by akopylov on 18/12/15.
 */

var alt = require('../../alt');

var Actions = require('../actions/Actions');

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
        });
        this.exportPublicMethods({
            getRest: this.getRest
        });
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

    getRest() {
        return this.getInstance().tiles.filter(tile => !tile.hidden).reduce(function(prev, next) {return prev + next}, '');
    }

    handleTileDroppedToField(letter) {
        let index = this.tiles.findIndex(tile => tile.letter == letter.letter);
        this.tiles[index].hidden = true;
    }

    handleTileDroppedToTrashcan(letter) {
        let index = this.tiles.findIndex(tile => tile.letter == letter);
        this.tiles[index].hidden = true;
        this.shuffle.push[letter];
    }

    handleTrashcanReverted() {
        this.tiles.forEach(tile => tile.hidden = false);
        this.shuffle = [];
    }

}
module.exports = alt.createStore(RackStore, 'RackStore');