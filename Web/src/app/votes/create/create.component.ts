import {AfterViewInit, Component, ElementRef, OnInit, QueryList, Renderer2, ViewChild, ViewChildren} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {VotesService} from '../../services/votes.service';
import {Algo} from '../algo';
import {ErrorsService} from '../../services/errors.service';

@Component({
    selector: 'app-create',
    templateUrl: './create.component.html',
    styleUrls: ['./create.component.scss']
})
export class CreateComponent implements OnInit, AfterViewInit {
    @ViewChild('labelInput') labelInput: ElementRef;
    @ViewChild('endDateToggle') endDateToggle: ElementRef;
    @ViewChild('anonymousToggle') anonymousToggle: ElementRef;
    @ViewChild('privateToggle') privateToggle: ElementRef;
    @ViewChild('intermediaryResultsToggle') intermediaryResultsToggle: ElementRef;
    @ViewChild('algoType') algoTypeSelector: ElementRef;
    @ViewChild('startDatePicker') startDatePicker: ElementRef;
    @ViewChild('endDatePicker') endDatePicker: ElementRef;
    @ViewChild('maxChoice') maxChoice: ElementRef;
    @ViewChildren('choices') choiceInputs: QueryList<ElementRef>;
    @ViewChildren('members') memberInputs: QueryList<ElementRef>;
    @ViewChild('submitBtn') submitBtn: ElementRef;
    isPending: boolean;
    isLimitedTime = false;
    isPrivate = false;
    isAnonymous = false;
    isIntermediaryResults = true;
    isMaxChoice = true;
    errorMessage: string;
    selectedAlgoType = Algo.MAJORITY;
    algoOptions = [
        {
            value: Algo.MAJORITY,
            name: 'Majoritaire'
        },
        {
            value: Algo.BORDA,
            name: 'Borda'
        },
        {
            value: Algo.STV,
            name: 'Scrutin à vote unique transférable'
        }
    ];

    choices = [{id: 1}, {id: 2}];
    members = [{id: 1}];

    private choicesLastId = 2;
    private membersLastId = 2;
    private emailRegExp = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
    private today = new Date();

    constructor(private authService: AuthService,
                private votesService: VotesService,
                private router: Router,
                private renderer: Renderer2,
                private errorsService: ErrorsService) {
        this.today.setTime(this.today.getTime() + this.today.getTimezoneOffset() * 60 * 1000);
        this.today.setHours(0, 0, 0, 0);
    }

    ngOnInit(): void {

    }

    ngAfterViewInit(): void {
        this.algoTypeSelector.nativeElement.value = this.algoOptions[0].value;
        this.endDatePicker.nativeElement.disabled = true;
        this.intermediaryResultsToggle.nativeElement.checked = true;
        this.intermediaryResultsToggle.nativeElement.disabled = true;
        this.startDatePicker.nativeElement.value = (new Date()).toISOString().substring(0, 10);
    }

    setInvalidInvalidFields(): boolean {
        const EmptyFields: ElementRef[] = [];
        if (this.onLabelInputFocusOut()) {
            EmptyFields.push(this.labelInput);
        }
        if (this.isLimitedTime && this.onEndDatePickerFocusOut()) {
            EmptyFields.push(this.endDatePicker);
        }
        if (this.onStartDatePickerFocusOut()) {
            EmptyFields.push(this.startDatePicker);
        }
        this.choiceInputs.forEach((choiceInput, index) => {
            if (this.onChoiceInputFocusOut(index)) {
                EmptyFields.push(choiceInput);
            }
        });
        if (this.maxChoice) {
            if (this.onMaxChoiceInput()) {
                EmptyFields.push(this.maxChoice);
            }
        }
        this.memberInputs.forEach((memberInput, index) => {
            if (this.onMemberInputFocusOut(index)) {
                EmptyFields.push(memberInput);
            }
        });
        if (EmptyFields.length !== 0) {
            EmptyFields[0].nativeElement.focus();
            return true;
        } else {
            return false;
        }
    }

    onSubmit(): void {

        if (!this.setInvalidInvalidFields()) {
            this.isPending = true;
            this.submitBtn.nativeElement.disabled = true;
            const choices = [];
            const members = [];
            this.choiceInputs.forEach((choiceInput) => {
                choices.push({names: [choiceInput.nativeElement.value]});
            });
            this.memberInputs.forEach((memberInput) => {
                members.push({email: memberInput.nativeElement.value});
            });
            const startDate = new Date(this.startDatePicker.nativeElement.value);
            const endDate = new Date(this.endDatePicker.nativeElement.value);
            endDate.setHours(2, 0, 0, 0);
            let maxChoice = 1;
            if (this.maxChoice) {
                maxChoice = this.maxChoice.nativeElement.value;
            }
            const vote = new VoteToSend(
                this.labelInput.nativeElement.value,
                startDate,
                endDate,
                this.algoTypeSelector.nativeElement.value,
                this.isAnonymous,
                this.isIntermediaryResults,
                choices,
                members,
                maxChoice);
            // console.log(JSON.stringify(vote));
            this.votesService.sendVote(vote, !this.isPrivate).then(
                newVote => {
                    this.router.navigate(['/', 'votes', newVote.id]).then();
                }, err => {
                    this.errorsService.manageError(err);
                    this.submitBtn.nativeElement.disabled = false;
                    this.isPending = false;
                }
            );
        }
    }

    addChoice(): void {
        this.choices.push({id: this.choicesLastId++});
        this.setMaxChoiceMaxInput();
    }

