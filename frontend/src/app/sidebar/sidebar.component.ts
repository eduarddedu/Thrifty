import { Component, OnInit, AfterViewInit } from '@angular/core';

import { Account } from '../model';
import { AnalyticsService } from '../services/analytics.service';

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html'
})
export class SidebarComponent implements OnInit, AfterViewInit {

    categories: { id: number, name: string }[];
    labels: { id: number, name: string }[];
    account: Account;

    constructor(protected analytics: AnalyticsService) { }

    ngOnInit() {
        this.analytics.dataChanged.subscribe((account: Account) => {
            this.account = account;
            this.resetNavLinks();
        });
    }

    ngAfterViewInit() {
        $('#side-menu')['metisMenu']();
    }

    resetNavLinks() {
        if (this.account) {
            this.categories = this.account.categories.map(c => ({ id: c.id, name: c.name }));
            this.labels = this.account.labels.map(l => ({ id: l.id, name: l.name }));
        }
    }
}
