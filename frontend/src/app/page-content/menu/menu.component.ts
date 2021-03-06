import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent {

  constructor(private http: HttpClient) { }

  signOut() {
    this.http.post('logout', null, { headers: null, responseType: 'text' }).subscribe(() => {
      window.location.assign('login?logout');
    });
  }

}
