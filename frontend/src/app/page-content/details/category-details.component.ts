import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Category, Account, Expense } from '../../model';
import { RestService } from '../../services/rest.service';
import { MessageService, Kind, Message } from '../../services/messages.service';
import { Utils } from '../../util/utils';
import { AnalyticsService } from '../../services/analytics.service';


@Component({
    templateUrl: './details.component.html',
    styles: [`button {margin-bottom: 7px}`]
})
export class CategoryDetailsComponent implements OnInit {

    category: Category;

    viewType = 'category';

    deleteActionConcernsCategory = true;

    categoryId: number;

    activeSince: Date;

    selectedExpenseId: number;

    showNotification = false;

    showModal = false;

    notificationMessage: Message;

    modalMessage: Message;

    dataReady = false;

    constructor(
        private rest: RestService,
        private analytics: AnalyticsService,
        private messages: MessageService,
        private route: ActivatedRoute,
        private router: Router) {
    }

    ngOnInit(): void {
        this.route.paramMap.pipe(switchMap(params => {
            this.categoryId = +params.get('id');
            return this.analytics.loadAccount();
        })).subscribe(this.init.bind(this), err => {
            this.showNotification = true;
            this.notificationMessage = this.messages.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    private init(account: Account) {
        this.category = account.categories.find(c => c.id === this.categoryId);
        this.setActiveSince();
        this.resetNotification();
        Utils.scrollPage();
        this.dataReady = true;
    }

    private resetNotification() {
        this.showNotification = false;
    }

    private setActiveSince() {
        const i = this.category.expenses.length - 1;
        if (i >= 0) {
            this.activeSince = this.getDate(this.category.expenses[i]);
        }
    }

    private getDate(expense: Expense): Date {
        return Utils.localDateToJsDate(expense.createdOn);
    }

    onClickDeleteCategory() {
        this.modalMessage = this.messages.get(Kind.CATEGORY_DELETE_WARN);
        this.deleteActionConcernsCategory = true;
        this.showModal = true;
    }

    onClickEditCategory() {
        this.router.navigate(['edit/category'], { queryParams: { id: this.categoryId } });
    }


    onClickDeleteExpense(id: number) {
        this.selectedExpenseId = id;
        this.deleteActionConcernsCategory = false;
        this.modalMessage = this.messages.get(Kind.EXPENSE_DELETE_WARN);
        this.showModal = true;
    }

    onConfirmDelete() {
        this.dataReady = false;
        this.showModal = false;
        this.showNotification = true;
        this.notificationMessage = this.messages.get(Kind.IN_PROGRESS);
        let action: Observable<any>, successMessage: Message;
        if (this.deleteActionConcernsCategory) {
            action = this.rest.deleteCategory(this.category.id);
            successMessage = this.messages.get(Kind.CATEGORY_DELETE_OK);
        } else {
            action = this.rest.deleteExpense(this.selectedExpenseId);
            successMessage = this.messages.get(Kind.EXPENSE_DELETE_OK);
        }
        action.subscribe(() => {
            this.notificationMessage = successMessage;
            Utils.scrollPage();
        }, err => {
            this.notificationMessage = this.messages.get(Kind.UNEXPECTED_ERROR);
            Utils.scrollPage();
        });
    }
}
