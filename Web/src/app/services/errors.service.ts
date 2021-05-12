import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {AuthService} from './auth.service';
import {AuthUtilsService} from './auth-utils.service';
import {ErrorPopupComponent} from '../error-popup/error-popup.component';

@Injectable({
    providedIn: 'root'
})
export class ErrorsService {

    constructor(private router: Router,
                private authUtilsService: AuthUtilsService) {
    }

    manageError(error: HttpErrorResponse): void {
        switch (error.status) {
            case 401:
                ErrorPopupComponent.setFourOhOne();
                this.authUtilsService.signOutUser();
                this.router.navigate(['/auth/signin']);
                ErrorPopupComponent.showModal();
                break;
            case 404:
                ErrorPopupComponent.setFourOhFour();
                ErrorPopupComponent.showModal();
                break;
            case 0 || 500:
                ErrorPopupComponent.setFiveOhOh();
                ErrorPopupComponent.showModal();
                break;
            default:
                ErrorPopupComponent.setTitle('Erreur ' + error.status);
                ErrorPopupComponent.setBody(error.error);
                ErrorPopupComponent.showModal();
                break;
        }
    }
}
