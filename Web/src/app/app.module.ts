import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {CookieService} from 'ngx-cookie-service';
import {Md5} from 'ts-md5';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {DragDropModule} from '@angular/cdk/drag-drop';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {AuthService} from './services/auth.service';
import {AuthGuardService} from './services/auth-guard.service';
import {VotesService} from './services/votes.service';

import {SigninComponent} from './auth/signin/signin.component';
import {SignupComponent} from './auth/signup/signup.component';
import {CreateComponent} from './votes/create/create.component';
import {PrivateComponent} from './votes/private/private.component';
import {PublicComponent} from './votes/public/public.component';
import {VoteComponent} from './votes/vote/vote.component';


@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        SigninComponent,
        SignupComponent,
        CreateComponent,
        PrivateComponent,
        PublicComponent,
        VoteComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        DragDropModule
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
export class AppModule {
}
