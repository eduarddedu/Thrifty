import { Component, Input, OnInit, AfterViewInit } from '@angular/core';

import { Account } from '../model';
import { RestService } from '../services/rest.service';

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html'
})
export class SidebarComponent implements OnInit, AfterViewInit {

    links: {id: number, name: string}[];

    constructor(protected rs: RestService) { }

    ngOnInit() {
        this.rs.dataReady.subscribe((account: Account) => {
            if (account) {
                this.setNavigationLinks(account);
            }
        });
    }

    ngAfterViewInit() {
        $('#side-menu')['metisMenu']();
    }

    setNavigationLinks(account: Account) {

        this.links = account.categories.map(c => ({id: c.id, name: c.name}));

        if (account.other) {
            this.links.push({id: account.other.id, name: account.other.name});
        }
    }
}
