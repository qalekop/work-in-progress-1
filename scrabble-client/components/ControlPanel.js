/**
 * Created by akopylov on 21/01/16.
 */
var React = require('react');

var Actions = require('../flux/actions/Actions');

var ButtonGo = React.createClass({
    clicked(event) {
        Actions.makeMove();
    }

    , render() {
        return(
            <div className="button"
                 onClick={this.clicked}>GO!
            </div>
        )
    }
});

var ControlPanel = React.createClass({
    render() {
        return (
            <div className="rack">
                <ButtonGo/>
            </div>
        )
    }
});
module.exports = ControlPanel;