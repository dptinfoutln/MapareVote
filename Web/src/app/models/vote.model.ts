import { User } from './user.model';
import { Choice } from './choice.model';

export class Vote {
  choices: Choice[] = [];
  results;

  constructor(
    public id: number,
    public label: string,
    public startDate: Date,
    public endDate: Date,
    public algo: string,
    public anonymous: boolean,
    public votemaker: User) {
  }
}
