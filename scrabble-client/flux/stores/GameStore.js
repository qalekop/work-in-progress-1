/**
 * Created by akopylov on 21/01/16.
 */

var alt = require('../../alt');

var Actions = require('../actions/Actions');

class GameStore {
    constructor() {
        this.items = [];
        // init field with empty cells
        for (var row=0; row<SIZE; row++) {
            for (var column=0; column<SIZE; column++) {
                this.items.push({'row': row, 'column': column});
            }
        }
        this.bindListeners({
            handleMakeMove: Actions.MAKE_MOVE,
            handleTileDropped: Actions.TILE_DROPPED,
            handleTileReverted: Actions.TILE_REVERTED
        });
    }

    handleMakeMove() {
        // todo implement me
        console.log('*** GameStore.makeMove');
    }

    handleTileDropped(tile) {
        var index = this.items.findIndex(item => item.row == tile.row && item.column == tile.col);
        if (index >= 0) this.items[index].letter = tile.letter;
    }

    handleTileReverted(tile) {
        var index = this.items.findIndex(item => item.row == tile.row && item.column == tile.col);
        if (index >= 0) this.items[index].letter = null;
    }
}
module.exports = alt.createStore(GameStore, 'GameStore');