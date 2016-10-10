/**
 * Created by akopylov on 28/04/16.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions');
const ScrabbleStore = require('./ScrabbleStore');

class ControlStore {
    constructor() {
        this.enabled = false;

        this.bindListeners({
            handleTileDropped: Actions.TILE_DROPPED,
            handleTileReverted: Actions.TILE_REVERTED,
        });
    }

    handleTileDropped(tile) {
        var cell = ControlStore.findCell(tile.row, tile.col);
        if (!!cell) {
            cell.availability = 'OCCUPIED';
            cell.letter = tile.letter;
        }
        let enabled = ControlStore.checkField();
        this.enabled = enabled;
    }

    handleTileReverted(tile) {
        var cell = ControlStore.findCell(tile.row, tile.col);
        if (!!cell) cell.availability = 'ALLOWED'; // todo: rollback to the previous state
        let enabled = ControlStore.checkField();
        this.enabled = enabled;
    }

    static checkField() {
        return ScrabbleStore.getState().cells
            .some(function(cell) { return cell.availability == 'OCCUPIED';})
            // .forEach(function(cell) {console.log(`cell[${cell.row}][${cell.col}]=${cell.letter} (${cell.availability == 'OCCUPIED'})`)})
        ;
    }

    static findCell(row, col) {
        return ScrabbleStore.getState().cells.find(function(cell) {
            return cell.row == row && cell.col == col;
        })
    }
}

module.exports = alt.createStore(ControlStore, 'ControlStore');
