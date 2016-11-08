/**
 * Created by akopylov on 18/12/15.
 */

const Actions = require('../actions/Actions');
const RACK_URL = '/scrabble/game/rack/';

var RackSource = {
    shuffleRack() {
        return {
            remote(state, rest, shuffle) {
                return new Promise(function(resolve) {
                    console.log("*** RackSource.getRest", shuffle);
                    $.ajax({
                        method: 'POST',
                        url: RACK_URL,
                        data: JSON.stringify({'rest': rest, 'shuffle': shuffle}),
                        dataType: 'json',
                        contentType: "application/json; charset=utf-8",
                        success: function(data) { resolve(data); },
                    });
                })
            }
            , local() { return null; }
            , success: Actions.getRack
        }
    }
};
module.exports = RackSource;
