import { Component, OnInit } from '@angular/core';
import {VotesService} from '../../services/votes.service';
import {AuthUtilsService} from '../../services/auth-utils.service';
import {Vote} from '../../models/vote.model';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent implements OnInit {

  public votes = [];
  public isLoading: boolean;

  constructor(private authUtilsService: AuthUtilsService,
              private votesService: VotesService) { }

  ngOnInit(): void {
    if (this.votes.length !== this.authUtilsService.getSelfUser().votedVotes.length){
      this.isLoading = true;
      this.getHistory().then(votes => {
        this.isLoading = false;
        this.votes = votes;
      });
    }
  }

  async getHistory(): Promise<Vote[]> {
    const votes = [];
    for (const votedVote of this.authUtilsService.getSelfUser().votedVotes) {
      if (votedVote.vote.id){
        await this.votesService.getVote(votedVote.vote.id).then(vote => votes.push(vote));
      } else {
        await this.votesService.getVote(votedVote.vote).then(vote => votes.push(vote));
      }
    }
    return votes;
  }
}
