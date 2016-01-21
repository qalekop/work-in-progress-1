/**
 * Created by akopylov on 29.09.2015.
 */
var React = require('react');
var RackStore = require('../flux/stores/RackStore');

var Tile = React.createClass({
    drag(event) {
        event.dataTransfer.setData('text', this.props.letter);
        //event.stopPropagation();
    }

    , render() {
        return <div className="tile" draggable="true" onDragStart={this.drag}>
            {this.props.letter}
        </div>
    }
});

var Rack = React.createClass({
    componentDidMount() {
        RackStore.getRack(['Ё', 'Ё', '?'].reduce(function(prev, next) {return prev + next}, ''));
    },

    render() {
        return (
            <div className="rack">
                {this.props.letters
                    .filter(tile => !tile.hidden)
                    .map((tile, i) => { return <Tile letter={tile.letter} score={tile.score} key={i}/> })}
            </div>
        )
    }
});
module.exports = Rack;
