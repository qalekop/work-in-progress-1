/**
 * Created by akopylov on 29/08/16.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions');

class DialogStore {
    constructor() {
        this.modalShown = false;
        this.showCloseButton = false;
        this.text = '';

        this.bindListeners({
            showDialog: Actions.MAKE_MOVE,
            hideDialog: Actions.HIDE_DIALOG,
            proceedWithDialog: Actions.GET_FIELD
        });
    }

    hideDialog() {
        this.modalShown = false;
    }

    showDialog() {
        this.modalShown = true;
        this.showCloseButton = false;
        this.text = 'Please wait&hellip;';
    }

    proceedWithDialog(response) {
        if (!response.success) {
            console.log('*** Error:' + response.message);
            this.showCloseButton = true;
            this.text = response.message;
        } else {
            this.modalShown = false;
        }
    }
}

module.exports = alt.createStore(DialogStore, 'DialogStore');
