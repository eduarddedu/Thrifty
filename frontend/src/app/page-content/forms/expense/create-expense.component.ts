import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { RestService } from '../../../services/rest.service';
import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { ExpenseData, Account, Category } from '../../../model';
import { ExpenseForm } from './expense-form';
import { AccountService } from '../../../services/account.service';


@Component({
    templateUrl: './expense-form.component.html',
    styleUrls: ['./select.css']
})
export class CreateExpenseComponent extends ExpenseForm implements OnInit {
    pageTitle = 'Create Expense';
    submitFormButtonText = 'Save';
    account: Account;

    constructor(
        protected fb: FormBuilder,
        private ns: NotificationService,
        private rest: RestService,
        private accountService: AccountService) {
        super(fb);
    }

    ngOnInit() {
        this.accountService.loadAccount().subscribe(v => {
            this.account = v;
            if (this.account.categories.length === 0) {
                this.ns.push(AppMessage.of(Kind.MUST_CREATE_CATEGORY));
            } else {
                this.setLabelOptions();
                this.setDefaultCategoryOption();
                this.showForm = true;
            }
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    onSubmit() {
        this.showForm = false;
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        const expense: ExpenseData = Object.assign(this.readFormData(), {
            accountId: this.account.id,
            labels: this.selectedLabels,
            category: {
                id: this.selectedCategory.id,
                name: this.selectedCategory.name,
                description: this.selectedCategory.description
            }
        });
        this.rest.createExpense(expense).subscribe(
            () => {
                this.ns.push(AppMessage.of(Kind.EXPENSE_CREATE_OK));
                this.accountService.reload();
            },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }

    private setDefaultCategoryOption() {
        this.form.patchValue({ category: this.findLargestCategory().name });
    }

    private findLargestCategory(): Category {
        return this.account.categories.slice().sort((a, b) => a.expenses.length - b.expenses.length)
            .reverse()[0];
    }

    private get selectedCategory(): Category {
        return this.account.categories.find(c => c.name === this.form.get('category').value);
    }

    private onSelectCategory(event: any) {
        this.form.patchValue({ category: event.target.value });
    }

    private setLabelOptions() {
        this.account.labels.forEach(label => this.radioOptionsLabel.push({
            id: label.id,
            name: label.name,
            accountId: label.id,
            checked: false
        }));
    }
}
