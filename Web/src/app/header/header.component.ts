import {Component, OnInit} from '@angular/core';
import {AuthService} from '../services/auth.service';
import {Subscription} from 'rxjs';
import {AuthUtilsService} from '../services/auth-utils.service';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

    selfUser = this.authService.utils.getSelfUser();
    userName: string;

    constructor(public authService: AuthService) {
        if (this.selfUser) {
            this.userName = String(this.selfUser.firstname + ' ' + this.selfUser.lastname);
        }
        this.authService.selfUserSubject.subscribe(() => {
            this.selfUser = this.authService.utils.getSelfUser();
            if (this.selfUser !== null) {
                this.userName = String(this.selfUser.firstname + ' ' + this.selfUser.lastname);
            }
        });
    }

    ngOnInit(): void {

    }

    onSignOut(): void {
        this.authService.utils.signOutUser();
    }
}
