/**
 * Created by akopylov on 29.09.2015.
 */
var React = require('react');
var Cells = require('./Cells');
var Rack = require('./Rack');

var Scrabble = React.createClass({
    render() {
        return(
            <div>
                <Cells/>
                <Rack/>
            </div>
        )
    }
});
module.exports = Scrabble;