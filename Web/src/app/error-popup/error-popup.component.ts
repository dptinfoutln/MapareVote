import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';

declare var $: any;

@Component({
    selector: 'app-error-popup',
    templateUrl: './error-popup.component.html',
    styleUrls: ['./error-popup.component.scss']
})
export class ErrorPopupComponent implements OnInit {
    private static isError = true;

    constructor() {
    }

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
        $('.modal-body').text('Vous n\'etes pas autorisé à accéder à ce contenu, veuillez vous connecter');
    }

    static setExpiredSession(): void {
        $('.modal-title').text('Erreur 401');
        $('.modal-body').text('Votre session à expiré, veuillez vous connecter');
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

    static setDanger(): void {
        this.isError = true;
    }

    static setSuccess(): void {
        this.isError = false;
        console.log('test');
    }

    isError(): boolean {
        return ErrorPopupComponent.isError;
    }

    ngOnInit(): void {
    }


}
