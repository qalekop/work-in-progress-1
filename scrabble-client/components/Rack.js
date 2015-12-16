/**
 * Created by akopylov on 29.09.2015.
 */
var React = require('react');
var SIZE = 3;

var Tile = React.createClass({
    drag(event) {
        event.dataTransfer.setData('text', this.props.letter);
        console.log("onDragStart: " + this.props.letter);
        //event.stopPropagation();
    }

    , render() {
        return <div className="tile" draggable="true" onDragStart={this.drag}>{this.props.letter}</div>
    }
});

var Rack = React.createClass({
    getInitialState() {
        return {'letters': ['X', 'Y', 'Z']};
    }

    , componentDidMount() {
        console.log('*** component did mount');
        $.get('/game/rack/', function(result) {
            console.log(result);
        });
    }

    , render() {
        return (
            <div className="rack">
                {this.state.letters.map((letter, i) => { return <Tile letter={letter} key={letter}/> })}
            </div>
        )
    }
});
module.exports = Rack;
