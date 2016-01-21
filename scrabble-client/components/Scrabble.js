/**
 * Created by akopylov on 29.09.2015.
 */
var React = require('react');

var Cells = require('./Cells');
var Rack = require('./Rack');
var ControlPanel = require('./ControlPanel');

var AltContainer = require('alt/AltContainer');
var RackStore = require('../flux/stores/RackStore');

var Scrabble = React.createClass({
    render() {
        return(
            <div>
                <Cells/>
                <AltContainer store={RackStore}>
                    <Rack/>
                    <ControlPanel/>
                </AltContainer>
            </div>
        )
    }
});
module.exports = Scrabble;