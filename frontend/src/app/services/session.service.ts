import { Injectable } from '@angular/core';

@Injectable()
export class SessionService {
  private readonly SPRING_SESSION_TIMEOUT_MILLIS = this.minutesToMillis(10);
  private timeoutDate: Date;

  constructor() {
    if (localStorage.getItem('timeout')) {
      this.timeoutDate = new Date(+localStorage.getItem('timeout'));
    } else {
      const timeout = new Date().getTime() + this.SPRING_SESSION_TIMEOUT_MILLIS;
      localStorage.setItem('timeout', timeout.toString());
      this.timeoutDate = new Date(timeout);
    }
    setInterval(this.navigateToLoginPage.bind(this), 1000);
    console.log(`Session timeout: ${this.timeoutDate}`);
  }

  private minutesToMillis(minutes: number) {
    return minutes * 60 * 1000;
  }

  private navigateToLoginPage() {
    if (this.isSessionExpired()) {
      this.signout();
      window.location.assign('login');
    }
  }

  private isSessionExpired() {
    return localStorage.getItem('timeout') === null || this.timeoutDate <= new Date();
  }

  signout() {
    localStorage.removeItem('timeout');
    clearInterval();
  }

}
