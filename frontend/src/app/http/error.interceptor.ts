
import { catchError } from 'rxjs/operators';
import { throwError as observableThrowError, Observable } from 'rxjs';
import { Injectable } from '@angular/core';

import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest,
    HttpErrorResponse
} from '@angular/common/http';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(catchError((err: HttpErrorResponse) => this.handleHttpError(err)));
    }

    private handleHttpError(err: HttpErrorResponse): Observable<never> {
        if (err.status === 200 && err.url.endsWith('login')) {
            window.location.assign(err.url);
        } else {
            console.log(err);
        }
        return observableThrowError(err);
    }
}
