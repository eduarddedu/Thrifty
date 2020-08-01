import { Component, ViewChild, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { MessageService, Kind } from '../../services/messages.service';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';
import { UpdatesFormParent } from './updates-form-parent';
import { AnalyticsService } from '../../services/analytics.service';



@Component({
    templateUrl: './updates-form.component.html'
})
export class CategoryUpdatesComponent extends UpdatesFormParent implements OnInit {

    formTitle = 'Update categories';

    constructor(private rs: RestService,
        private router: Router,
        private messages: MessageService,
        private analytics: AnalyticsService) {
        super();
    }

    ngOnInit() {
        this.analytics.loadAccount().subscribe((account: Account) => {
            if (account.categories.length > 0) {
                this.setRadioSelectorOptions(account);
                this.showForm = true;
            }
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.messages.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    private setRadioSelectorOptions(account: Account) {
        account.categories.forEach(category => this.radioOptions.push({
            id: category.id,
            name: category.name,
            checked: false
        }));
    }

    handleSelectedAction() {
        switch (this.formAction) {
            case 'New': this.router.navigate(['new/category']); break;
            case 'Edit': this.router.navigate(['edit/category'], { queryParams: { id: this.checkedOption.id } }); break;
            case 'Delete':
                this.showModal = true;
                this.modalMessage = this.messages.get(Kind.CATEGORY_DELETE_WARN);
        }
    }

    onConfirmDelete() {
        this.showForm = this.showModal = false;
        this.notificationMessage = this.messages.get(Kind.IN_PROGRESS);
        this.showNotification = true;
        this.rs.deleteCategory(this.checkedOption.id).subscribe(() => {
            this.notificationMessage = this.messages.get(Kind.CATEGORY_DELETE_OK);
            this.analytics.reload();
        }, err => {
            this.notificationMessage = this.messages.get(Kind.UNEXPECTED_ERROR);
            this.showNotification = true;
        });
    }
}
