import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild, ViewChildren} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  signInForm: FormGroup;
  errorMessage: string;
  isPending = false;
  @ViewChild('submitBtn') submitBtn: ElementRef;
  @ViewChild('emailInput') emailInput: ElementRef;
  @ViewChild('passwordInput') passwordInput: ElementRef;

  constructor(private fromBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private renderer: Renderer2) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.signInForm = this.fromBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit(): void {
    let focused = false;
    if (!this.signInForm.get('email').valid){
      this.renderer.addClass(this.emailInput.nativeElement, 'is-invalid');
      this.emailInput.nativeElement.focus();
      focused = true;
    } else {
      this.renderer.removeClass(this.emailInput.nativeElement, 'is-invalid');
    }
    if (!this.signInForm.get('password').valid ) {
      this.renderer.addClass(this.passwordInput.nativeElement, 'is-invalid');
      if (!focused){
        this.passwordInput.nativeElement.focus();
      }
      focused = true;
    } else {
      this.renderer.removeClass(this.passwordInput.nativeElement, 'is-invalid');
    }
    if (!focused){
      this.submitBtn.nativeElement.focus();
      const email = this.signInForm.get('email').value;
      const password = this.signInForm.get('password').value;
      this.submitBtn.nativeElement.disabled = true;
      this.isPending = true;
      this.authService.signInUser(email, password).then(
          () => {
            this.router.navigate(['/']);
          },
          (error) => {
            this.submitBtn.nativeElement.disabled = false;
            this.isPending = false;
            this.errorMessage = error;
          });
    }
  }

  onMailFocusOut(): void {
    if (this.signInForm.get('email').valid){
      this.renderer.removeClass(this.emailInput.nativeElement, 'is-invalid');
    } else {
      this.renderer.addClass(this.emailInput.nativeElement, 'is-invalid');
    }
  }

  onPasswordFocusOut(): void {
    if (this.signInForm.get('password').valid){
      this.renderer.removeClass(this.passwordInput.nativeElement, 'is-invalid');
    } else {
      this.renderer.addClass(this.passwordInput.nativeElement, 'is-invalid');
    }
  }
}
