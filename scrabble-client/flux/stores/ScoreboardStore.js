/**
 * Created by akopylov on 11/11/2016.
 */

const alt = require('../../alt');

const Actions = require('../actions/Actions');

class ScoreboardStore {
    constructor() {
        this.score = {'human': 0, 'machine': 0};
        this.bindListeners({
            gandleGetScore: Actions.GET_SCORE,
        });
    }

    gandleGetScore(scores) {
        this.score.human = scores.human;
        this.score.machine = scores.machine;
    }
}

module.exports = alt.createStore(ScoreboardStore, 'ScoreboardStore');
