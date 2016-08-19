/**
 * Created by akopylov on 18/12/15.
 */

var alt = require('../../alt');

class Actions {
    getRack(rack) {
        this.dispatch(rack);
    }

    tileDropped(tile) {
        this.dispatch(tile);
    }

    tileReverted(letter) {
        this.dispatch(letter);
    }

    makeMove() {
        this.dispatch();
    }
    
    getField(response) {
        this.dispatch(response);
    }

    toggleModal(type) {
        this.dispatch(type);
    }
}
module.exports = alt.createActions(Actions);
