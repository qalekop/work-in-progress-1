/**
 * Created by akopylov on 23.09.2015.
 */
var React = require('react');
var Actions = require('../flux/actions/Actions');

var SIZE = 7;
var Cell = React.createClass({

    getInitialState() {
        return {letter: '', occupied: false}
    }

    , drop(event) {
        event.preventDefault();
        var letter = event.dataTransfer.getData('text');
        this.setState({letter: letter, occupied: true});
        Actions.tileDropped(letter);
    }

    , dragOver(event) {
        if (!this.state.occupied) event.preventDefault();
    }

    , clicked(event) {
        event.preventDefault();
        this.setState({letter: '', occupied: false});
        Actions.tileReverted(this.state.letter);
    }

    , render() {
        var row = this.props.row,
            col = this.props.col,
            className = "cell " + ((row + col) % 2 ? "odd" : "even") + (" col_" + col) + (" row_" + row);
        return (
            <div className={className}
                 onContextMenu={this.clicked}
                 onDragOver={this.dragOver}
                 onDrop={this.drop}>{this.state.letter}</div>
        );
    }
});

var Cells = React.createClass({

    getInitialState() {
        var items = [];
        for (var row=0; row<SIZE; row++) {
            for (var column=0; column<SIZE; column++) {
                items.push({'row': row, 'column': column});
            }
        }
        return {'items': items};
    }

    , render() {
        return (
          <div className="gamefield">
              {this.state.items.map(cell => {
                  return <Cell row={cell.row} col={cell.column} key={cell.row + '-' + cell.column}/>
              })}
          </div>
        );
    }
});

module.exports = Cells;
