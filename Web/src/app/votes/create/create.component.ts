import {
  AfterViewInit,
  Component,
  ElementRef,
  OnInit,
  QueryList,
  Renderer2,
  ViewChild,
  ViewChildren
} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

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
  @ViewChild('endDatePicker') endDatePicker: ElementRef;
  @ViewChildren('choices') choiceInputs: QueryList<ElementRef>;
  @ViewChildren('members') memberInputs: QueryList<ElementRef>;
  @ViewChild('submitBtn') submitBtn: ElementRef;
  createVoteForm: FormGroup;
  isPending: boolean;
  isLimitedTime = false;
  isPrivate = false;
  isAnonymous = false;
  isIntermediaryResults = true;
  errorMessage: string;
  algoOptions = [
      {
        value : Algo.MAJORITY,
        name : 'Majoritaire'
      },
      {
        value : Algo.BORDA,
        name : 'Borda'
      },
      {
        value : Algo.STV,
        name : 'Scrutin à vote unique transférable'
      }
    ];

  choices = [{id: 1}, {id: 2}]
  members = [{id: 1}]

  private choicesLastId: number = 2;
  private membersLastId: number = 2;
  private emailRegExp = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);

  constructor(private fromBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private renderer: Renderer2) { }

  ngOnInit(): void {
    this.createVoteForm = this.fromBuilder.group({
      label: ['', [Validators.required]],
      endDate: ['', [Validators.required]],
      algoType: ['', [Validators.required, Validators.email]]
    });
  }

  ngAfterViewInit(): void {
    this.algoTypeSelector.nativeElement.value = this.algoOptions[0].value
    this.endDatePicker.nativeElement.disabled = true;
    this.intermediaryResultsToggle.nativeElement.checked = true;
    this.intermediaryResultsToggle.nativeElement.disabled = true;
  }

  setInvalidEmptyFields(): boolean {
    let EmptyFields: ElementRef[] = [];
    if (this.onLabelInputFocusOut()){
      EmptyFields.push(this.labelInput);
    }
    if (this.isLimitedTime && this.onDatePickerFocusOut()) {
      EmptyFields.push(this.endDatePicker);
    }
    this.choiceInputs.forEach((choiceInput, index) => {
      if (this.onChoiceInputFocusOut(index)){
        EmptyFields.push(choiceInput);
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

    if (!this.setInvalidEmptyFields()){
      const choices = [];
      this.choiceInputs.forEach((choiceInput) => {
        choices.push({name: choiceInput.nativeElement.value});
      });
      const vote = new VoteToSend(
          this.labelInput.nativeElement.value,
          new Date(),
          this.endDatePicker.nativeElement.value,
          this.algoTypeSelector.nativeElement.value,
          this.isAnonymous,
          this.isIntermediaryResults,
          choices,
          1
          );
      console.log(vote);
    }
  }

  addChoice(): void {
    this.choices.push({id: this.choicesLastId++});
  }

  removeChoice(i: number): void {
    this.choices.splice(i,1);
  }

  addMember(): void {
    this.members.push({id: this.membersLastId++});
  }

  removeMember(i: number): void {
    this.members.splice(i,1);
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

  onDatePickerFocusOut(): boolean {
    const today = (new Date()).setHours(0,0,0,0);
    const pickedDate = new Date(this.endDatePicker.nativeElement.value).setHours(0,0,0,0);
    if (this.isLimitedTime && (today >= pickedDate || !this.endDatePicker.nativeElement.value)) {
      this.renderer.addClass(this.endDatePicker.nativeElement, 'is-invalid');
      return true;
    } else {
      this.renderer.removeClass(this.endDatePicker.nativeElement, 'is-invalid');
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

  onAlgoTypeSelectorInput() {
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
}
export class VoteToSend {
  constructor(private label: string,
              private startDate: Date,
              private endDate: Date,
              private algo: Algo,
              private anonymous: boolean,
              private intermediaryResult: boolean,
              private choices: { names : string[] }[],
              private maxChoices: number) { }
}

enum Algo {
  MAJORITY = "majority",
  BORDA = "borda",
  STV = "stv",
}
