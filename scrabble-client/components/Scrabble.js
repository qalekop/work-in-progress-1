/**
 * Created by akopylov on 29.09.2015.
 */
var React = require('react');
var Cells = require('./Cells');
var Rack = require('./Rack');
var AltContainer = require('alt/AltContainer');
var RackStore = require('../flux/stores/RackStore');

var Scrabble = React.createClass({
    render() {
        return(
            <div>
                <Cells/>
                <AltContainer store={RackStore}>
                    <Rack/>
                </AltContainer>
            </div>
        )
    }
});
module.exports = Scrabble;