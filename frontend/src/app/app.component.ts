import { Component, OnInit } from '@angular/core';
import { SessionTimeoutService } from './services/timeout.service';

@Component({
  selector: 'app-root',
  template: `<router-outlet></router-outlet>`
})
export class AppComponent implements OnInit {

  constructor(private service: SessionTimeoutService) {
  }

  ngOnInit() {
    const navigateToLogin = () => window.location.assign('login');
    this.service.timeout$.subscribe(navigateToLogin);
  }
}
