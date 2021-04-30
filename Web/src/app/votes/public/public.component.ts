import { Component, OnInit } from '@angular/core';
import {VotesService} from '../../services/votes.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-public',
  templateUrl: './public.component.html',
  styleUrls: ['./public.component.scss']
})
export class PublicComponent implements OnInit {

    votes;
    isLoading;

    constructor(private votesService: VotesService,
                private router: Router) { }

    ngOnInit(): void {
        this.isLoading = true;
        this.votesService.getPublicVotes().then(
            votes => {
                this.isLoading = false;
                this.votes = votes;
                // console.log(votes);
            },
            error => {
                console.log(error);
            });
    }
}
