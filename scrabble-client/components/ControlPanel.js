/**
 * Created by akopylov on 21/01/16.
 */
var React = require('react');

var Actions = require('../flux/actions/Actions');

const ButtonGo = React.createClass({
    getInitialState() {
        return {'hover': false};
    }

    , clicked(event) {
        if (this.props.enabled) {
            Actions.makeMove();
        }
    }
    
    , hover() {
        this.setState({'hover': !this.state.hover});
    }

    , render() {
        let className = 'control-panel__move-button'
            + (this.props.enabled
                ? (this.state.hover ? ' control-panel__move-button_hilighted' : '')
                : ' control-panel__move-button_disabled');
        return (
            <div className={className}
                 onMouseEnter={this.hover}
                 onMouseLeave={this.hover}
                 onClick={this.clicked}>GO!
            </div>
        )
    }
});

const ButtonShuffle = React.createClass({

    clicked(event) {
        if (this.props.enabled) {
            Actions.shuffle();
        }
    }

    , render() {
         return (
            <div className='control-panel__shuffle-button'
                 onClick={this.clicked}>Shuffle!
            </div>
        )
    }
});

const TrashCan = React.createClass({
    getInitialState() {
        return {'hover': false, 'empty': true};
    }

    , hover() {
        this.setState({'hover': !this.state.hover});
    }

    , clicked(event) {
        if (!this.state.empty) {
            this.setState({'empty': true});
            Actions.trashcanReverted();
        }
    }

    , dragOver(event) {
        event.preventDefault();
    }

    , drop(event) {
        event.preventDefault();
        let letter = event.dataTransfer.getData('text');
        this.setState({'empty': false});
        Actions.tileDroppedToTrashcan(letter);
    }

    , render() {
        let className = 'control-panel__trashcan'
            + (!this.state.empty
                ? (this.state.hover ? ' control-panel__trashcan_hilighted' : ' control-panel__trashcan_full')
                : '');
        return (
            <div className="control-panel__trashcan-button"
                onMouseEnter={this.hover}
                onMouseLeave={this.hover}
                onDragOver={this.dragOver}
                onDrop={this.drop}
                onClick={this.clicked}>
                <div className={className}/>
            </div>
        )
    }
});

const ControlPanel = React.createClass({

    render() {
        return this.props.moveButtonEnabled
            ? (
                <div className="controlpanel-container">
                    <ButtonGo enabled={this.props.enabled}/>
                    <TrashCan enabled="true"/>
                </div>
            )
            : (
                <div className="controlpanel-container">
                    <ButtonShuffle/>
                    <TrashCan enabled="true"/>
                </div>
            )
    }
});

module.exports = ControlPanel;