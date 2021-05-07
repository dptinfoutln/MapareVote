import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthService} from './auth.service';
import {ErrorPopupComponent} from '../error-popup/error-popup.component';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(private router: Router,
              private authService: AuthService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.authService.isStillAuth()){
      return true;
    } else {
      ErrorPopupComponent.setFourOhOne();
      ErrorPopupComponent.showModal();
      this.authService.utils.signOutUser();
      this.router.navigate(['/auth', 'signin']);
      return false;
    }
  }
}
