import {Injectable} from '@angular/core';
import {Vote} from '../models/vote.model';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {AuthService} from './auth.service';
import {Ballot} from '../models/ballot.model';
import {AuthUtilsService} from './auth-utils.service';
import {ErrorsService} from './errors.service';

@Injectable({
    providedIn: 'root'
})
export class VotesService {

    constructor(private http: HttpClient,
                private errorsService: ErrorsService,
                private authService: AuthService) {
    }

    sendVote(vote, publique: boolean): Promise<Vote> {
        let url;
        if (publique) {
            url = environment.apiURL + 'votes/public';
        } else {
            url = environment.apiURL + 'votes/private';
        }
        let headers = environment.headers;
        headers = headers.set('Authorization', 'Bearer ' + this.authService.utils.getToken());

        return new Promise(
            (resolve, reject) => {
                this.http.post<Vote>(url, vote, {headers}).subscribe({
                    next: newVote => {
                        resolve(newVote);
                    },
                    error: error => {
                        reject(error);
                    }
                });
            }
        );
    }

    getPublicVotes(pageNum: number = 1, pageSize: number = 10): Promise<any> {
        const url = environment.apiURL + 'votes/public';
        let headers = environment.headers;
        headers = headers.set('Accept', 'application/json');

        const params = new HttpParams().set('page_num', String(pageNum));

        return new Promise(
            (resolve, reject) => {
                this.http.get<Vote[]>(url, {headers, params}).subscribe(
                    votes => {
                        resolve(votes);
                    }, err => {
                        reject(err);
                    });
            });
    }

    getVoteResults(voteId: number): Promise<any> {
        const url = environment.apiURL + 'votes/' + voteId + '/results';
        let headers = environment.headers;
        headers = headers.set('Authorization', 'Bearer ' + this.authService.utils.getToken());

        return new Promise(
            (resolve, reject) => {
                this.http.get<[]>(url, {headers}).subscribe(
                    voteResults => {
                        resolve(voteResults);
                    }, err => {
                        reject(err);
                    }
                );
            });
    }

    getPrivateVotes(pageNum?: number, pageSize?: number): Promise<any> {
        const url = environment.apiURL + 'votes/private/invited';
        let headers = environment.headers;
        headers = headers.set('Authorization', 'Bearer ' + this.authService.utils.getToken());
        return new Promise(
            (resolve, reject) => {
                this.http.get<Vote[]>(url, {headers}).subscribe(
                    votes => {
                        resolve(votes);
                    }, err => {
                        reject(err);
                    }
                );
            });
    }

    getVote(id: number): Promise<Vote> {
        const url = environment.apiURL + 'votes/' + id;
        let headers = environment.headers;
        headers = headers.set('Authorization', 'Bearer ' + this.authService.utils.getToken());

        return new Promise(
            (resolve, reject) => {
                this.http.get<Vote>(url, {headers}).subscribe(
                    vote => {
                        resolve(vote);
                    }, err => {
                        this.errorsService.manageError(err);
                        reject(err);
                    }
                );
            }
        );
    }

    getMyBallot(voteId: number): Promise<Ballot> {
        const url = environment.apiURL + 'votes/' + voteId + '/myballot';
        let headers = environment.headers;
        headers = headers.set('Authorization', 'Bearer ' + this.authService.utils.getToken());

        return new Promise(
            (resolve, reject) => {
                this.http.get<Ballot>(url, {headers}).subscribe(
                    ballot => {
                        resolve(ballot);
                    }, err => {
                        reject(err);
                    }
                );
            }
        );
    }

    sendBallot(voteId: number,
               ballot: { choices: { choice: { id: number }, weight: number }[] }): Promise<Ballot> {
        const url = environment.apiURL + 'votes/' + voteId + '/ballots';
        let headers = environment.headers;
        headers = headers.set('Authorization', 'Bearer ' + this.authService.utils.getToken());

        return new Promise(
            (resolve, reject) => {
                this.http.post<Ballot>(url, ballot, {headers}).subscribe({
                    next: newBallot => {
                        resolve(newBallot);
                    },
                    error: error => {
                        reject(error);
                    }
                });
            }
        );
    }
}
