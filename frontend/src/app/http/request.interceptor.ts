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
export class RestRequestInterceptor implements HttpInterceptor {
    private readonly XSRF_HEADER_NAME = 'X-XSRF-TOKEN';
    private readonly XSRF_COOKIE_NAME = 'XSRF-TOKEN';

    constructor(private xsrfTokenExtractor: HttpXsrfTokenExtractor) { }

    private headers = new HttpHeaders(
        {
            'Accept': 'application/json',
            'Content-Type': 'application/json; charset=utf-8'
        });

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (this.isRestRequest(req)) {
            req = req.clone({
                url: environment.apiUrl.concat(req.url), headers: this.headers
            });
        }
        if (req.method.match('POST|PUT|DELETE')) {
            const xsrfTokenValue = this.xsrfTokenExtractor.getToken() === null ?
                CookieReader.get(this.XSRF_COOKIE_NAME) : this.xsrfTokenExtractor.getToken();
            req = req.clone({
                headers: req.headers.set(this.XSRF_HEADER_NAME, xsrfTokenValue),
            });
        }
        return next.handle(req);
    }

    private isRestRequest(req: HttpRequest<any>): boolean {
        return !req.url.match('logout');
    }
}

class CookieReader {
    static get(cname: string): string {
        const name = cname + '=';
        const decodedCookie = decodeURIComponent(document.cookie);
        const ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            const c = ca[i].replace(/^\s+/gm, '');
            if (c.indexOf(name) === 0) {
                return c.substr(name.length);
            }
        }
    }
}
