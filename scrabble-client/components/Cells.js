/**
 * Created by akopylov on 23.09.2015.
 */
var React = require('react');

var Actions = require('../flux/actions/Actions');
var GameStore = require('../flux/stores/GameStore');

const SIZE = 50;

var Cell = React.createClass({

    getInitialState() {
        return {letter: '', occupied: false}
    }

    , drop(event) {
        event.preventDefault();
        var letter = event.dataTransfer.getData('text');
        // todo switch to Store.listen
        this.setState({letter: letter, occupied: true});
        Actions.tileDropped({'row': this.props.row,
            'col': this.props.col,
            'letter': letter});
    }

    , dragOver(event) {
        if (!this.state.occupied) event.preventDefault();
    }

    , clicked(event) {
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
                + this.props.state + ' '
                + (this.props.bonus == 'NONE' ? '' : this.props.bonus);
        return (
            <div className={className}
                 onContextMenu={this.clicked}
                 onDragOver={this.dragOver}
                 style={{left: col * SIZE +'px', top: row * SIZE + 'px'}}
                 onDrop={this.drop}>{this.state.letter}</div>
        );
    }
});

var Cells = React.createClass({

    componentDidMount() {
        console.log('*** Cells.didMount');
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
                               key={cell.row + '-' + cell.col}/>
              })}
          </div>
        );
    }
});

module.exports = Cells;
