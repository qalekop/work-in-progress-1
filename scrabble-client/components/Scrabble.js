/**
 * Created by akopylov on 29.09.2015.
 */
const React = require('react');

const Cells = require('./Cells');
const Rack = require('./Rack');
const ControlPanel = require('./ControlPanel');
const ScrabbleDialog = require('./ScrabbleDialog');
const Scoreboard = require('./Scoreboard');

const AltContainer = require('alt-container');
const RackStore = require('../flux/stores/RackStore');
const ControlStore = require('../flux/stores/ControlStore');
const DialogStore = require('../flux/stores/DialogStore');
const ScrabbleStore = require('../flux/stores/ScrabbleStore');
const ScoreboardStore = require('../flux/stores/ScoreboardStore');

const Actions = require('../flux/actions/Actions');

const Scrabble = React.createClass({
    componentDidMount(){
        Actions.bootstrapRequest();
    }

    , render() {
        return(
            <div>
                <AltContainer store={ScoreboardStore}>
                    <Scoreboard/>
                </AltContainer>
                <AltContainer store={ScrabbleStore}>
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