import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SignupComponent} from './auth/signup/signup.component';
import {AuthGuardService} from './services/auth-guard.service';
import {SigninComponent} from './auth/signin/signin.component';
import {CreateComponent} from './votes/create/create.component';
import {HistoryVotedComponent} from './votes/history/voted/history-voted.component';
import {HistoryStartedComponent} from './votes/history/started/history-started.component';
import {PublicComponent} from './votes/public/public.component';
import {PrivateComponent} from './votes/private/private.component';
import {VoteComponent} from './votes/vote/vote.component';
import {ValidateComponent} from './auth/validate/validate.component';

const routes: Routes = [
    {path: 'auth/signup', component: SignupComponent},
    {path: 'auth/signin', component: SigninComponent},
    {path: 'votes/create', canActivate: [AuthGuardService], component: CreateComponent},
    {path: 'votes/history/voted', canActivate: [AuthGuardService], component: HistoryVotedComponent},
    {path: 'votes/history/started', canActivate: [AuthGuardService], component: HistoryStartedComponent},
    {path: 'votes/private', canActivate: [AuthGuardService], component: PrivateComponent},
    {path: 'votes/public', component: PublicComponent},
    {path: 'votes/:id', canActivate: [AuthGuardService], component: VoteComponent},
    {path: 'validate/:id/:token', component: ValidateComponent},
    {path: '', redirectTo: 'votes/public', pathMatch: 'full'},
    {path: '**', redirectTo: 'votes/public'},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
