import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import {HttpClient} from '@angular/common/http';
import jwt_decode, {JwtPayload} from 'jwt-decode';

import { environment } from '../../environments/environment';
import { User } from '../models/user.model';
import {Subject} from 'rxjs';
import {ErrorsService} from './errors.service';
import {AuthUtilsService} from './auth-utils.service';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  selfUserSubject = new Subject<User>();

  constructor(private http: HttpClient,
              public utils: AuthUtilsService,
              private errorsService: ErrorsService,
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
    console.log(btoa(email + ':' + password));
    return new Promise(
      (resolve , reject) => {
        this.http.get(url, { headers, responseType: 'text' } ).subscribe(
          token => {
            this.utils.setToken(token);
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

  isStillAuth(): boolean {
    let isAuth;
    if (this.utils.isTokenExpired()) {
      this.utils.signOutUser();
      isAuth = false;
      // TODO: check if not ban
    } else {
      isAuth = true;
    }
    return isAuth;
  }

  importSelf(): Promise<User> {
    const url = environment.apiURL + 'users/me';
    const headers = environment.headers.append('Authorization', 'Bearer ' + this.utils.getToken());

    return new Promise(
      (resolve , reject) => {
        this.http.get<User>(url, { headers } ).subscribe(
          user => {
            this.utils.setSelfUser(user);
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
}
