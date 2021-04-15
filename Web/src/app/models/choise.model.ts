import {Vote} from './vote.model';

export class Choice {
  constructor(
    public id: number,
    public names: string[],
    public vote: number
  ) {}
}
