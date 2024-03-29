import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { RestService } from '../../../services/rest.service';
import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { ExpenseData, AccountData, CategoryData } from '../../../model';
import { ExpenseForm } from './expense-form';
import { DataService } from '../../../services/data.service';
import { Utils } from '../../../util/utils';


@Component({
    templateUrl: './expense-form.component.html',
    styleUrls: ['./expense-form.component.css']
})
export class CreateExpenseComponent extends ExpenseForm implements OnInit {
    pageTitle = 'Create Expense';
    submitFormButtonText = 'Save';
    account: AccountData;

    constructor(
        protected fb: FormBuilder,
        private ns: NotificationService,
        private rest: RestService,
        private ds: DataService) {
        super(fb);
    }

    ngOnInit() {
        this.rest.getAccount().subscribe(v => {
            this.account = v;
            const byName = (a: { name: string; }, b: { name: any; }) => a.name.localeCompare(b.name);
            this.account.categories.sort(byName);
            this.account.labels.sort(byName);
            if (this.account.categories.length === 0) {
                this.ns.push(AppMessage.of(Kind.MUST_CREATE_CATEGORY));
            } else {
                this.setLabelOptions();
                this.setCategoryDropdownPreselectedValue();
                this.showSpinner = false;
                this.showForm = true;
                this.ds.reload();
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
                description: this.selectedCategory.description,
                accountId: this.account.id
            }
        });
        this.rest.createExpense(expense).subscribe(
            () => {
                Utils.scrollPage();
                this.ns.push(AppMessage.of(Kind.EXPENSE_CREATE_OK));
                this.ds.reload();
            },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }

    private setCategoryDropdownPreselectedValue() {
        this.form.patchValue({ category: this.account.categories[0].name });
    }

    private get selectedCategory(): CategoryData {
        return this.account.categories.find(c => c.name === this.form.get('category').value);
    }

    private onSelectCategory(event: any) {
        this.form.patchValue({ category: event.target.value });
    }

    private setLabelOptions() {
        this.radioOptionsLabel = [];
        this.account.labels.forEach(label => this.radioOptionsLabel.push({
            id: label.id,
            name: label.name,
            accountId: label.id,
            checked: false
        }));
    }
}
