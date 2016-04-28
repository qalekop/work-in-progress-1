/**
 * Created by akopylov on 21/01/16.
 */
var React = require('react');

var Actions = require('../flux/actions/Actions');

var ButtonGo = React.createClass({
    clicked(event) {
        if (this.props.enabled) Actions.makeMove();
    }

    , render() {
        let className = 'button' + (this.props.enabled ? '' : ' disabled');
        return(
            <div className={className}
                 onClick={this.clicked}>GO!
            </div>
        )
    }
});

var ControlPanel = React.createClass({
    getInitialState() {
        return {'enabled': false};
    }

    , componentDidMount(){
        ControlStore.listen(this.onChange);
    }

    , componentWillUnmount() {
        ControlStore.unlisten(this.onChange);
    }

    , onChange() {
        this.forceUpdate();
    }

    , render() {
        return (
            <div className="rack">
                <ButtonGo enabled={this.props.enabled}/>
            </div>
        )
    }
});
module.exports = ControlPanel;