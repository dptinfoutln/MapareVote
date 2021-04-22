export class User {
  public id: number;
  public email: string;
  public lastname: string;
  public firstname: string;
  public confirmed: boolean;
  public admin: boolean;
  public banned: boolean;
  public startedVote;
  public privateVoteList;
  public votedVote;

  constructor() { }
}
