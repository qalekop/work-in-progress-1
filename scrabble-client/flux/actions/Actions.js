/**
 * Created by akopylov on 18/12/15.
 */

var alt = require('../../alt');

class Actions {
    getRack(rack) {
        // todo reconsider, since there's going to be one single umblella 'game state' object
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

    hideDialog() {
        this.dispatch();
    }

    handleResponse(response) {
        // todo do I really need it?
        this.dispatch(response);
    }

    //<editor-fold desc="Initial setup>
    bootstrapRequest() {
        this.dispatch();
    }

    wsReady() {
        this.dispatch();
    }
    //</editor-fold>
}
module.exports = alt.createActions(Actions);
