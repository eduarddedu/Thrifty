import { Component, OnInit, AfterViewInit } from '@angular/core';

import { Account } from '../model';
import { AccountService } from '../services/account.service';

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html'
})
export class SidebarComponent implements OnInit, AfterViewInit {

    categories: { id: number, name: string }[];
    labels: { id: number, name: string }[];
    account: Account;

    constructor(protected service: AccountService) { }

    ngOnInit() {
        this.service.dataChanged.subscribe((account: Account) => {
            this.account = account;
            this.refresh();
        });
    }

    ngAfterViewInit() {
        $('#side-menu')['metisMenu']();
    }

    private refresh() {
        if (this.account) {
            this.categories = this.account.categories;
            this.labels = this.account.labels;
        }
    }
}
