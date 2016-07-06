/**
 * Created by akopylov on 23.09.2015.
 */
var React = require('react');

var Actions = require('../flux/actions/Actions');
var GameStore = require('../flux/stores/GameStore');

const SIZE = 50;

var Cell = React.createClass({

    getInitialState() {
        return {letter: this.props.letter, occupied: this.props.state === 'ACCEPTED'}
    }

    , drop(event) {
        event.preventDefault();
        var letter = event.dataTransfer.getData('text');
        this.setState({letter: letter, occupied: true});
        Actions.tileDropped({'row': this.props.row,
            'col': this.props.col,
            'letter': letter});
    }

    , dragOver(event) {
        if (!this.state.occupied) event.preventDefault();
    }

    , rightClicked(event) {
        event.preventDefault();
        this.setState({letter: '', occupied: false});
        Actions.tileReverted({'row': this.props.row,
            'col': this.props.col,
            'letter': this.state.letter});
    }

    , render() {
        var row = this.props.row,
            col = this.props.col,
            className = 'cell '
                + (this.props.state == 'UNALLOWED' ? 'UNALLOWED' : '') + ' '
                + (this.props.bonus == 'NONE' ? '' : this.props.bonus);
        return (
            <div className={className}
                 onContextMenu={this.rightClicked}
                 onDragOver={this.dragOver}
                 style={{left: col * SIZE +'px', top: row * SIZE + 'px'}}
                 onDrop={this.drop}>{this.state.letter}</div>
        );
    }
});

var Cells = React.createClass({

    componentDidMount() {
        GameStore.getField();
    }

    , render() {
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
