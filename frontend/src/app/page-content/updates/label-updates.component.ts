import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { MessageService, Kind } from '../../services/messages.service';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';
import { UpdatesFormParent } from './updates-form-parent';
import { AnalyticsService } from '../../services/analytics.service';


@Component({
    templateUrl: './updates-form.component.html'
})
export class LabelUpdatesComponent extends UpdatesFormParent implements OnInit {

    formTitle = 'Update labels';

    constructor(private router: Router,
        private rest: RestService,
        private messages: MessageService,
        private analytics: AnalyticsService) {
        super();
    }

    ngOnInit() {
        this.analytics.loadAccount().subscribe((account: Account) => {
            if (account.labels.length !== 0) {
                this.setRadioSelectorOptions(account);
                this.showForm = true;
            }
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.messages.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    private setRadioSelectorOptions(account) {
        account.labels.forEach(label => this.radioOptions.push({
            id: label.id,
            name: label.name,
            checked: false
        }));
    }

    handleSelectedAction() {
        switch (this.formAction) {
            case 'New': this.router.navigate(['new/label']); break;
            case 'Edit': this.router.navigate(['edit/label'], { queryParams: { id: this.checkedOption.id } }); break;
            case 'Delete':
                this.showModal = true;
                this.modalMessage = this.messages.get(Kind.LABEL_DELETE_WARN);
        }
    }

    onConfirmDelete() {
        this.showForm = false;
        this.showModal = false;
        this.notificationMessage = this.messages.get(Kind.IN_PROGRESS);
        this.showNotification = true;
        this.rest.deleteLabel(this.checkedOption.id).subscribe(() => {
            this.notificationMessage = this.messages.get(Kind.LABEL_DELETE_OK);
            this.analytics.reload();
        }, err => {
            this.notificationMessage = this.messages.get(Kind.UNEXPECTED_ERROR);
        });
    }
}
