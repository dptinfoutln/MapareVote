import {Injectable} from '@angular/core';
import {User} from '../models/user.model';
import jwt_decode, {JwtPayload} from 'jwt-decode';
import {CookieService} from 'ngx-cookie-service';

@Injectable({
    providedIn: 'root'
})
export class AuthUtilsService {

    constructor(private cookieService: CookieService) {
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

        if (decode.exp === undefined) {
            return null;
        }
        const date = new Date(0);

        date.setUTCSeconds(decode.exp);
        return date;
    }

    isTokenExpired(): boolean {
        const token = this.getToken();
        if (!token) {
            return true;
        }

        const date = this.getTokenExpirationDate(token);
        if (date === undefined) {
            return false;
        }
        return !(date.valueOf() > new Date().valueOf());
    }

    getTokenInfo(): string {
        return (jwt_decode(this.getToken()));
    }

    signOutUser(): void {
        this.removeToken();
        this.removeSelfUser();
    }

}
