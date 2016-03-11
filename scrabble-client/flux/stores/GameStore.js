/**
 * Created by akopylov on 21/01/16.
 */

var alt = require('../../alt');

var Actions = require('../actions/Actions');
var GameSource = require('../sources/GameSource');

class GameStore {
    constructor() {
        this.cells = [];

         this.bindListeners({
            handleMakeMove: Actions.MAKE_MOVE,
            handleTileDropped: Actions.TILE_DROPPED,
            handleTileReverted: Actions.TILE_REVERTED,
            handleGetField: Actions.GET_FIELD
        });
        this.exportAsync(GameSource);
    }

    handleMakeMove() {
        var field = this.cells
            .filter(function(cell) { return cell.occupied;})
            //.forEach(function(cell) {console.log('cell[' + cell.row + '][' + cell.col + ']=' + cell.letter)})
            ;
        this.getInstance().makeMove(field);
    }

    handleTileDropped(tile) {
        var index = this.cells.findIndex(item => item.row == tile.row && item.col == tile.col);
        if (index >= 0) {
            this.cells[index].letter = tile.letter;
            this.cells[index].occupied = true;
        }
    }

    handleTileReverted(tile) {
        var index = this.cells.findIndex(item => item.row == tile.row && item.col == tile.col);
        if (index >= 0) {
            this.cells[index].letter = null; // excessive
            this.cells[index].occupied = false;
        }
    }

    handleGetField(cells) {
        this.cells = cells;
    }
}
module.exports = alt.createStore(GameStore, 'GameStore');