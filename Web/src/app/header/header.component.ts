import { Component, OnInit } from '@angular/core';
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  isAuth: boolean = this.authService.isStillAuth();
  private userInfo;
  // @ts-ignore
  public userName = "";

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.authService.isAuthSubject.subscribe(
      (isAuth: boolean) => {
        this.isAuth = isAuth;
        if (isAuth){
          this.userInfo = this.authService.getInfo();
          this.userName = this.userInfo.firstname + ' ' + this.userInfo.lastname
        }
      }
    )
  }

  onSignOut() {
    this.authService.signOutUser();
    this.ngOnInit();
  }
}
