import { User } from './user.model';
import { Choice } from './choise.model';

export class Vote {
  choice: Choice[] = [];
  results;

  constructor(
    public id: number,
    public label: string,
    public startDate: Date,
    public endDate: Date,
    public algo: string,
    public anonymous: boolean,
    public VoteMaker: User) {
  }
}
