/**
 * Created by akopylov on 28/04/16.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions');

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
        this.enabled = true;
    }

    handleTileReverted(tile) {
        console.log(`*** ControlStore.tileReverted: ${tile.col}/${tile.row}`);
        this.enabled = false;
    }
}

module.exports = alt.createStore(ControlStore, 'ControlStore');