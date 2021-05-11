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
import {Algo} from '../algo';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';

registerLocaleData(localeFr, 'fr-FR', localeFrExtra);

@Component({
    selector: 'app-vote',
    templateUrl: './vote.component.html',
    styleUrls: ['./vote.component.scss']
})
export class VoteComponent implements OnInit {

    vote: Vote;
    btnType = 'hidden';
    choices: Choice[];
    isLoaded = false;
    isVoted = false;
    tokenStyle = 'blur';
    resultList = [];
    selfUser;
    voteToken: string;
    myBallot;
    results;
    isPending = false;
    isResults = false;

    @ViewChildren('choices') choicesElem: QueryList<ElementRef>;
    @ViewChild('submitBtn') submitBtn: ElementRef;

    private checkedBoxes = [];

    constructor(private route: ActivatedRoute,
                private votesService: VotesService,
                private location: Location,
                private router: Router,
                private authService: AuthService,
                private renderer: Renderer2) {
    }

    ngOnInit(): void {
        this.selfUser = this.authService.utils.getSelfUser();
        const id = this.route.snapshot.params.id;

        this.votesService.getVote(+id).then(
            (vote: Vote) => {
                if (vote == null) {
                    this.router.navigate(['/']);
                } else {
                    this.vote = vote;
                    this.choices = vote.choices;
                    this.isLoaded = true;
                    this.checkIfUserVoted();
                    // console.log('vote anonyme ? ', this.vote.anonymous);
                    if (this.isVoted && !this.vote.anonymous) {
                        this.setCheckedChoices(+id);
                    }
                    this.setChoiceBtnType();
                    this.votesService.getVoteResults(+id).then(
                        resultList => {
                            this.results = resultList;
                            if (this.results.length !== 0){
                                this.isResults = true;
                                this.setResults();
                            }
                        });
                }
            }, err => {
                console.error(err);
                this.router.navigate(['/']);
            }
        );
    }

    private setResults(): void {
        this.results.sort((a, b) => {
            return b.value - a.value;
        });
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

    setChoiceBtnType(): void {
        if (this.vote.algo === Algo.MAJORITY){
            if (this.vote.maxChoices === 1) {
                this.btnType = 'radio';
            } else {
                this.btnType = 'checkbox';
            }
        }
    }

    setCheckedChoices(voteId): void {
        this.votesService.getMyBallot(voteId).then(
            myBallot => {
                this.myBallot = myBallot;
                myBallot.choices.forEach(choice => {
                    this.choicesElem.forEach(elemChoice => {
                        if (Number(elemChoice.nativeElement.value) === choice.choice.id) {
                            this.renderer.setProperty(elemChoice.nativeElement, 'checked', true);
                        }
                    });
                });
            }
        );
    }

    onChecked(event: any): void {
        if (event.currentTarget.checked) {
            this.checkedBoxes.push(event.currentTarget);
            if (this.checkedBoxes.length > this.vote.maxChoices) {
                this.checkedBoxes[0].checked = false;
                this.checkedBoxes.splice(0, 1);
            }
        } else {
            this.checkedBoxes.splice(this.checkedBoxes.indexOf(event.currentTarget), 1);
        }
    }


    onSubmit(): void {
        this.isPending = true;
        this.submitBtn.nativeElement.disabled = true;
        const tmpChoices = [];

        if (this.vote.algo === Algo.MAJORITY){
            for (const choice of this.choicesElem){
                if (choice.nativeElement.checked){
                    tmpChoices.push({choice: {id: choice.nativeElement.value}, weight: 1});
                }
            }
        } else {
            this.choices.forEach((choice, index) => {
                tmpChoices.push({choice: {id: choice.id}, weight: this.choices.length - index});
            });
        }
        // console.log(tmpChoices);

        this.votesService.sendBallot(this.vote.id, {choices: tmpChoices}).then(
            ballot => {
                console.log(ballot);
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

    drop(event: CdkDragDrop<string[]>): void {
        moveItemInArray(this.choices, event.previousIndex, event.currentIndex);
    }

}
