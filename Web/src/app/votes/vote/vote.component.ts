import {Component, ElementRef, OnInit, QueryList, Renderer2, ViewChild, ViewChildren} from '@angular/core';
import {Location} from '@angular/common';
import {Vote} from '../../models/vote.model';
import {ActivatedRoute, Router} from '@angular/router';
import {VotesService} from '../../services/votes.service';
import {registerLocaleData} from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import localeFrExtra from '@angular/common/locales/fr';
import {AuthService} from '../../services/auth.service';
import {Choice} from '../../models/choice.model';

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
    isVoted = false;
    tokenStyle = 'blur';
    resultList = [];
    selfUser;
    voteToken: string;
    myBallot;
    results;
    isPending = false;
    isResults: false;

    @ViewChildren('choices') choicesElem: QueryList<ElementRef>;
    @ViewChild('submitBtn') submitBtn: ElementRef;

    constructor(private route: ActivatedRoute,
                private votesService: VotesService,
                private location: Location,
                private router: Router,
                private authService: AuthService,
                private renderer: Renderer2) {
    }

    ngOnInit(): void {
        this.selfUser = this.authService.getSelfUser();
        const id = this.route.snapshot.params.id;

        // this.router.events
        //     .pipe(filter(event => event instanceof NavigationEnd))
        //     .subscribe((event: NavigationEnd) => {
        //       this.previousUrl = event.url;
        //       console.log(this.previousUrl);
        //     });

        this.votesService.getVote(+id).then(
            (vote: Vote) => {
                if (vote == null) {
                    this.router.navigate(['/']);
                } else {
                    this.vote = vote;
                    this.isLoaded = true;
                    this.checkIfUserVoted();
                    // console.log('vote anonyme ? ', this.vote.anonymous);
                    if (this.isVoted && !this.vote.anonymous) {
                        this.setCheckedChoices(+id);
                    }
                    this.setChoiceBtnType(vote.maxChoices);
                  this.votesService.getVoteResults(+id).then(
                      resultList => {
                        this.resultList = resultList;
                        console.log(resultList);
                      });
                    // if (this.vote.resultList.length > 0){
                    //   this.setResults();
                    // }
                }
            }, err => {
                console.error(err);
                this.router.navigate(['/']);
            }
        );
    }

    private setResults(): void {
        this.results = this.vote.resultList;
        this.results.sort((a, b) => {
            return b.value - a.value;
        });
    }

    getChoiceById(id: number): Choice {
        for (const choice of this.vote.choices) {
            if (choice.id === id) {
                return choice;
            }
        }
        return null;
    }

    onBack(): void {
        this.location.back();
    }

    checkIfUserVoted(): void {
        for (const votedVote of this.selfUser.votedVotes) {
            if (this.vote.id === votedVote.vote || this.vote.id === votedVote.vote.id) {
                this.isVoted = true;
                this.voteToken = votedVote.token;
                break;
            }
        }
    }

    setChoiceBtnType(maxChoices): void {
        if (maxChoices === 1) {
            this.btnType = 'radio';
        } else {
            this.btnType = 'checkbox';
        }
    }

    setCheckedChoices(voteId): void {
        this.votesService.getMyBallot(voteId).then(
            myBallot => {
                this.myBallot = myBallot;
                myBallot.choices.forEach(choice => {
                    this.choicesElem.forEach(elemChoice => {
                        // tslint:disable-next-line:triple-equals
                        if (elemChoice.nativeElement.value == choice.choice.id) {
                            this.renderer.setProperty(elemChoice.nativeElement, 'checked', true);
                        }
                    });
                });
            }
        );
    }

    toggleChoice(id: number): void {
        if (this.btnType === 'checkbox') {
            if (this.choices.includes(id)) {
                this.choices.splice(this.choices.indexOf(id), 1);
            } else {
                this.choices.push(id);
            }
        } else {
            this.choices = [id];
        }
    }

    setBallotChoices(): void {
        const tmpChoices = [];
        for (const choiceElem of this.choicesElem) {
            if (choiceElem.nativeElement.checked) {

            }
        }
    }

    onSubmit(): void {
        this.isPending = true;
        this.submitBtn.nativeElement.disabled = true;
        const tmpChoices = [];
        // switch (this.vote.algo){
        //   case 'majority':
        //     this.setBallotChoices();
        //     break;
        //   default:
        //     break;
        // }


        this.choices.forEach((choiceId) => {
            this.vote.choices.forEach((choice) => {
                if (choiceId === choice.id) {
                    tmpChoices.push({
                        choice: {
                            id: choiceId,
                            names: choice.names,
                        },
                        weight: 1,
                        ballot: 0
                    });
                }
            });
        });
        const toSend = {
            id: 0,
            date: new Date(),
            choices: tmpChoices
        };
        this.votesService.sendBallot(this.vote.id, toSend).then(
            ballot => {
                this.authService.importSelf().then(
                    () => {
                        this.ngOnInit();
                    }
                );
            }, err => {
                this.submitBtn.nativeElement.disabled = false;
                this.isPending = false;
            }
        );
    }

    toggleToken(): void {
        if (this.tokenStyle === null) {
            this.tokenStyle = 'blur';
        } else {
            this.tokenStyle = null;
        }
    }


}
