import {Component, OnInit} from '@angular/core';
import {VotesService} from '../../services/votes.service';
import {ActivatedRoute, Router} from '@angular/router';
import {environment} from '../../../environments/environment';
import {VotesComponent} from '../votes.component';
import {AuthUtilsService} from '../../services/auth-utils.service';
import {ErrorsService} from '../../services/errors.service';
import {AuthService} from '../../services/auth.service';

@Component({
    selector: 'app-private',
    templateUrl: '../votes.component.html',
    styleUrls: ['./private.component.scss']
})
export class PrivateComponent extends VotesComponent implements OnInit {

    constructor(votesService: VotesService,
                route: ActivatedRoute,
                authService: AuthService,
                errorsService: ErrorsService) {
        super (votesService, route, authService, errorsService);
    }

    loadVotes(): void {
        this.isLoading = true;
        this.authService.importSelf().then(user => {
                this.selfUser = user;
                this.votesService.getPrivateVotes(this.pageNum, this.pageSize, this.orderBy, this.sortBy, this.nameLike, this.open).then(
                    votes => {
                        this.manageVotes(votes);
                    },
                    err => {
                        this.errorsService.manageError(err);
                    });
            },
            err => {
                this.errorsService.manageError(err);
            }
        );
    }
}
