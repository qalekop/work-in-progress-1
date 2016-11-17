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
            proceedWithDialog: Actions.HANDLE_RESPONSE
        });
    }

    hideDialog(forcibly = false) {
        if (forcibly || !this.showCloseButton) this.modalShown = false;
    }

    showDialog() {
        this.modalShown = true;
        this.showCloseButton = false;
        this.text = 'Please wait...';
    }

    proceedWithDialog(response) {
        this.showCloseButton = !response.success;
        if (!response.success) {
            this.text = response.message;
        }
    }
}

module.exports = alt.createStore(DialogStore, 'DialogStore');
