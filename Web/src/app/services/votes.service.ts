import { Injectable } from '@angular/core';
import { Vote } from '../models/vote.model';
import { Subject } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';
import {Ballot} from '../models/ballot.model';
import {JsonObject} from '@angular/compiler-cli/ngcc/src/packages/entry_point';

@Injectable({
  providedIn: 'root'
})
export class VotesService {

  constructor(private http: HttpClient,
              private authService: AuthService,
  ) { }

  votes: Vote[] = [];
  votesSubject = new Subject<Vote[]>();

  private static tmpTestVote(): JsonObject{
    return {
      id: 1,
      label: 'Oui ou Non ?',
      startDate: [
        2021,
        4,
        15
      ],
      endDate: [
        2021,
        5,
        23
      ],
      algo: '1',
      anonymous: false,
      deleted: false,
      votemaker: {
        id: 1,
        email: 'test@test.mail',
        lastname: 'TEST',
        firstname: 'test',
        emailToken: null,
        confirmed: true,
        admin: false,
        banned: false
      },
      choices: [
        {
          id: 1,
          names: [
            'Oui'
          ],
          vote: 1
        },
        {
          id: 2,
          names: [
            'Non'
          ],
          vote: 1
        }
      ],
      members: []
    };
  }

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
    const url = environment.apiURL + 'votes/public/' + id;
    const headers = environment.headers;
    headers.append(
      'Authorization', 'Bearer ' + this.authService.getToken()
    );

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
