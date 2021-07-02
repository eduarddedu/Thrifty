import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { RestService } from '../../../services/rest.service';
import { Account } from '../../../model';
import { CategoryForm } from './category-form';
import { AccountService } from '../../../services/account.service';


@Component({
    templateUrl: './category-form.component.html',
})
export class CategoryCreateComponent extends CategoryForm implements OnInit {

    pageTitle = 'Create Category';

    constructor(protected fb: FormBuilder,
        private rest: RestService,
        private ns: NotificationService,
        private accountService: AccountService) {
        super(fb);
    }

    ngOnInit() {
        this.accountService.loadAccount().subscribe((account: Account) => {
            this.forbiddenNames = account.categories.map(c => c.name);
            this.setRadioSelectorOptions(account);
            this.createForm();
            this.showForm = true;
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    private setRadioSelectorOptions(account: Account) {
        account.labels.forEach(label => this.radioOptions.push({
            id: label.id,
            name: label.name,
            checked: false
        }));
    }

    onSubmit() {
        this.showForm = false;
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        const category = Object.assign(this.readFormData(), { labels: this.selectedLabels });
        this.rest.createCategory(category).subscribe(() => {
            this.ns.push(AppMessage.of(Kind.CATEGORY_CREATE_OK));
            this.accountService.reload();
        },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }
}
