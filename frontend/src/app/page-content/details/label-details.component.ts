import { switchMap } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Expense, Account, Label } from '../../model';
import { RestService } from '../../services/rest.service';
import { MessageService, Kind, Message } from '../../services/messages.service';
import { Utils } from '../../util/utils';
import { AnalyticsService } from '../../services/analytics.service';

@Component({
  templateUrl: './details.component.html'
})
export class LabelDetailsComponent implements OnInit {

  labelId: number;

  label: Label;

  activeSince: Date;

  showNotification = false;

  showModal = false;

  notificationMessage: Message;

  modalMessage: Message;

  dataReady = false;

  viewType = 'label';

  constructor(
    private rest: RestService,
    private analytics: AnalyticsService,
    private messages: MessageService,
    private route: ActivatedRoute,
    private router: Router,
    private ms: MessageService) {
  }

  ngOnInit(): void {
    this.route.paramMap.pipe(switchMap(params => {
      this.labelId = +params.get('id');
      return this.analytics.loadAccount();
    })).subscribe(this.init.bind(this), err => {
      this.showNotification = true;
      this.notificationMessage = this.messages.get(Kind.WEB_SERVICE_OFFLINE);
    });
  }

  private init(account: Account) {
    this.label = account.labels.find(label => label.id === this.labelId);
    this.setActiveSince();
    this.showNotification = false;
    Utils.scrollPage();
    this.dataReady = true;
  }

  private setActiveSince() {
    const i = this.label.expenses.length - 1;
    if (i >= 0) {
      this.activeSince = this.getDate(this.label.expenses[i]);
    }
  }

  private getDate(expense: Expense): Date {
    return Utils.localDateToJsDate(expense.createdOn);
  }

  onClickEditLabel() {
    this.router.navigate(['edit/label'], { queryParams: { id: this.labelId } });
  }

  onClickDeleteLabel() {
    this.showModal = true;
    this.modalMessage = this.messages.get(Kind.LABEL_DELETE_WARN);
  }

  onConfirmDeleteLabel() {
    this.dataReady = false;
    this.showModal = false;
    this.showNotification = true;
    this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
    this.rest.deleteLabel(this.labelId).subscribe(() => {
      this.notificationMessage = this.ms.get(Kind.LABEL_DELETE_OK);
      Utils.scrollPage();
    }, err => {
      this.notificationMessage = this.messages.get(Kind.UNEXPECTED_ERROR);
      Utils.scrollPage();
    });
  }

}
