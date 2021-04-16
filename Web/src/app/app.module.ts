import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { SignupComponent } from './auth/signup/signup.component';
import { SigninComponent } from './auth/signin/signin.component';
import { HeaderComponent } from './header/header.component';
import { AuthService} from './services/auth.service';
import { VotesService } from './services/votes.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { RouterModule, Routes } from '@angular/router';
import { Md5 } from 'ts-md5';
import { PublicComponent } from './votes/public/public.component';
import { PrivateComponent } from './votes/private/private.component';
import { CreateComponent } from './votes/create/create.component';
import { VoteComponent } from './votes/vote/vote.component';
import { AuthGuardService } from "./services/auth-guard.service";

const appRoutes: Routes = [
  { path: 'auth/signup', component: SignupComponent },
  { path: 'auth/signin', component: SigninComponent },
  { path: 'votes/create', canActivate: [AuthGuardService], component: CreateComponent },
  { path: 'votes/private', canActivate: [AuthGuardService], component: PrivateComponent },
  { path: 'votes/public', canActivate: [AuthGuardService], component: PublicComponent },
  { path: 'votes/:id', canActivate: [AuthGuardService], component: VoteComponent },
  { path: '', redirectTo: 'votes/public', pathMatch: 'full'},
  { path: '**', redirectTo: 'votes/public' },
];

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    SigninComponent,
    HeaderComponent,
    PublicComponent,
    PrivateComponent,
    CreateComponent,
    VoteComponent
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
    VotesService,
    CookieService,
    Md5
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
