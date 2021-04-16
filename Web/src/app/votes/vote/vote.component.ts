import { Component, OnInit } from '@angular/core';
import { Vote} from "../../models/vote.model";
import { ActivatedRoute, Router } from "@angular/router";
import { VotesService } from "../../services/votes.service";
import { User } from "../../models/user.model";
import localeFr from '@angular/common/locales/fr';
import localeFrExtra from '@angular/common/locales/fr';
import {registerLocaleData} from "@angular/common";
import {Ballot} from "../../models/ballot.model";
import {Choice} from "../../models/choise.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

registerLocaleData(localeFr, 'fr-FR', localeFrExtra)

@Component({
  selector: 'app-vote',
  templateUrl: './vote.component.html',
  styleUrls: ['./vote.component.scss']
})
export class VoteComponent implements OnInit {

  vote: Vote;
  btnType = 'checkbox';
  choices: Choice[];
  voteForm: FormGroup;
  choicesSel;
  choiceSelected: any;

  constructor(private fromBuilder: FormBuilder,
              private route: ActivatedRoute,
              private votesService: VotesService,
              private router: Router) { }

  ngOnInit(): void {
    this.vote = new Vote(-1, '', new Date(), new Date(), '', false, new User('', '', ''));
    let id = this.route.snapshot.params['id'];
    this.initForm();
    this.votesService.getVote(+id).then(
      (vote: Vote) => {
        this.vote = vote;
        switch (vote.algo){
          case "1":
            this.btnType = 'radio';
            break;
          default :
            this.btnType = 'checkbox';
        }

      }, err => {
        console.log(err);
        // this.router.navigate(['/']);
      }
    );
  }

  initForm(): void {
    this.voteForm = this.fromBuilder.group({
      choices: ['']
    });
  }

  onBack() {
    this.router.navigate(['/votes', 'public'])
  }

  getSelectedChoices(){
    this.choicesSel = this.vote.choices.find(choice => choice.id === this.choiceSelected)
  }

  onSubmit(){
    // const choicesValues = this.getSelectedChoices();
    // console.log(choicesValues);
    // this.choices.push(new Choice(1,['x', 'y'], 1))
    // let ballot = new Ballot(new Date(), this.vote, this.choices)
    // this.votesService.sendBallot(ballot);
  }
}
