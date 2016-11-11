/**
 * Created by akopylov on 10/11/2016.
 */
const React = require('react');

const Scoreboard = React.createClass({

    render() {
        return (
            <div className="scoreboard-container">
                <div className="scoreboard-container__score scoreboard-container__score_human">
                    {this.props.score.human}
                </div>
                <div className="scoreboard-container__score scoreboard-container__score_machine">
                    {this.props.score.machine}
                </div>
            </div>
        );
    }

});

module.exports = Scoreboard;
