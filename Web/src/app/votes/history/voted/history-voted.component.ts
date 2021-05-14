import {Component, OnInit} from '@angular/core';
import {VotesService} from '../../../services/votes.service';
import {AuthUtilsService} from '../../../services/auth-utils.service';
import {Vote} from '../../../models/vote.model';
import {environment} from '../../../../environments/environment';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../../services/auth.service';
import {ErrorsService} from '../../../services/errors.service';
import {VotesComponent} from '../../votes.component';

@Component({
    selector: 'app-history',
    templateUrl: '../../votes.component.html',
    styleUrls: ['./history-voted.component.scss']
})
export class HistoryVotedComponent extends VotesComponent implements OnInit {

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
                this.votesService.getVotedVotes(this.pageNum, this.pageSize, this.orderBy, this.sortBy, this.nameLike, this.open).then(
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
