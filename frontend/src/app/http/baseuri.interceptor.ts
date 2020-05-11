import { Injectable } from '@angular/core';
import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest,
    HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';


@Injectable()
export class BaseUriInterceptor implements HttpInterceptor {

    private apiUrl: string = environment.apiUrl;

    private headers = new HttpHeaders(
        {
            'Accept':  'application/json',
            'Content-Type' : 'application/json; charset=utf-8'
        });

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const request: HttpRequest<any> = req.clone({
            url: this.apiUrl.concat(req.url),
            headers: this.headers
        });
        return next.handle(request);
    }
}
