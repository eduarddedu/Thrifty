import { Injectable } from '@angular/core';
import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest,
    HttpHeaders,
    HttpXsrfTokenExtractor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable()
export class RestApiRequestInterceptor implements HttpInterceptor {
    private readonly xsrfHeaderName = 'X-XSRF-TOKEN';

    constructor(private xsrfTokenExtractor: HttpXsrfTokenExtractor) { }

    private headers = new HttpHeaders(
        {
            'Accept': 'application/json',
            'Content-Type': 'application/json; charset=utf-8'
        });

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (this.isRestApiRequest(req)) {
            req = req.clone({
                url: environment.apiUrl.concat(req.url), headers: this.headers
            });
        }
        if (req.method.match('POST|PUT|DELETE')) {
            req = req.clone({
                headers: req.headers.set(this.xsrfHeaderName, this.xsrfTokenExtractor.getToken())
            });
        }
        return next.handle(req);
    }

    private isRestApiRequest(req: HttpRequest<any>): boolean {
        return !req.url.match('session-timeout|logout');
    }
}
