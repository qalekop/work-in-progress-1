/**
 * Created by akopylov on 23.09.2015.
 */
const React = require('react');

const Actions = require('../flux/actions/Actions');
const ScrabbleStore = require("../flux/stores/ScrabbleStore");

const SIZE = 50;

const Cell = React.createClass({

    getInitialState() {
        return {'letter': this.props.letter, 'occupied': this.props.state !== 'AVAILABLE'};
    }

    , componentWillReceiveProps(nextProps) {
        this.setState({'letter': nextProps.letter, 'occupied': false});
    }

    , drop(event) {
        event.preventDefault();
        let letter = event.dataTransfer.getData('text');
        this.setState({letter: letter, occupied: true});
        Actions.tileDropped({'row': this.props.row,
            'col': this.props.col,
            'letter': letter});
    }

    , dragOver(event) {
        if (ScrabbleStore.getState().dropEnabled && !this.state.occupied) event.preventDefault();
    }

    , rightClicked(event) {
        event.preventDefault();
        this.setState({letter: '', occupied: false});
        Actions.tileReverted({'row': this.props.row,
            'col': this.props.col,
            'letter': this.state.letter});
    }

    , render() {
        let row = this.props.row,
            col = this.props.col,
            className = 'cell ';
        if (this.state.occupied) {
            className += this.props.state;
        } else {
            className += this.props.bonus == 'NONE' ? '' : this.props.bonus;
        }
        return (
            <div className={className}
                 onContextMenu={this.rightClicked}
                 onDragOver={this.dragOver}
                 style={{left: col * SIZE +'px', top: row * SIZE + 'px'}}
                 onDrop={this.drop}>{this.state.occupied ? this.state.letter : ''}</div>
        );
    }
});

var Cells = React.createClass({

    render() {
        if (!this.props.cells) return null;
        return (
          <div className="gamefield">
              {this.props.cells.map(cell => {
                  return <Cell row={cell.row}
                               col={cell.col}
                               bonus={cell.bonus}
                               state={cell.state}
                               letter={cell.letter}
                               key={cell.row + '-' + cell.col}/>
              })}
          </div>
        );
    }
});

module.exports = Cells;
