import { Component, ViewChild, OnInit, AfterViewInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { MessageService, Kind } from '../../services/messages.service';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';
import { CategoryFormParent } from './category-form-parent';


@Component({
    templateUrl: './category-form.component.html',
})
export class CategoryCreateComponent extends CategoryFormParent implements OnInit {

    pageTitle = 'New category';

    constructor(protected fb: FormBuilder, private rs: RestService, private ms: MessageService) {
        super(fb);
    }

    ngOnInit() {
        this.rs.getAccount().subscribe((account: Account) => {
            this.forbiddenNames = account.categories.map(c => c.name);
            this.setRadioSelectorOptions(account);
            this.createForm();
            this.showForm = true;
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
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
        this.showNotification = true;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        const category = Object.assign(this.readFormData(), { labels: this.selectedLabels });
        this.rs.createCategory(category).subscribe(() =>
            this.notificationMessage = this.ms.get(Kind.CATEGORY_CREATE_OK),
            err => this.notificationMessage = this.ms.get(Kind.UNEXPECTED_ERROR));
    }
}
