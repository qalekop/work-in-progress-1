/**
 * Created by akopylov on 21/01/16.
 */

var alt = require('../../alt');

var Actions = require('../actions/Actions');
var Source = require('../sources/RackSource');
var SIZE = 7;
class GameStore {
    constructor() {
        this.cells = [];
        // init field with empty cells
        for (var row=0; row<SIZE; row++) {
            for (var column=0; column<SIZE; column++) {
                this.cells.push({'row': row, 'column': column});
            }
        }
        this.bindListeners({
            handleMakeMove: Actions.MAKE_MOVE,
            handleTileDropped: Actions.TILE_DROPPED,
            handleTileReverted: Actions.TILE_REVERTED
        });
        this.exportAsync(Source);
    }

    handleMakeMove() {
        // todo implement me
        var field = this.cells
            .filter(function(cell) { return !!cell.letter;});
            //.forEach(function(cell) {console.log('* ' + cell.letter)});
        this.getInstance().makeMove(field);
    }

    handleTileDropped(tile) {
        var index = this.cells.findIndex(item => item.row == tile.row && item.column == tile.col);
        if (index >= 0) this.cells[index].letter = tile.letter;
    }

    handleTileReverted(tile) {
        var index = this.cells.findIndex(item => item.row == tile.row && item.column == tile.col);
        if (index >= 0) this.cells[index].letter = null;
    }
}
module.exports = alt.createStore(GameStore, 'GameStore');