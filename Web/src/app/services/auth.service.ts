import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import {HttpClient} from '@angular/common/http';
import jwt_decode, {JwtPayload} from 'jwt-decode';

import { environment } from '../../environments/environment';
import { User } from '../models/user.model';
import {Subject} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  selfUserSubject = new Subject<User>();

  constructor(private http: HttpClient,
              private cookieService: CookieService) {
  }

  signUp(firstname: string, lastname: string, email: string, password: string): Promise<void>{
    const url = environment.apiURL + 'users';
    const body = JSON.stringify({firstname, lastname, email, password});
    const headers = environment.headers;

    return new Promise(
      (resolve , reject) => {
        this.http.post<void>(url, body, {headers} ).subscribe({
          next: () => {
            resolve();
          },
          error: err => {
            if (err.status === 0) {
              reject('Erreur 500 : Le serveur de ne répond pas');
            } else {
              reject('Erreur ' + err.status + ' : ' + err.error);
            }
          }
        });
      }
    );
  }

  signInUser(email: string, password: string): Promise<void>{
    const url = environment.apiURL + 'auth/signin';
    let headers = environment.headers;
    headers = headers.set('Accept', 'text/plain');
    headers = headers.set('Authorization', 'Basic ' + btoa(email + ':' + password));

    return new Promise(
      (resolve , reject) => {
        this.http.get(url, { headers, responseType: 'text' } ).subscribe(
          token => {
            this.setToken(token);
            this.importSelf().then( user => {
              resolve();
            });
          }, err => {
            console.log(err);
            if (err.status === 0) {
              reject('Erreur 500 : Le serveur ne répond pas');
            } else {
              reject('Erreur ' + err.status + ' : ' + err.error);
            }
          }
        );
      }
    );
  }

  signOutUser(): Promise<void> | void{
    this.removeToken();
    this.removeSelfUser();
    // this.isAuthSubject.next(false);
    // const url = environment.apiURL + 'auth/signout';
    // return new Promise(
    //   (resolve , reject) => {
    //     this.http.get(url).subscribe(
    //       () => {
    //         resolve();
    //       }, (error) => {
    //         reject(error);
    //       }
    //     );
    //   }
    // );
  }

  importSelf(): Promise<User> {
    const url = environment.apiURL + 'users/me';
    const headers = environment.headers.append('Authorization', 'Bearer ' + this.getToken());

    return new Promise(
      (resolve , reject) => {
        this.http.get<User>(url, { headers } ).subscribe(
          user => {
            this.setSelfUser(user);
            this.selfUserSubject.next(user);
            resolve(user);
          }, err => {
            console.log(err);
            reject();
          }
        );
      }
    );
  }

  getSelfUser(): User {
    if (this.cookieService.check('self')) {
      return JSON.parse(this.cookieService.get('self')) as User;
    }
    return null;
  }

  setSelfUser(user: User): void {
    this.removeSelfUser();
    this.cookieService.set('self', JSON.stringify(user), this.getTokenExpirationDate(this.getToken()), '/');
  }

  removeSelfUser(): void {
    while (this.cookieService.check('self')) {
      this.cookieService.delete('self', '/');
    }
  }

  getToken(): string {
    return this.cookieService.get('token');
  }

  setToken(token: string): void {
    this.removeToken();
    this.cookieService.set('token', token, this.getTokenExpirationDate(token), '/');
  }

  removeToken(): void {
    while (this.cookieService.check('token')) {
      this.cookieService.delete('token', '/');
    }
  }

  getTokenExpirationDate(token: string): Date {
    const decode = (jwt_decode(token)) as JwtPayload;

    if (decode.exp === undefined) { return null; }
    const date = new Date(0);

    date.setUTCSeconds(decode.exp);
    return date;
  }

  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) { return true; }

    const date = this.getTokenExpirationDate(token);
    if (date === undefined) { return false; }
    return !(date.valueOf() > new Date().valueOf());
  }

  getTokenInfo(): string {
    return (jwt_decode(this.getToken()));
  }

  isStillAuth(): boolean {
    let isAuth;
    if (this.isTokenExpired()) {
      this.signOutUser();
      isAuth = false;
      // TODO: check if not ban
    } else {
      isAuth = true;
    }
    return isAuth;
  }
}

export interface ISession {
  self: User;
}
