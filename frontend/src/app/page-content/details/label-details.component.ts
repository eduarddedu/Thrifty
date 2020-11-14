import { switchMap } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Expense, Account, Label } from '../../model';
import { NotificationService } from '../../services/notification.service';
import { Kind, AppMessage } from '../../model/app-message';
import { Utils } from '../../util/utils';
import { AnalyticsService } from '../../services/analytics.service';
import { DeleteEntityModalService } from '../../services/modal.service';

@Component({
  templateUrl: './z-details.component.html'
})
export class LabelDetailsComponent implements OnInit {

  labelId: number;

  label: Label;

  activeSince: Date;

  dataReady = false;

  viewType = 'label';

  subscription: Subscription;

  constructor(
    private analytics: AnalyticsService,
    private ns: NotificationService,
    private ms: DeleteEntityModalService,
    private route: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit(): void {
    this.route.paramMap.pipe(switchMap(params => {
      this.labelId = +params.get('id');
      return this.analytics.loadAccount();
    })).subscribe(this.init.bind(this), err => {
      this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
    });
  }

  private init(account: Account) {
    this.label = account.labels.find(label => label.id === this.labelId);
    this.setActiveSince();
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
    this.router.navigate(['edit/label', this.labelId]);
  }

  onClickDeleteLabel() {
    this.ms.pushMessage(AppMessage.of(Kind.LABEL_DELETE_WARN));
    this.ms.onConfirmDelete(this.onConfirmDeleteLabel.bind(this));
  }

  onConfirmDeleteLabel() {
    this.router.navigate(['delete/label', this.labelId]);
  }

}
