import { Injectable } from '@angular/core';
import { Vote } from '../models/vote.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';
import { Ballot } from '../models/ballot.model';

@Injectable({
  providedIn: 'root'
})
export class VotesService {

  constructor(private http: HttpClient,
              private authService: AuthService ) { }

  sendPublicVote(vote): Promise<Vote>{
    const url = environment.apiURL + 'votes/public';
    let headers = environment.headers;
    headers = headers.set('Authorization', 'Bearer ' + this.authService.getToken());

    return new Promise(
        (resolve , reject) => {
          this.http.post<Vote>(url, vote, {headers} ).subscribe({
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

  getPublicVotes(pageNum?: number, pageSize?: number): Promise<any> {
    const url = environment.apiURL + 'votes/public';
    let headers = environment.headers;
    headers = headers.set('Accept', 'application/json');

    return new Promise(
        (resolve , reject) => {
          this.http.get<Vote[]>(url, { headers } ).subscribe(
              votes => {
                resolve(votes);
              }, err => {
                reject(err);
              }
          );
        }
    );
  }

    getPrivateVotes(pageNum?: number, pageSize?: number): Promise<any> {
        const url = environment.apiURL + 'votes/private/invited';
        let headers = environment.headers;
        headers = headers.set('Authorization', 'Bearer ' + this.authService.getToken());

        return new Promise(
            (resolve , reject) => {
                this.http.get<Vote[]>(url, { headers } ).subscribe(
                    votes => {
                        resolve(votes);
                    }, err => {
                        reject(err);
                    }
                );
            }
        );
    }

  getVote(id: number): Promise<Vote>{
    const url = environment.apiURL + 'votes/' + id;
    let headers = environment.headers;
    headers = headers.set('Authorization', 'Bearer ' + this.authService.getToken());

    return new Promise(
      (resolve , reject) => {
        this.http.get<Vote>(url, { headers } ).subscribe(
          vote => {
            resolve(vote);
          }, err => {
            reject(err);
          }
        );
      }
    );
  }

  getMyBallot(voteId: number): Promise<Ballot>{
    const url = environment.apiURL + 'votes/' + voteId + '/myballot';
    let headers = environment.headers;
    headers = headers.set('Authorization', 'Bearer ' + this.authService.getToken());

    return new Promise(
        (resolve , reject) => {
          this.http.get<Ballot>(url, { headers } ).subscribe(
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
             ballot: { date: Date; choices: { weight: number; choice: { names: string[]; id: number } }[] }): Promise<Ballot>{
    const url = environment.apiURL + 'votes/' + voteId + '/ballots';
    let headers = environment.headers;
    headers = headers.set('Authorization', 'Bearer ' + this.authService.getToken());

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
