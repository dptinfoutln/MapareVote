import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {ErrorPopupComponent} from '../../error-popup/error-popup.component';

@Component({
  selector: 'app-validate',
  templateUrl: './validate.component.html',
  styleUrls: ['./validate.component.scss']
})
export class ValidateComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private authService: AuthService,
              private router: Router) { }

  ngOnInit(): void {
    const id = this.route.snapshot.params.id;
    const token = this.route.snapshot.params.token;

    this.authService.validateUser(id, token).then(
        () => {
          ErrorPopupComponent.setTitle('Création du compte confirmé');
          ErrorPopupComponent.setBody('Vous pouvez à présent vous connecter');
          ErrorPopupComponent.setSuccess();
          ErrorPopupComponent.showModal();
          this.authService.utils.signOutUser();
          this.router.navigate(['/auth/signin']);
        }, () => {
          this.authService.utils.signOutUser();
          this.router.navigate(['/auth/signin']);
        }
    );
  }

}
