import {Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
declare var $: any;

@Component({
  selector: 'app-error-popup',
  templateUrl: './error-popup.component.html',
  styleUrls: ['./error-popup.component.scss']
})
export class ErrorPopupComponent implements OnInit {

  constructor() { }

  @ViewChild('errorModal') errorModal: ElementRef;

  static showModal(): void {
    $('#errorModal').modal('show');
  }

  static setFourOhFour(): void {
    $('.modal-title').text('Erreur 404');
    $('.modal-body').text('Le contenu auquel vous essayez d\'accéder n\'existe pas');
  }

  static setFourOhOne(): void {
    $('.modal-title').text('Erreur 401');
    $('.modal-body').text('Un problème d\'authentification est survenue, veuillez vous reconnecter');
  }

  static setFiveOhOh(): void {
    $('.modal-title').text('Erreur 500');
    $('.modal-body').text('Le serveur de répond pas');
  }

  static setTitle(title: string): void {
    $('.modal-title').text(title);
  }

  static setBody(body: string): void {
    $('.modal-body').text(body);
  }

  ngOnInit(): void {}


}
