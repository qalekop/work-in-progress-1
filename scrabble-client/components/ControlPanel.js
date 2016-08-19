/**
 * Created by akopylov on 21/01/16.
 */
var React = require('react');

var Actions = require('../flux/actions/Actions');
var ControlStore = require('../flux/stores/ControlStore');

const ButtonGo = React.createClass({
    getInitialState() {
        return {'hover': false};
    }

    , clicked(event) {
        if (this.props.enabled) Actions.makeMove();
    }
    
    , hover() {
        this.setState({'hover': !this.state.hover});
    }

    , render() {
        let className = 'button' + (this.props.enabled ? (this.state.hover ? ' hilighted' : '') : ' disabled');
        return(
            <div className={className}
                 onMouseEnter={this.hover}
                 onMouseLeave={this.hover}
                 onClick={this.clicked}>GO!
            </div>
        )
    }
});

const ControlPanel = React.createClass({
    componentDidMount(){
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