import { Component, OnInit } from '@angular/core';
import { zip } from 'rxjs';

import { AccountService } from '../..//services/account.service';
import { RestService } from '../../services/rest.service';
import { DeleteEntityModalService } from '../../services/modal.service';
import { NotificationService } from '../../services/notification.service';
import { Kind, AppMessage } from '../../model/app-message';


@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  pageTitle = 'Account settings';
  username: String;
  currency: String;
  showForm = false;
  constructor(private rest: RestService, private accountService: AccountService,
    private notifications: NotificationService, private ms: DeleteEntityModalService) { }

  ngOnInit() {
    zip(this.rest.getUsername(), this.accountService.loadAccount()).subscribe(value => {
      this.username = value[0].username;
      this.currency = value[1].currency;
      this.showForm = true;
    },
      err => this.notifications.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
  }

  onClickDeleteAccount() {
    this.ms.pushMessage(AppMessage.of(Kind.ACCOUNT_DELETE_WARN));
    this.ms.onConfirmDelete(this.onConfirmDeleteAccount.bind(this));
  }

  onConfirmDeleteAccount() {
    this.showForm = false;
    this.notifications.push(AppMessage.of(Kind.IN_PROGRESS));
    this.rest.deleteAccount().subscribe(() => {
      window.location.assign('/login');
    }, err => this.notifications.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
  }

}
