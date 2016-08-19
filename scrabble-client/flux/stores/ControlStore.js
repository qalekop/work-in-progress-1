/**
 * Created by akopylov on 28/04/16.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions');
const GameStore = require('./GameStore');

class ControlStore {
    constructor() {
        this.enabled = false;
        this.modalShown = false;

        this.bindListeners({
            handleTileDropped: Actions.TILE_DROPPED,
            handleTileReverted: Actions.TILE_REVERTED,

            handleToggleModal: Actions.TOGGLE_MODAL
        });
    }

    handleTileDropped(tile) {
        var cell = ControlStore.findCell(tile.row, tile.col);
        if (!!cell) cell.availability = 'OCCUPIED';
        this.enabled = ControlStore.checkField();
    }

    handleTileReverted(tile) {
        var cell = ControlStore.findCell(tile.row, tile.col);
        if (!cell) cell.availability = 'ALLOWED'; // todo: rollback to the previous state
        this.enabled = ControlStore.checkField();
    }

    handleToggleModal(type) {
        console.log('*** ControlStore.handleToggleModal ' + type);
        this.modalShown = !this.modalShown;
    }

    static checkField() {
        return GameStore.getState().cells
            .some(function(cell) { return cell.occupied && cell.availability == 'OCCUPIED';})
            // .forEach(function(cell) {console.log(`cell[${cell.row}][${cell.col}]=${cell.letter} (${cell.availability})`)})
        ;
    }

    static findCell(row, col) {
        return GameStore.getState().cells.find(function(cell) {
            return cell.row == row && cell.col == col;
        })
    }
}

module.exports = alt.createStore(ControlStore, 'ControlStore');