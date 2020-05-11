
import {catchError} from 'rxjs/operators';

import {throwError as observableThrowError,  Observable } from 'rxjs';
import { Injectable } from '@angular/core';

import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest,
    HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

    constructor () {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(catchError((err: HttpErrorResponse) => this.handleError(err)));
    }

    private handleError(err: HttpErrorResponse): Observable<HttpEvent<any>> {
        console.log(err);
        // TODO Send error to the backend, so it can be logged
        return observableThrowError(err);
    }
}
