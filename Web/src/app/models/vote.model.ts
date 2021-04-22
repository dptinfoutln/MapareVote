import { User } from './user.model';
import { Choice } from './choice.model';
import {Result} from './result.model';

export class Vote {
  algo: string;
  anonymous: boolean;
  choices: Choice[];
  endDate: Date;
  id: number;
  intermediaryResult: true;
  label: string;
  maxChoices: number;
  members;
  resultList: Result[];
  startDate: Date;
  votemaker: User;


  constructor() {}
}
