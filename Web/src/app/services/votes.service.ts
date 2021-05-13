import {Injectable} from '@angular/core';
import {Vote} from '../models/vote.model';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {AuthService} from './auth.service';
import {Ballot} from '../models/ballot.model';
import {AuthUtilsService} from './auth-utils.service';
import {ErrorsService} from './errors.service';
import {Subject} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class VotesService {

    constructor(private http: HttpClient,
                private errorsService: ErrorsService,
                private authService: AuthService) {
    }

    public voteCount = new Subject();
    public pageCount = new Subject();

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

    setHttpParams(pageNum: number, pageSize: number, orderBy: string, open: boolean, sortBy ?: string, nameLike ?: string): HttpParams{
        let params = new HttpParams()
            .set('page_num', String(pageNum))
            .set('page_size', String(pageSize))
            .set('order', String(orderBy))
            .set('open', String(open));
        if (nameLike) {
            params = params.set('name_like', String(nameLike));
        }
        if (sortBy) {
            params = params.set('sort', String(sortBy));
        }
        return params;
    }


    getVotes(url: string, headers: HttpHeaders, params: HttpParams): Promise<Vote[]> {
        return new Promise(
            (resolve, reject) => {
                this.http
                    .get<any>(url, {headers, params, observe : 'response'}, )
                    .subscribe((response: HttpResponse<Vote[]>) => {
                        this.voteCount.next(response.headers.get('votecount'));
                        this.pageCount.next(response.headers.get('pagecount'));
                        resolve(response.body);
                    }, error => {
                        this.errorsService.manageError(error);
                        reject(error);
                    });
            });
    }

    getPublicVotes(pageNum = 1, pageSize = 25, orderBy = 'asc', sortBy ?: string, nameLike ?: string, open = true): Promise<Vote[]> {
        const url = environment.apiURL + 'votes/public';
        let headers = environment.headers;
        headers = headers.set('Accept', 'application/json');
        const params = this.setHttpParams(pageNum, pageSize, orderBy, open, sortBy, nameLike);

        return this.getVotes(url, headers, params);
    }

    getPrivateVotes(pageNum = 1, pageSize = 25, orderBy = 'asc', sortBy ?: string, nameLike ?: string, open = true): Promise<any> {
        const url = environment.apiURL + 'votes/private/invited';
        let headers = environment.headers;
        headers = headers.set('Authorization', 'Bearer ' + this.authService.utils.getToken());
        const params = this.setHttpParams(pageNum, pageSize, orderBy, open, sortBy, nameLike);
        return this.getVotes(url, headers, params);
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
