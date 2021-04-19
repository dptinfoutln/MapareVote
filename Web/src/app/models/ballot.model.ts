import {Vote} from './vote.model';
import {User} from './user.model';
import {Choice} from './choice.model';

export class Ballot {
  public id: number = null;
  public user: User = null;

  constructor(
    public date: Date,
    public vote: Vote,
    public choices: Choice[]
  ) {}
}
