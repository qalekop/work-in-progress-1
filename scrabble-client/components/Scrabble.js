/**
 * Created by akopylov on 29.09.2015.
 */
const React = require('react');

const Cells = require('./Cells');
const Rack = require('./Rack');
const ControlPanel = require('./ControlPanel');
const ScModal = require('./ScModal');

const AltContainer = require('alt-container');
const RackStore = require('../flux/stores/RackStore');
const GameStore = require('../flux/stores/GameStore');
const ControlStore = require('../flux/stores/ControlStore');

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
                    <ScModal/>
                </AltContainer>
            </div>
        )
    }
});
module.exports = Scrabble;