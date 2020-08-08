import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { DetailsComponentParent } from './details-component-parent';
import { Category, Account, Expense } from '../../model';
import { RestService } from '../../services/rest.service';
import { MessageService, Kind, Message } from '../../services/messages.service';
import { Utils } from '../../util/utils';
import { AnalyticsService } from '../../services/analytics.service';


@Component({
    templateUrl: './details.component.html',
    styles: [`button {margin-bottom: 7px}`]
})
export class CategoryDetailsComponent extends DetailsComponentParent implements OnInit {

    category: Category;

    viewType = 'category';

    deleteActionConcernsCategory = true;

    showEditDeleteCategoryButtons = false;

    constructor(
        private rest: RestService,
        private analytics: AnalyticsService,
        private messages: MessageService,
        private route: ActivatedRoute,
        private router: Router) {
        super();
    }

    ngOnInit(): void {
        this.route.paramMap.pipe(switchMap(params => {
            this.categoryId = +params.get('id');
            return this.analytics.loadAccount();
        })).subscribe(this.resetPageContent.bind(this), err => {
            this.showNotification = true;
            this.notificationMessage = this.messages.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    private resetPageContent(account: Account) {
        this.showEditDeleteCategoryButtons = true;
        this.category = account.categories.find(c => c.id === this.categoryId);
        if (this.category.expenses.length > 0) {
            this.activeSince = this.getEarliestExpenseDate(this.category.expenses);
        }
        this.resetNotification();
        Utils.scrollPage();
        this.dataReady = true;
    }

    private resetNotification() {
        this.showNotification = false;
    }

    private getEarliestExpenseDate(expenses: Expense[]) {
        const earliestLocalDate = expenses.reduce((prevExp, currExp) => prevExp <= currExp ? prevExp : currExp).createdOn;
        return Utils.localDateToJsDate(earliestLocalDate);
    }

    onClickDeleteCategory() {
        this.modalMessage = this.messages.get(Kind.CATEGORY_DELETE_WARN);
        this.deleteActionConcernsCategory = true;
        this.showModal = true;
    }

    onClickEditCategory() {
        this.router.navigate(['edit/category'], { queryParams: { id: this.categoryId } });
    }


    onClickDeleteExpense(selectedId) {
        this.selectedExpenseId = selectedId;
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
