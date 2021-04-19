import { Component, OnInit } from '@angular/core';
import { Vote} from '../../models/vote.model';
import { ActivatedRoute, Router } from '@angular/router';
import { VotesService } from '../../services/votes.service';
import { User } from '../../models/user.model';
import localeFr from '@angular/common/locales/fr';
import localeFrExtra from '@angular/common/locales/fr';
import {registerLocaleData} from '@angular/common';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

registerLocaleData(localeFr, 'fr-FR', localeFrExtra);

@Component({
  selector: 'app-vote',
  templateUrl: './vote.component.html',
  styleUrls: ['./vote.component.scss']
})
export class VoteComponent implements OnInit {

  vote: Vote;
  btnType = 'checkbox';
  choices: number[];
  voteForm: FormGroup;
  isLoaded = false;

  constructor(private fromBuilder: FormBuilder,
              private route: ActivatedRoute,
              private votesService: VotesService,
              private router: Router) { }

  ngOnInit(): void {
    this.vote = new Vote(-1, '', null, null, '', false, new User('', '', ''));
    const id = this.route.snapshot.params.id;
    this.initForm();
    this.votesService.getVote(+id).then(
      (vote: Vote) => {
        if (vote == null) {
          this.router.navigate(['/']);
        } else {
          this.vote = vote;
          this.isLoaded = true;
          switch (vote.algo) {
            case '1':
              this.btnType = 'radio';
              break;
            default :
              this.btnType = 'checkbox';
          }
        }
      }, err => {
        console.log(err);
        this.router.navigate(['/']);
      }
    );
  }

  initForm(): void {
    this.voteForm = this.fromBuilder.group({});
  }

  onBack(): void {
    this.router.navigate(['/votes', 'public']);
  }

  toggleChoice(id: number): void {
    if (this.choices.includes(id)){
      this.choices.splice(this.choices.indexOf(id), 1);
    } else {
      this.choices.push(id);
    }
    console.log(this.choices);
  }

  onSubmit(): void{
    const toSend = {
      date: new Date(),
      choices: [
        {
          choice:
            {
              id: 3,
              names: [
                'Non'
              ]
            },
          weight: 0
        }
      ]
    };
    const newBallot = this.votesService.sendBallot(this.vote.id, toSend);
    console.log(newBallot);
  }
}
