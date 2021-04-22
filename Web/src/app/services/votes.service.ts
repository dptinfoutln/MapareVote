import { Injectable } from '@angular/core';
import {Vote} from '../models/vote.model';
import {Subject} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AuthService} from './auth.service';
import {environment} from '../../environments/environment';
import {Ballot} from '../models/ballot.model';

@Injectable({
  providedIn: 'root'
})
export class VotesService {

  constructor(private http: HttpClient,
              private authService: AuthService,
  ) { }

  votes: Vote[] = [];
  votesSubject = new Subject<Vote[]>();

  emitVotes(): void {
    this.votesSubject.next(this.votes);
  }

  createVote(): void{
    // TODO: Create Vote
  }

  getVotes(page: number): Vote[] {
    const url = environment.apiURL + 'votes';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json; charset=utf-8',
      Accept: 'text/plain'
    });

    this.http.get(url, { headers } ).subscribe();
    return [];
  }

  getVote(id: number): Promise<any>{
    const url = environment.apiURL + 'votes/' + id;
    let headers = environment.headers;
    headers = headers.set('Authorization', 'Bearer ' + this.authService.getToken());

    return new Promise(
      (resolve , reject) => {
        this.http.get(url, { headers } ).subscribe(
          vote => {
            resolve(vote);
          }, err => {
            reject(err);
          }
        );
      }
    );
  }

  sendBallot(voteId: number,
             ballot: { date: Date; choices: { weight: number; choice: { names: string[]; id: number } }[] }): Promise<Ballot>{
    const url = environment.apiURL + 'votes/' + voteId + '/ballots';
    const headers = environment.headers;
    headers.append(
      'Authorization', 'Bearer ' + this.authService.getToken()
    );
    return new Promise(
      (resolve , reject) => {
        this.http.post<Ballot>(url, ballot, {headers} ).subscribe({
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
