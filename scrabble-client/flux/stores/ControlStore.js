/**
 * Created by akopylov on 28/04/16.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions');
const ScrabbleStore = require('./ScrabbleStore');

class ControlStore {
    constructor() {
        this.goButtonEnabled = false;
        this.goButtonShown = true;

        this.bindListeners({
            handleMakeMove: Actions.MAKE_MOVE,
            handleTileDropped: Actions.TILE_DROPPED,
            handleTileReverted: Actions.TILE_REVERTED,
            handleTileDroppedToTrashcan: Actions.TILE_DROPPED_TO_TRASHCAN,
            handleTrashcanReverted: Actions.TRASHCAN_REVERTED,
            handleGetRack: Actions.GET_RACK,
        });
    }

    handleGetRack() {
        this.goButtonShown = true;
    }

    handleTileDroppedToTrashcan() {
        this.goButtonShown = false;
    }

    handleTrashcanReverted() {
        this.goButtonShown = true;
    }

    handleTileDropped(tile) {
        var cell = ControlStore.findCell(tile.row, tile.col);
        if (!!cell) {
            cell.availability = 'OCCUPIED';
            cell.letter = tile.letter;
        }
        this.goButtonEnabled = ControlStore.checkField();
    }

    handleTileReverted(tile) {
        var cell = ControlStore.findCell(tile.row, tile.col);
        if (!!cell) cell.availability = 'ALLOWED';
        this.goButtonEnabled = ControlStore.checkField();
    }

    handleMakeMove() {
        this.goButtonEnabled = false;
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
