import { switchMap } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Category, Account, Expense } from '../../model';
import { Kind, AppMessage } from '../../model/app-message';
import { NotificationService } from '../../services/notification.service';
import { AnalyticsService } from '../../services/analytics.service';
import { DeleteEntityModalService } from '../../services/modal.service';
import { Utils } from '../../util/utils';


@Component({
    templateUrl: './z-details.component.html',
    styles: [`button {margin-bottom: 7px}`]
})
export class CategoryDetailsComponent implements OnInit {

    category: Category;

    viewType = 'category';

    categoryId: number;

    activeSince: Date;

    dataReady = false;

    constructor(
        private analytics: AnalyticsService,
        private ns: NotificationService,
        private ms: DeleteEntityModalService,
        private route: ActivatedRoute,
        private router: Router) {
    }

    ngOnInit(): void {
        this.route.paramMap.pipe(switchMap(params => {
            this.categoryId = +params.get('id');
            return this.analytics.loadAccount();
        })).subscribe(this.init.bind(this), err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    private init(account: Account) {
        this.category = account.categories.find(c => c.id === this.categoryId);
        this.setActiveSince();
        this.dataReady = true;
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
        this.router.navigate(['edit/category', this.categoryId]);
    }

    onClickDeleteCategory() {
        this.ms.pushMessage(AppMessage.of(Kind.CATEGORY_DELETE_WARN));
        this.ms.onConfirmDelete(this.onConfirmDeleteCategory.bind(this));
    }

    onConfirmDeleteCategory() {
        this.router.navigate(['delete/category', this.categoryId]);
    }

}
