/**
 * Created by akopylov on 28/04/16.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions');
const GameStore = require('./GameStore');

class ControlStore {
    constructor() {
        this.enabled = false;

        this.bindListeners({
            handleTileDropped: Actions.TILE_DROPPED,
            handleTileReverted: Actions.TILE_REVERTED
        });
    }

    handleTileDropped(tile) {
        console.log(`*** ControlStore.tileDropped: ${tile.col}/${tile.row}`);
        this.enabled = zzz();
    }

    handleTileReverted(tile) {
        console.log(`*** ControlStore.tileReverted: ${tile.col}/${tile.row}`);
        this.enabled = zzz();
    }

    static zzz() {
        return GameStore.getState().cells
            .some(function(cell) { return cell.occupied && cell.availability == 'ALLOWED';})
            // .forEach(function(cell) {console.log(`cell[${cell.row}][${cell.col}]=${cell.letter} (${cell.availability})`)})
        ;
    }
}

module.exports = alt.createStore(ControlStore, 'ControlStore');