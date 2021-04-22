import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  signUpForm: FormGroup;
  errorMessage: string;
  errorMessageType = 'text-danger';

  constructor(private fromBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.signUpForm = this.fromBuilder.group({
      firstname: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      confirmPassword: ['']
    }, { validators: this.checkPasswords });
  }

  checkPasswords(group: FormGroup): { notSame: boolean } {
    const password = group.get('password').value;
    const confirmPassword = group.get('confirmPassword').value;

    return password === confirmPassword ? null : { notSame: true };
  }

  onSubmit(): void {
    const firstname = this.signUpForm.get('firstname').value;
    const lastname = this.signUpForm.get('lastname').value;
    const email = this.signUpForm.get('email').value;
    const password = this.signUpForm.get('password').value;
    this.authService.signUp(firstname, lastname, email, password).then(
      () => {
        this.errorMessageType = 'text-success';
        this.errorMessage = 'La création du compte à bien été prise en compte. Veuillez vous connecter';
      },
      (error) => {
        this.errorMessageType = 'text-danger';
        this.errorMessage = error;
      });
  }

  onFirstnameFocusOut(): void {

  }

  onLastnameFocusOut(): void {

  }

  onMailFocusOut(): void {

  }


}
