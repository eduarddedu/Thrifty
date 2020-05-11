import { Component, OnInit, AfterViewInit,
    Input, Output, ViewChild, OnChanges, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { DataTableDirective } from 'angular-datatables';

import { Expense } from '../model';
import { RestService } from '../services/rest.service';
import { MessageService } from '../services/messages.service';
import { Utils } from '../util/utils';

@Component({
    selector: 'app-expense-table',
    templateUrl: './expense-table.component.html',
    styles: ['button { margin-left: 3px }']
})


/* Docs:
 * https://datatables.net/
 * https://l-lin.github.io/angular-datatables
 */

export class ExpenseTableComponent implements OnInit, OnChanges, AfterViewInit {

    @Input() expenses: Expense[];

    @Input() categoryId: number;

    @Output() clickDeleteExpense$: EventEmitter<any> = new EventEmitter();

    expenseId: number;

    @ViewChild(DataTableDirective)
    datatableElement: DataTableDirective;

    dtOptions;

    showModal = false;

    constructor(private router: Router, private rs: RestService, protected ms: MessageService) {
    }

    ngOnInit() {
        this.dtOptions = {
            searching: false,
            lengthChange: false,
            pageLength: 10,
            columns: [
                { title: 'ID' },
                { title: 'Date' },
                { title: 'Description' },
                { title: 'Amount' }
            ],
            data: this.expenses.map(e => [e.id, Utils.localDateToIsoDate(e.date), e.description, e.amount]),
            select: 'single',
            rowCallback: (row: Node, data: any[] | Object, index: number) => {
                $('td', row).unbind('click');
                $('td', row).bind('click', () => {
                    this.onRowSelected(data);
                });
                return row;
            }
        };
    }

    ngAfterViewInit() {
        this.redrawTable();
    }


    ngOnChanges() {
        this.redrawTable();
    }

    redrawTable() {
        if (!this.datatableElement.dtInstance) {
            return;
        }
        this.datatableElement.dtInstance.then((dtInstance: DataTables.Api) => {
            dtInstance.clear();
            this.expenses.forEach(e =>
                dtInstance.row.add([e.id, Utils.localDateToIsoDate(e.date), e.description, e.amount]));
            dtInstance.draw();
            this.disableTextSelectionOnTableElements();
            this.expenseId = null;
        });
    }

    disableTextSelectionOnTableElements() {
        $('td').css('-webkit-user-select', 'none');
        $('td').css('-webkit-touch-callout', 'none');
        $('td').css('-khtml-user-select', 'none');
        $('td').css('-moz-user-select', 'none');
        $('td').css('user-select', 'none');
    }


    onRowSelected(data) {
        this.expenseId = data[0];
    }


    onClickNewExpense() {
        this.router.navigate(['new/expense'], { queryParams: { categoryId: this.categoryId }});
    }

    onClickEditExpense() {
        this.router.navigate(['edit/expense'], { queryParams: { expenseId: this.expenseId }});
    }

    onClickDeleteExpense() {
        this.clickDeleteExpense$.emit(this.expenseId);
    }
}
