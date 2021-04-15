import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import jwt_decode, {JwtPayload} from "jwt-decode";
import { environment } from '../../environments/environment';
import {Observable, Subject} from "rxjs";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {resolve} from "@angular/compiler-cli/src/ngtsc/file_system";



@Injectable({
  providedIn: 'root'
})
export class AuthService{

  public isAuthSubject = new Subject<boolean>();

  constructor(private http: HttpClient,
              private cookieService: CookieService,
              private router: Router
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
            this.isAuthSubject.next(true);
            resolve();
          },
          error: error => {
            this.isAuthSubject.next(false);
            reject(error);
          }
        });
      }
    );
  }

  signInUser(email: string, password: string): Promise<void>{
    const url = environment.apiURL + 'auth/signin';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json; charset=utf-8',
      Accept: 'text/plain',
      Authorization: 'Basic ' + btoa(email + ':' + password)
    });

    return new Promise(
      (resolve , reject) => {
        this.http.get(url, { headers, responseType: 'text' } ).subscribe(
          token => {
            this.setToken(token);
            this.isAuthSubject.next(true);
            resolve();
          }, err => {
            this.isAuthSubject.next(false);
            if (err.status === 401){
              console.log(err);
              reject('L\'adresse email ou le mot de passe est incorrecte');
            } else {
              console.log(err);
              reject('Erreur ' + err.status);
            }
          }
        );
      }
    );
  }

  signOutUser(): Promise<void> | void{
    this.removeToken();
    this.isAuthSubject.next(false);
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

  getToken(): string {
    if (this.cookieService.check('token')) {
      return this.cookieService.get('token');
    }
    else {
      return null;
    }
  }

  setToken(token: string): void {
    this.cookieService.delete('token');
    this.cookieService.set('token', token);
  }

  removeToken(): void {
    this.cookieService.delete('token');
  }

  getTokenExpirationDate() {
    let decode = (jwt_decode(this.getToken())) as JwtPayload;

    if (decode.exp === undefined) return null;
    const date = new Date(0);

    date.setUTCSeconds(decode.exp);
    return date;
  }

  isTokenExpired(): boolean {

    if(!this.getToken()) return true;

    const date = this.getTokenExpirationDate();
    if(date === undefined) return false;
    return !(date.valueOf() > new Date().valueOf());
  }

  getInfo() {
    return (jwt_decode(this.getToken()))
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
    this.isAuthSubject.next(isAuth);
    return isAuth;
  }
}
