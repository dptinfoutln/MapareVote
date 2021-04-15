import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { SignupComponent } from './auth/signup/signup.component';
import { SigninComponent } from './auth/signin/signin.component';
import { HeaderComponent } from './header/header.component';
import { AuthService} from './services/auth.service';
import { AuthGuardService} from './services/auth-guard.service';
import { VoteService } from './services/vote.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { RouterModule, Routes } from '@angular/router';
import {Md5} from 'ts-md5';
import { PublicComponent } from './vote/public/public.component';
import { PrivateComponent } from './vote/private/private.component';
import { CreateComponent } from './vote/create/create.component';

const appRoutes: Routes = [
  { path: 'auth/signup', component: SignupComponent },
  { path: 'auth/signin', component: SigninComponent },
  { path: 'votes/create', component: CreateComponent },
  { path: 'votes/private', component: PrivateComponent },
  { path: 'votes/public', component: PublicComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    SigninComponent,
    HeaderComponent,
    PublicComponent,
    PrivateComponent,
    CreateComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    AuthService,
    AuthGuardService,
    VoteService,
    CookieService,
    Md5
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
