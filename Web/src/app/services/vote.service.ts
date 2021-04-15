import { Injectable } from '@angular/core';
import { Vote } from '../models/vote.model';
import { Subject } from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  votes: Vote[] = [];
  votesSubject = new Subject<Vote[]>();

  constructor(private http: HttpClient,
              private cookieService: CookieService
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
}
