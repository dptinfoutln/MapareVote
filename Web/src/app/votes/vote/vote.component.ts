import { Component, OnInit } from '@angular/core';
import {Vote} from '../../models/vote.model';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {VotesService} from '../../services/votes.service';
import {User} from '../../models/user.model';
import {registerLocaleData} from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import localeFrExtra from '@angular/common/locales/fr';
import {filter} from 'rxjs/operators';

registerLocaleData(localeFr, 'fr-FR', localeFrExtra);

@Component({
  selector: 'app-vote',
  templateUrl: './vote.component.html',
  styleUrls: ['./vote.component.scss']
})
export class VoteComponent implements OnInit {

  vote: Vote;
  btnType = 'checkbox';
  choices: number[] = [];
  isLoaded = false;
  isVoted = true;
  tokenStyle = 'blur';
  previousUrl = '';

  constructor(private route: ActivatedRoute,
              private votesService: VotesService,
              private router: Router) { }

  ngOnInit(): void {
    this.vote = new Vote(-1, '', null, null, '', false, new User());
    const id = this.route.snapshot.params.id;

    this.router.events
        .pipe(filter(event => event instanceof NavigationEnd))
        .subscribe((event: NavigationEnd) => {
          this.previousUrl = event.url;
        });

    this.votesService.getVote(+id).then(
      (vote: Vote) => {
        if (vote == null) {
          this.router.navigate(['/']);
        } else {
          this.vote = vote;
          this.isLoaded = true;
          switch (vote.algo) {
            case 'majority':
              this.btnType = 'radio';
              break;
            default :
              this.btnType = 'checkbox';
          }
        }
      }, err => {
        console.error(err);
        this.router.navigate(['/']);
      }
    );
  }

  onBack(): void {
    console.log(this.previousUrl);
    this.router.navigate([this.previousUrl]);
  }

  toggleChoice(id: number): void {
    if (this.btnType === 'checkbox'){
      if (this.choices.includes(id)){
        this.choices.splice(this.choices.indexOf(id), 1);
      } else {
        this.choices.push(id);
      }
    } else {
      this.choices = [id];
    }
  }

  onSubmit(): void{
    const tmpChoices = [];
    this.choices.forEach((choiceId) => {
      this.vote.choices.forEach((choice) => {
        if (choiceId === choice.id){
          tmpChoices.push({
            choice: {
              id : choiceId,
              names: choice.names
            },
            weight: 0
          });
        }
      });
    });
    const toSend = {
      date: new Date(),
      choices: tmpChoices
    };
    const newBallot = this.votesService.sendBallot(this.vote.id, toSend);
    console.log(newBallot);
  }

  toggleToken(): void {
    if (this.tokenStyle === null) {
      this.tokenStyle = 'blur';
    } else {
      this.tokenStyle = null;
    }
  }
}
