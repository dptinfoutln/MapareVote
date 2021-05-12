import {Component, OnInit} from '@angular/core';
import {VotesService} from '../../services/votes.service';
import {ActivatedRoute} from '@angular/router';
import {environment} from '../../../environments/environment';
import {Vote} from '../../models/vote.model';
import {AuthService} from '../../services/auth.service';

@Component({
    selector: 'app-public',
    templateUrl: './public.component.html',
    styleUrls: ['./public.component.scss']
})
export class PublicComponent implements OnInit {

    votes = [];
    isLoading;
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
                public authService: AuthService) {
    }

    ngOnInit(): void {
        if (this.authService.isStillAuth()) {
            this.selfUser = this.authService.utils.getSelfUser();
        }
        this.route.queryParams.subscribe(params => {
            if (params.page_num) {
                this.pageNum = Number(params.page_num);
            } else {
                this.pageNum = 1;
            }
            if (params.page_size) {
                this.pageSize = Number(params.page_size);
            } else {
                this.pageSize = environment.defaultPageSize;
            }
            if (params.order) {
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
            error => {
                console.log(error);
            });
    }

    getReversedOrder(): string {
        let order;
        if (this.orderBy === 'asc') {
            order = 'desc';
        } else {
            order = 'asc';
        }
        return order;
    }

    getVotemakerName(votemaker: any): string {
        let name = '';
        if (votemaker.id) {
            this.votemakers.forEach(vm => {
                if (vm.id === votemaker.id) {
                    name = vm.firstname + ' ' + vm.lastname;
                }
            });
        } else {
            this.votemakers.forEach(vm => {
                if (vm.id === votemaker) {
                    name = vm.firstname + ' ' + vm.lastname;
                }
            });
        }
        return name;
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
