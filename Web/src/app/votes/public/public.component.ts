import {Component, OnInit} from '@angular/core';
import {VotesService} from '../../services/votes.service';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {VotesComponent} from '../votes.component';
import {ErrorsService} from '../../services/errors.service';
import {HttpResponse} from '@angular/common/http';

@Component({
    selector: 'app-public',
    templateUrl: '../votes.component.html',
    styleUrls: ['./public.component.scss']
})
export class PublicComponent extends VotesComponent implements OnInit {

    constructor(votesService: VotesService,
                route: ActivatedRoute,
                authService: AuthService,
                errorsService: ErrorsService) {
        super (votesService, route, authService, errorsService);
    }

    loadVotes(): void {
        this.isLoading = true;
        this.votesService.getPublicVotes(this.pageNum, this.pageSize, this.orderBy, this.sortBy, this.nameLike, this.open).then(
            votes => {
                this.isLoading = false;
                if (votes.length > 0) {
                    this.votes = votes;
                } else {
                    this.votes = null;
                }
                votes.forEach(vote => {
                    if (vote.votemaker.id) {
                        this.votemakers.push(vote.votemaker);
                    }
                });
            },
            err => {
                this.errorsService.manageError(err);
            });
    }
}
