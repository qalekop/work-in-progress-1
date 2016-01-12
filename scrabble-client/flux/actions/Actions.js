/**
 * Created by akopylov on 18/12/15.
 */

var alt = require('../../alt');

class Actions {
    getRack(rack) {
        this.dispatch(rack);
    }

    tileDropped(letter) {
        this.dispatch(letter);
    }

    tileReverted(letter) {
        this.dispatch(letter);
    }
}
module.exports = alt.createActions(Actions);
