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

    categoryId: number;

    activeSince: Date;

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

    onClickEditCategory() {
        this.router.navigate(['edit/category'], { queryParams: { id: this.categoryId } });
    }

    onClickDeleteCategory() {
        this.showModal = true;
        this.modalMessage = this.messages.get(Kind.CATEGORY_DELETE_WARN);
    }

    onConfirmDeleteCategory() {
        this.dataReady = false;
        this.showModal = false;
        this.showNotification = true;
        this.notificationMessage = this.messages.get(Kind.IN_PROGRESS);
        this.rest.deleteCategory(this.category.id).subscribe(() => {
            this.notificationMessage = this.messages.get(Kind.CATEGORY_DELETE_OK);
            Utils.scrollPage();
        }, err => {
            this.notificationMessage = this.messages.get(Kind.UNEXPECTED_ERROR);
            Utils.scrollPage();
        });
    }

}
