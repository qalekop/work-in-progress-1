/**
 * Created by akopylov on 23.09.2015.
 */
var React = require('react');

var Cells = React.createClass({displayName: "Cells",

    getInitialState() {
        var SIZE = 3,
            items = [];
        for (var row=0; row<SIZE; row++) {
            for (var column=0; column<SIZE; column++) {
                items.push({'row': row, 'column': column});
            }
        }
        return {'items': items};
    }

    , render() {
        var cells = this.state.items.map(function(cell){
            var row = cell.row,
                col = cell.column;
            var key = row + "-" + col;
            var className = "cell " + ((row + col) % 2 ? "odd" : "even") + (" col_" + col) + (" row_" + row);
            return (
                React.createElement("div", {className: className, key: key})
            );
        });
        return (
          React.createElement("div", {className: "gamefield"}, cells)
        );
    }
});

module.exports = Cells;
