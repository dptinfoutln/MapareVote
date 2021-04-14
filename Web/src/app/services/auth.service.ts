import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';
import {Md5} from 'ts-md5';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient,
              private cookieService: CookieService
  ) { }

  createNewUser(email: string, password: string): Promise<void>{
    const url = 'https://maparevote.siannos.fr:6666/auth/signup';
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
    const url = 'https://maparevote.siannos.fr:6666/auth/signin';
    const body = JSON.stringify({email, password});
    const headers = new HttpHeaders({'Content-Type': 'application/json; charset=utf-8'});

    return new Promise(
      (resolve , reject) => {
        this.http.post(url, body, {headers} ).subscribe(
          () => {
            resolve();
          }, (error) => {
            reject(error);
          }
        );
      }
    );
  }

  signOutUser(email: string, password: string): Promise<void>{
    const url = 'https://maparevote.siannos.fr:6666/auth/signout';
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
