import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { NotificationService } from '../../services/notification.service';
import { RestService } from '../../services/rest.service';
import { Kind, AppMessage } from '../../model/app-message';
import { Utils } from '../../util/utils';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-delete-entity',
  template: ``,
  styles: []
})
export class DeleteEntityComponent implements OnInit {
  id: number;
  entity: string;

  constructor(
    private route: ActivatedRoute,
    private ns: NotificationService,
    private rest: RestService,
    private analytics: AccountService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.id = +params.get('id');
      this.entity = params.get('entity');
      this.deleteEntity();
    });
  }

  private deleteEntity() {
    this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
    let deleteEntity: Observable<any>;
    let successMessage: AppMessage;
    switch (this.entity) {
      case 'expense':
        deleteEntity = this.rest.deleteExpense(this.id);
        successMessage = AppMessage.of(Kind.EXPENSE_DELETE_OK);
        break;
      case 'category':
        deleteEntity = this.rest.deleteCategory(this.id);
        successMessage = AppMessage.of(Kind.CATEGORY_DELETE_OK);
        break;
      case 'label':
        deleteEntity = this.rest.deleteLabel(this.id);
        successMessage = AppMessage.of(Kind.LABEL_DELETE_OK);
    }
    deleteEntity.subscribe(() => {
      this.analytics.reload();
      this.ns.push(successMessage);
      Utils.scrollPage();
    }, err => {
      this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR));
      Utils.scrollPage();
    });
  }
}

