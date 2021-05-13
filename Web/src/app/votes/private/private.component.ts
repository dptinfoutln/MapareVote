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
        this.votesService.getPrivateVotes(this.pageNum, this.pageSize, this.orderBy, this.sortBy, this.nameLike, this.open).then(
            votes => {
                this.isLoading = false;
                if (votes.length > 0) {
                    this.votes = votes;
                    votes.forEach(vote => {
                        if (vote.votemaker.id) {
                            this.votemakers.push(vote.votemaker);
                        }
                    });
                } else {
                    this.votes = null;
                }
            },
            err => {
                this.errorsService.manageError(err);
            });
    }

}
