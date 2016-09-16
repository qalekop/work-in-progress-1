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
const ScrabbleStore = require('../flux/stores/ScrabbleStore');

const Actions = require('../flux/actions/Actions');

const Scrabble = React.createClass({
    componentDidMount(){
        ScrabbleStore.listen(this.onChange);
        Actions.bootstrapRequest();
    }

    , componentWillUnmount() {
        ScrabbleStore.unlisten(this.onChange);
    }

    , render() {
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