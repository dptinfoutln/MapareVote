import {Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  signUpForm: FormGroup;
  errorMessage: string;
  errorMessageType = 'text-danger';
  isPending = false;

  @ViewChild('firstnameInput') firstnameInput: ElementRef;
  @ViewChild('lastnameInput') lastnameInput: ElementRef;
  @ViewChild('emailInput') emailInput: ElementRef;
  @ViewChild('passwordInput') passwordInput: ElementRef;
  @ViewChild('confirmPasswordInput') confirmPasswordInput: ElementRef;
  @ViewChild('submitBtn') submitBtn: ElementRef;

  constructor(private fromBuilder: FormBuilder,
              private authService: AuthService,
              private renderer: Renderer2) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.signUpForm = this.fromBuilder.group({
      firstname: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.checkPasswords });
  }

  checkPasswords(group: FormGroup): { notSame: boolean } {
    const password = group.get('password').value;
    const confirmPassword = group.get('confirmPassword').value;

    return password === confirmPassword ? null : { notSame: true };
  }

  onSubmit(): void {
    let focused = false;
    if (!this.signUpForm.get('firstname').valid){
      this.renderer.addClass(this.firstnameInput.nativeElement, 'is-invalid');
      this.firstnameInput.nativeElement.focus();
      focused = true;
    } else {
      this.renderer.removeClass(this.firstnameInput.nativeElement, 'is-invalid');
    }
    if (!this.signUpForm.get('lastname').valid ) {
      this.renderer.addClass(this.lastnameInput.nativeElement, 'is-invalid');
      if (!focused){
        this.lastnameInput.nativeElement.focus();
      }
      focused = true;
    } else {
      this.renderer.removeClass(this.lastnameInput.nativeElement, 'is-invalid');
    }
    if (!this.signUpForm.get('email').valid ) {
      this.renderer.addClass(this.emailInput.nativeElement, 'is-invalid');
      if (!focused){
        this.emailInput.nativeElement.focus();
      }
      focused = true;
    } else {
      this.renderer.removeClass(this.emailInput.nativeElement, 'is-invalid');
    }
    if (!this.signUpForm.get('password').valid ) {
      this.renderer.addClass(this.passwordInput.nativeElement, 'is-invalid');
      if (!focused){
        this.passwordInput.nativeElement.focus();
      }
      focused = true;
    } else {
      this.renderer.removeClass(this.passwordInput.nativeElement, 'is-invalid');
    }
    if (!this.signUpForm.get('confirmPassword').valid || this.checkPasswords(this.signUpForm)) {
      this.renderer.addClass(this.confirmPasswordInput.nativeElement, 'is-invalid');
      if (!focused){
        this.confirmPasswordInput.nativeElement.focus();
      }
      focused = true;
    } else {
      this.renderer.removeClass(this.confirmPasswordInput.nativeElement, 'is-invalid');
    }
    if (!focused) {
      const firstname = this.signUpForm.get('firstname').value;
      const lastname = this.signUpForm.get('lastname').value;
      const email = this.signUpForm.get('email').value;
      const password = this.signUpForm.get('password').value;
      this.submitBtn.nativeElement.disabled = true;
      this.isPending = true;
      this.authService.signUp(firstname, lastname, email, password).then(
          () => {
            this.errorMessageType = 'text-success';
            this.errorMessage = 'La création du compte à bien été prise en compte. Veuillez vous connecter';
          },
          (error) => {
            this.submitBtn.nativeElement.disabled = false;
            this.isPending = false;
            this.errorMessageType = 'text-danger';
            this.errorMessage = error;
          });
    }
  }

  onFirstnameFocusOut(): void {
    if (this.signUpForm.get('firstname').valid){
      this.renderer.removeClass(this.firstnameInput.nativeElement, 'is-invalid');
    } else {
      this.renderer.addClass(this.firstnameInput.nativeElement, 'is-invalid');
    }
  }

  onLastnameFocusOut(): void {
    if (this.signUpForm.get('lastname').valid){
      this.renderer.removeClass(this.lastnameInput.nativeElement, 'is-invalid');
    } else {
      this.renderer.addClass(this.lastnameInput.nativeElement, 'is-invalid');
    }
  }

  onMailFocusOut(): void {
    if (this.signUpForm.get('email').valid){
      this.renderer.removeClass(this.emailInput.nativeElement, 'is-invalid');
    } else {
      this.renderer.addClass(this.emailInput.nativeElement, 'is-invalid');
    }
  }

  onPasswordFocusOut(): void {
    if (this.signUpForm.get('password').valid){
      this.renderer.removeClass(this.passwordInput.nativeElement, 'is-invalid');
    } else {
      this.renderer.addClass(this.passwordInput.nativeElement, 'is-invalid');
    }
  }

  confirmPasswordFocusOut(): void {
    if (this.signUpForm.get('confirmPassword').valid && !this.checkPasswords(this.signUpForm)){
      this.renderer.removeClass(this.confirmPasswordInput.nativeElement, 'is-invalid');
    } else {
      this.renderer.addClass(this.confirmPasswordInput.nativeElement, 'is-invalid');
    }
  }

}
