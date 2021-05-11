import { Component, OnInit } from '@angular/core';
import {VotesService} from '../../services/votes.service';
import {ActivatedRoute, Router} from '@angular/router';
import {environment} from '../../../environments/environment';
import {Vote} from '../../models/vote.model';
import {AuthUtilsService} from '../../services/auth-utils.service';

@Component({
  selector: 'app-private',
  templateUrl: './private.component.html',
  styleUrls: ['./private.component.scss']
})
export class PrivateComponent implements OnInit {
    votes = [];
    isLoading;
    isEmpty;
    votemakers = [];
    selfUser;

    pageNum = 1;
    pageSize = environment.defaultPageSize;
    orderBy = 'asc';
    sortBy;
    nameLike;
    open: boolean;

    constructor(private votesService: VotesService,
                private route: ActivatedRoute,
                private router: Router,
                private authUtils: AuthUtilsService) { }

    ngOnInit(): void {
        this.selfUser = this.authUtils.getSelfUser();
        this.route.queryParams.subscribe(params => {
            if (params.page_num){
                this.pageNum = Number(params.page_num);
            } else {
                this.pageNum = 1;
            }
            if (params.page_size){
                this.pageSize = Number(params.page_size);
            } else {
                this.pageSize = environment.defaultPageSize;
            }
            if (params.order){
                this.orderBy = params.order;
            } else {
                this.orderBy = 'asc';
            }
            this.open = params.open;
            this.sortBy = params.sort;
            this.nameLike = params.name;
            this.loadVotes();
        });

    }

    loadVotes(): void {
        this.isLoading = true;
        this.isEmpty = false;
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
            error => {
                console.log(error);
            });
    }

    getReversedOrder(): string {
        let order;
        if (this.orderBy === 'asc'){
            order = 'desc';
        } else {
            order = 'asc';
        }
        return order;
    }

    isVotedVote(vote: Vote): boolean {
        let isVoted = false;
        for (const votedVote of this.selfUser.votedVotes) {
            if (vote.id === votedVote.vote || vote.id === votedVote.vote.id) {
                isVoted = true;
                break;
            }
        }
        return isVoted;
    }
}
