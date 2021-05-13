export class User {
    public id: number;
    public email: string;
    public lastname: string;
    public firstname: string;
    public confirmed: boolean;
    public admin: boolean;
    public banned: boolean;
    public startedVotes;
    public privateVoteList;
    public votedVotes;

    constructor() {
    }
}
