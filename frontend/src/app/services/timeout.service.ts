import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';

@Injectable()
export class SessionTimeoutService {
  timeout$: Subject<any> = new Subject();
  private _sessionExpiredDate = new Date();

  constructor(private http: HttpClient) {
    this.fetchAndSetTimeout();
  }

  private fetchAndSetTimeout() {
    this.http.get('session-timeout').subscribe(time => {
      let delay = +time - new Date().getTime();
      if (delay < 1000) {
        delay = 0;
      }
      setTimeout(() => this.timeout$.next(), delay);
      this._sessionExpiredDate.setTime(+time);
    });
  }

  get sessionExpiredDate() {
    const d = new Date();
    d.setTime(this._sessionExpiredDate.getTime());
    return d;
  }

}
