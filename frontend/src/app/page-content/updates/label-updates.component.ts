import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { MessageService, Kind } from '../../services/messages.service';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';
import { UpdatesFormParent } from './updates-form-parent';


@Component({
    templateUrl: './updates-form.component.html'
})
export class LabelUpdatesComponent extends UpdatesFormParent implements OnInit {

    formTitle = 'Update labels';

    constructor(private router: Router, private rs: RestService, private ms: MessageService) {
        super();
    }

    ngOnInit() {
        this.rs.getAccount().subscribe((account: Account) => {
            if (account.labels.length !== 0) {
                this.setRadioSelectorOptions(account);
                this.showForm = true;
            } else {
                this.showNotification = true;
                this.notificationMessage = this.ms.get(Kind.NO_LABELS_ERROR);
            }
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
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
            case 'New' : this.router.navigate(['new/label']); break;
            case 'Edit' : this.router.navigate(['edit/label'], {queryParams: {id: this.checkedOption.id}}); break;
            case 'Delete' :
            this.showModal = true;
            this.modalMessage = this.ms.get(Kind.LABEL_DELETE_WARN);
        }
    }

    onConfirmDelete() {
        this.showForm = false;
        this.showModal = false;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        this.showNotification = true;
        this.rs.deleteLabel(this.checkedOption.id).subscribe(() => {
            this.notificationMessage = this.ms.get(Kind.LABEL_DELETE_OK);
        }, err => {
            this.notificationMessage = this.ms.get(Kind.UNEXPECTED_ERROR);
        });
    }
}
