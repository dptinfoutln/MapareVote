import { Component, OnInit } from '@angular/core';
import {VotesComponent} from '../../votes.component';
import {VotesService} from '../../../services/votes.service';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../../services/auth.service';
import {ErrorsService} from '../../../services/errors.service';

@Component({
  selector: 'app-started',
  templateUrl: '../../votes.component.html',
  styleUrls: ['./history-started.component.scss']
})
export class HistoryStartedComponent extends VotesComponent implements OnInit {

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
                this.votesService.getStartedVotes(this.pageNum, this.pageSize, this.orderBy, this.sortBy, this.nameLike, this.open).then(
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
