/**
 * Created by akopylov on 18/12/15.
 */

var alt = require('../../alt');

var Actions = require('../actions/Actions');

class RackStore {
    constructor() {
        this.letters = []; // todo rename to 'tiles'
        this.bindListeners({
            handleGetRack: Actions.GET_RACK,
            handleTileDroppedToField: Actions.TILE_DROPPED,
            handleTileDroppedToTrashcan: Actions.TILE_DROPPED_TO_TRASHCAN,
            handleTileReverted: Actions.TILE_REVERTED,
            handleTrashcanReverted: Actions.TRASHCAN_REVERTED,
        });
        this.exportPublicMethods({
            getRest: this.getRest
        });
    }

    handleGetRack(letters) {
        this.letters = letters;
    }

    handleTileReverted(letter) {
        var index = this.letters.findIndex(tile => tile.hidden && tile.letter == letter.letter);
        if (index >= 0) {
            this.letters[index].hidden = false;
        }
    }

    getRest() {
        return this.getInstance().letters.filter(tile => !tile.hidden).reduce(function(prev, next) {return prev + next}, '');
    }

    handleTileDroppedToField(letter) {
        let index = this.letters.findIndex(tile => tile.letter == letter.letter);
        this.letters[index].hidden = true;
    }

    handleTileDroppedToTrashcan(letter) {
        let index = this.letters.findIndex(tile => tile.letter == letter);
        this.letters[index].hidden = true;
    }

    handleTrashcanReverted() {
        this.letters.forEach(tile => tile.hidden = false);
    }

}
module.exports = alt.createStore(RackStore, 'RackStore');