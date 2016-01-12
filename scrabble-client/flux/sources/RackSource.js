/**
 * Created by akopylov on 18/12/15.
 */

var Actions = require('../actions/Actions');
var RACK_URL = '/scrabble/game/rack/';

var RackSource = {
    getRack() {
        return {
            remote(state, letters) {
                console.log('*** Source.getRack ' + letters);
                return new Promise(function(resolve) {
                    $.ajax({
                        method: 'POST',
                        url: RACK_URL,
                        data: {'letters': letters},
                        success: function(data) { resolve(data); },
                        dataType: 'json'
                    });
                })
            }
            , local(state) { return null; }
            , success: Actions.getRack
        }
    }
};
module.exports = RackSource;
