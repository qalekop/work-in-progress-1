/**
 * Created by akopylov on 29.09.2015.
 */
const React = require('react');

const Cells = require('./Cells');
const Rack = require('./Rack');
const ControlPanel = require('./ControlPanel');
const ScrabbleDialog = require('./ScrabbleDialog');

const AltContainer = require('alt-container');
const RackStore = require('../flux/stores/RackStore');
const GameStore = require('../flux/stores/GameStore');
const ControlStore = require('../flux/stores/ControlStore');
const DialogStore = require('../flux/stores/DialogStore');

const Scrabble = React.createClass({
    render() {
        return(
            <div>
                <AltContainer store={GameStore}>
                    <Cells/>
                </AltContainer>
                <AltContainer store={RackStore}>
                    <Rack/>
                </AltContainer>
                <AltContainer store={ControlStore}>
                    <ControlPanel/>
                </AltContainer>
                <AltContainer store={DialogStore}>
                    <ScrabbleDialog/>
                </AltContainer>
            </div>
        )
    }
});
module.exports = Scrabble;