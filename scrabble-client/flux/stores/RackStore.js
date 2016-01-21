/**
 * Created by akopylov on 18/12/15.
 */

var alt = require('../../alt');

var Actions = require('../actions/Actions');
var Source = require('../sources/RackSource');

class RackStore {
    constructor() {
        this.letters = []; // todo rename to 'tiles'
        this.bindListeners({
            handleGetRack: Actions.GET_RACK,
            handleTileDropped: Actions.TILE_DROPPED,
            handleTileReverted: Actions.TILE_REVERTED
        });
        this.exportAsync(Source);

        ////
        this.on('bootstrap', () => { console.log('--- bootstrap')});
        this.on('snapshot', () => { console.log('--- snapshot')});
        this.on('init', () => { console.log('--- init')});
        this.on('error', () => { console.log('--- error')});

    }

    handleGetRack(letters) {
        this.letters = letters;
    }

    handleTileDropped(letter) {
        var index = this.letters.findIndex(tile => tile.letter == letter.letter);
        //console.log('*** RackStore.tileDropped ' + letter + ' ' + index);
        this.letters[index].hidden = true;
    }

    handleTileReverted(letter) {
        var index = this.letters.findIndex(tile => tile.hidden && tile.letter == letter.letter);
        if (index >= 0) this.letters[index].hidden = false;
    }

}
module.exports = alt.createStore(RackStore, 'RackStore');