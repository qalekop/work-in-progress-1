/**
 * Created by akopylov on 29.09.2015.
 */
const React = require('react');

const Tile = React.createClass({
    drag(event) {
        event.dataTransfer.setData('text', this.props.letter);
    }

    , render() {
        return <div className="tile" draggable="true" onDragStart={this.drag}>
            {this.props.letter}
        </div>
    }
});

const Rack = React.createClass({
    render() {
        return (
            <div className="rack-container">
                {this.props.tiles
                    .filter(tile => !tile.hidden)
                    .map((tile, i) => { return <Tile letter={tile.letter} score={tile.score} key={i}/> })}
            </div>
        )
    }
});
module.exports = Rack;
