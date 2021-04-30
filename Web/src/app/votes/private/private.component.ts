import { Component, OnInit } from '@angular/core';
import {VotesService} from '../../services/votes.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-private',
  templateUrl: './private.component.html',
  styleUrls: ['./private.component.scss']
})
export class PrivateComponent implements OnInit {
    votes;
    isLoading;
    isEmpty;

    constructor(private votesService: VotesService,
                private router: Router) { }

    ngOnInit(): void {
        this.isLoading = true;
        this.isEmpty = false;
        this.votesService.getPrivateVotes().then(
            votes => {
                this.isLoading = false;
                this.votes = votes;
                if (this.votes.length === 0) {
                    this.isEmpty = true;
                }
                // console.log(votes);
            },
            error => {
                console.log(error);
            });
  }

}
