import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { AppMessage, Kind } from '../model';
import { NotificationService } from '../services/notification.service';
import { SessionService } from '../services/session.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent {

  constructor(private http: HttpClient,
    private notification: NotificationService,
    private session: SessionService) { }

  signOut() {
    this.http.post('logout', null, { headers: null, responseType: 'text' })
      .subscribe(() => {
        this.session.signout();
        window.location.assign('login?logout');
      }, err => this.notification.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
  }

}
