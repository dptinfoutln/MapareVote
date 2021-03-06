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
        if (this.authService.isStillAuth()) {
            this.authService.importSelf().then(user => {
                    this.selfUser = user;
                    this.votesService.getPublicVotes(this.pageNum, this.pageSize, this.orderBy, this.sortBy, this.nameLike, this.open).then(
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
        } else {
            this.votesService.getPublicVotes(this.pageNum, this.pageSize, this.orderBy, this.sortBy, this.nameLike, this.open).then(
                votes => {
                    this.manageVotes(votes);
                },
                err => {
                    this.errorsService.manageError(err);
                });
        }
    }
}
