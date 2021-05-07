import {Component, OnInit} from '@angular/core';
import {VotesService} from '../../services/votes.service';
import {Router} from '@angular/router';
import {PageEvent} from '@angular/material/paginator';

@Component({
    selector: 'app-public',
    templateUrl: './public.component.html',
    styleUrls: ['./public.component.scss']
})
export class PublicComponent implements OnInit {

    votes;
    isLoading;
    votemakers = [];

    constructor(private votesService: VotesService,
                private router: Router) {
    }

    ngOnInit(): void {
        this.isLoading = true;
        this.votesService.getPublicVotes().then(
            votes => {
                this.isLoading = false;
                this.votes = votes;
                votes.forEach(vote => {
                    if (vote.votemaker.id) {
                        this.votemakers.push(vote.votemaker);
                    }
                });
            },
            error => {
                console.log(error);
            });
    }

    getVotemakerName(votemaker: any): string {
        let name = '';
        if (votemaker.id){
            this.votemakers.forEach(vm => {
                if (vm.id === votemaker.id){
                    name = vm.firstname + ' ' + vm.lastname;
                }
            });
        } else {
            this.votemakers.forEach(vm => {
                if (vm.id === votemaker){
                    name = vm.firstname + ' ' + vm.lastname;
                }
            });
        }
        return name;
    }
}
