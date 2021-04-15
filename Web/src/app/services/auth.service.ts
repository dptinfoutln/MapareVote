import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';
import {Md5} from 'ts-md5';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient,
              private cookieService: CookieService
  ) { }

  createNewUser(email: string, password: string): Promise<void>{
    const url = environment.apiURL + 'auth/signup';
    const body = JSON.stringify({email, password});
    const headers = new HttpHeaders({'Content-Type': 'application/json; charset=utf-8'});

    return new Promise(
      (resolve , reject) => {
        this.http.post<string>(url, body, {headers} ).subscribe({
          next: token => {
            this.cookieService.set( 'Token', token );
            resolve();
          },
          error: error => {
            reject(error);
          }
        });
      }
    );
  }

  signInUser(email: string, password: string): Promise<void>{
    const url = environment.apiURL + 'auth/login';
    const body = JSON.stringify({email, password});

    const headers = new HttpHeaders({
      'Content-Type': 'application/json; charset=utf-8',
      Accept: 'text/plain',
      Authorization: 'Basic ' + btoa(email + ':' + password)
    });

    return new Promise(
      (resolve , reject) => {
        this.http.get(url, { headers, responseType: 'text' } ).subscribe(
          token => {
            console.log('token', token);
            resolve();
          }, err => {
            if (err.status === 401){
              console.log(err);
              reject('L\'adresse email our le mot de passe est incorrecte');
            } else {
              console.log(err);
              reject('Erreur ' + err.status);
            }
          }
        );
      }
    );
  }

  signOutUser(email: string, password: string): Promise<void>{
    const url = environment.apiURL + 'auth/signout';
    return new Promise(
      (resolve , reject) => {
        this.http.get(url).subscribe(
          () => {
            resolve();
          }, (error) => {
            reject(error);
          }
        );
      }
    );
  }

  hashPassword(toHash: string): string {
    const md5 = new Md5();
    return (md5.appendStr(toHash).end()) as string;
  }
}
