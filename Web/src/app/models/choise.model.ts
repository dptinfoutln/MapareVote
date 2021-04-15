import {Vote} from './vote.model';

export class Choice {
  constructor(
    public values: string[],
    public vote: Vote
) {}
}
