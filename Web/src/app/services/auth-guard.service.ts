import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {AuthService} from "./auth.service";


@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{
  constructor(private router: Router,
              private authService: AuthService) {
  }
  canActivate(): Observable<boolean> | Promise<boolean> | boolean {
    if (this.authService.isStillAuth()){
      return true;
    } else {
      this.router.navigate(['/auth', 'signin']);
      return false;
    }
  }
}
