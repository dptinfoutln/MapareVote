import { Component, OnInit } from '@angular/core';
import {AuthService} from '../services/auth.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  userName: string;

  constructor( public authService: AuthService ) {
    const selfUser = this.authService.getSelfUser();
    if (selfUser !== null){
      this.userName = String(selfUser.firstname + ' ' + selfUser.lastname);
    }
  }

  ngOnInit(): void {

  }

  onSignOut(): void {
    this.authService.signOutUser();
  }
}
