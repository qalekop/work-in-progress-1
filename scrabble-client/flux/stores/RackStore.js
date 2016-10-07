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
            handleTileDropped: Actions.TILE_DROPPED,
            handleTileReverted: Actions.TILE_REVERTED
        });
    }

    handleGetRack(letters) {
        this.letters = letters;
    }

    handleTileDropped(letter) {
        var index = this.letters.findIndex(tile => tile.letter == letter.letter);
        this.letters[index].hidden = true;
    }

    handleTileReverted(letter) {
        var index = this.letters.findIndex(tile => tile.hidden && tile.letter == letter.letter);
        if (index >= 0) this.letters[index].hidden = false;
    }

}
module.exports = alt.createStore(RackStore, 'RackStore');