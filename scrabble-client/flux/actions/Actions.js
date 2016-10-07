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
    
    getField(cells) {
        this.dispatch(cells);
    }

    hideDialog() {
        this.dispatch();
    }

    handleResponse(response) {
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
