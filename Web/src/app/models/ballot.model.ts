import {Vote} from './vote.model';
import {User} from './user.model';
import {BallotChoice} from './BallotChoice.model';

export class Ballot {
    public id: number = null;
    public date: Date;
    public vote: Vote;
    public choices: BallotChoice[];

    constructor() {
    }
}