    removeChoice(i: number): void {
        this.choices.splice(i, 1);
        this.setMaxChoiceMaxInput();
    }

    private setMaxChoiceMaxInput(): void {
        if (this.algoTypeSelector.nativeElement.value !== Algo.BORDA) {
            this.renderer.setProperty(this.maxChoice.nativeElement, 'max', this.choices.length);
        }
    }

    addMember(): void {
        this.members.push({id: this.membersLastId++});
    }

    removeMember(i: number): void {
        this.members.splice(i, 1);
    }

    onLabelInputFocusOut(): boolean {
        if (this.labelInput.nativeElement.value) {
            this.renderer.removeClass(this.labelInput.nativeElement, 'is-invalid');
            return false;
        } else {
            this.renderer.addClass(this.labelInput.nativeElement, 'is-invalid');
            return true;
        }
    }

    onEndDatePickerFocusOut(): boolean {
        const pickedDate = new Date(this.endDatePicker.nativeElement.value);
        pickedDate.setTime(pickedDate.getTime() + pickedDate.getTimezoneOffset() * 60 * 1000);
        pickedDate.setHours(0, 0, 0, 0);
        if (this.isLimitedTime && (this.today.getTime() >= pickedDate.getTime() || !this.endDatePicker.nativeElement.value)) {
            this.renderer.addClass(this.endDatePicker.nativeElement, 'is-invalid');
            return true;
        } else {
            this.renderer.removeClass(this.endDatePicker.nativeElement, 'is-invalid');
            return false;
        }
    }

    onStartDatePickerFocusOut(): boolean {

        const pickedDate = new Date(this.startDatePicker.nativeElement.value);
        pickedDate.setTime(pickedDate.getTime() + pickedDate.getTimezoneOffset() * 60 * 1000);
        pickedDate.setHours(0, 0, 0, 0);
        if (this.today.getTime() > pickedDate.getTime() || !this.startDatePicker.nativeElement.value) {
            this.renderer.addClass(this.startDatePicker.nativeElement, 'is-invalid');
            return true;
        } else {
            this.renderer.removeClass(this.startDatePicker.nativeElement, 'is-invalid');
            return false;
        }
    }

    onChoiceInputFocusOut(id: number): boolean {
        if (this.choiceInputs.get(id).nativeElement.value) {
            this.renderer.removeClass(this.choiceInputs.get(id).nativeElement, 'is-invalid');
            return false;
        } else {
            this.renderer.addClass(this.choiceInputs.get(id).nativeElement, 'is-invalid');
            return true;
        }
    }

    onMemberInputFocusOut(id: number): boolean {
        if (this.memberInputs.get(id).nativeElement.value && this.emailRegExp.test(this.memberInputs.get(id).nativeElement.value)) {
            this.renderer.removeClass(this.memberInputs.get(id).nativeElement, 'is-invalid');
            return false;
        } else {
            this.renderer.addClass(this.memberInputs.get(id).nativeElement, 'is-invalid');
            return true;
        }
    }

    onEndDateToggle(): void {
        this.isLimitedTime = !this.isLimitedTime;
        this.endDatePicker.nativeElement.disabled = !this.endDatePicker.nativeElement.disabled;
        if (!this.isLimitedTime) {
            this.intermediaryResultsToggle.nativeElement.checked = true;
            this.intermediaryResultsToggle.nativeElement.disabled = true;
            this.renderer.removeClass(this.endDatePicker.nativeElement, 'is-invalid');
        } else {
            this.intermediaryResultsToggle.nativeElement.disabled = false;
        }
    }

    onAnonymousToggle(): void {
        this.isAnonymous = !this.isAnonymous;
    }

    onPrivateToggle(): void {
        this.isPrivate = !this.isPrivate;
    }

    onIntermediaryResultsToggle(): void {
        this.isIntermediaryResults = !this.isIntermediaryResults;
    }

    onAlgoTypeSelectorInput(): void {
        this.selectedAlgoType = this.algoTypeSelector.nativeElement.value;
        this.isMaxChoice = this.algoTypeSelector.nativeElement.value !== Algo.BORDA;
        if (this.algoTypeSelector.nativeElement.value === Algo.STV) {
            this.isIntermediaryResults = false;
            this.intermediaryResultsToggle.nativeElement.checked = false;
            this.intermediaryResultsToggle.nativeElement.disabled = true;
            this.isLimitedTime = true;
            this.endDateToggle.nativeElement.checked = true;
            this.endDateToggle.nativeElement.disabled = true;
            this.endDatePicker.nativeElement.disabled = false;
        } else {
            this.endDateToggle.nativeElement.disabled = false;
            this.intermediaryResultsToggle.nativeElement.disabled = false;
        }
    }

    onMaxChoiceInput(): boolean {
        if (this.maxChoice.nativeElement.min <= this.maxChoice.nativeElement.value &&
            this.maxChoice.nativeElement.value <= this.maxChoice.nativeElement.max) {
            this.renderer.removeClass(this.maxChoice.nativeElement, 'is-invalid');
            return false;
        } else {
            this.renderer.addClass(this.maxChoice.nativeElement, 'is-invalid');
            return true;
        }
    }
}

class VoteToSend {
    constructor(public label: string,
                public startDate: Date,
                public endDate: Date,
                public algo: Algo,
                public anonymous: boolean,
                public intermediaryResult: boolean,
                public choices: { names: string[] }[],
                public members: { email: string }[],
                public maxChoices: number) {
    }
}

