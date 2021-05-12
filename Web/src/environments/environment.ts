import {HttpHeaders} from '@angular/common/http';

export const environment = {
    production: true,
    apiURL: 'https://api.maparevote.siannos.fr/',
    // apiURL: 'http://localhost:5431/',
    headers: new HttpHeaders({'Content-Type': 'application/json; charset=utf-8'}),
    defaultPageSize: 20
};
