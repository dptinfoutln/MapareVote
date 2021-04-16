import { Injectable } from '@angular/core';
import { Vote } from '../models/vote.model';
import { Subject } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { AuthService } from "./auth.service";
import {Ballot} from "../models/ballot.model";

@Injectable({
  providedIn: 'root'
})
export class VotesService {

  votes: Vote[] = [];
  votesSubject = new Subject<Vote[]>();

  constructor(private http: HttpClient,
              private authService: AuthService,
  ) { }

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
    )

    // return new Promise(
    //   (resolve , reject) => {
    //     setTimeout(
    //       () => {
    //         resolve(this.tmpTestVote());
    //       }, 1
    //     );
    //   }
    // );

    return new Promise(
      (resolve , reject) => {
        this.http.get(url, { headers } ).subscribe(
          vote => {
            resolve(vote);
          }, err => {
            reject(err)
          }
        );
      }
    );
  }

  sendBallot(ballot: Ballot): Promise<void>{
    const url = environment.apiURL + 'votes/' + ballot.vote.id + '/Ballots';
    const headers = environment.headers;
    headers.append(
      'Authorization', 'Bearer ' + this.authService.getToken()
    )
    return new Promise(
      (resolve , reject) => {
        this.http.post<Ballot>(url, ballot, {headers} ).subscribe({
          next: getBallot => {
            if (getBallot.id != ballot.id)
              reject('WTF ??!');
            else
              resolve();
          },
          error: error => {
            reject(error);
          }
        });
      }
    );
  }

  private tmpTestVote(){
    return {
      "id": 1,
      "label": "Oui ou Non ?",
      "startDate": [
        2021,
        4,
        15
      ],
      "endDate": [
        2021,
        5,
        23
      ],
      "algo": "1",
      "anonymous": false,
      "deleted": false,
      "votemaker": {
        "id": 1,
        "email": "test@test.mail",
        "lastname": "TEST",
        "firstname": "test",
        "emailToken": null,
        "confirmed": true,
        "admin": false,
        "banned": false
      },
      "choices": [
        {
          "id": 1,
          "names": [
            "Oui"
          ],
          "vote": 1
        },
        {
          "id": 2,
          "names": [
            "Non"
          ],
          "vote": 1
        }
      ],
      "members": []
    }
  }
}
