import {
    Component, OnInit, AfterViewInit,
    Input, Output, ViewChild, OnChanges, EventEmitter
} from '@angular/core';
import { Router } from '@angular/router';
import { DataTableDirective } from 'angular-datatables';

import { Expense } from '../model';
import { RestService } from '../services/rest.service';
import { Kind, MessageService } from '../services/messages.service';
import { Utils } from '../util/utils';
import { Message } from '@angular/compiler/src/i18n/i18n_ast';

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

    modalMessage: Message;

    notificationMessage: Message;

    showNotification: Boolean = false;

    constructor(private router: Router, private rs: RestService, protected ms: MessageService) {
    }

    ngOnInit() {
        this.dtOptions = {
            searching: true,
            lengthChange: false,
            pageLength: 10,
            columns: [
                { title: 'Id' },
                { title: 'Date' },
                { title: 'Details' },
                { title: 'Amount' }
            ],
            data: this.expenses.map(e => [e.id, Utils.localDateToIsoDate(e.createdOn), e.description, e.amount]),
            select: 'single',
            rowCallback: (row: Node, data: any[] | Object, index: number) => {
                $('td', row).unbind('click');
                $('td', row).bind('click', () => {
                    this.onRowSelected(data);
                });
                return row;
            },
            order: [['1', 'desc']]
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
            for (const e of this.expenses) {
                dtInstance.row.add([e.id, Utils.localDateToIsoDate(e.createdOn), e.description, e.amount]);
            }
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
        this.router.navigate(['new/expense']);
    }

    onClickEditExpense() {
        this.router.navigate(['edit/expense'], { queryParams: { id: this.expenseId } });
    }

    onClickDeleteExpense() {
        this.modalMessage = this.ms.get(Kind.EXPENSE_DELETE_WARN);
        this.showModal = true;
    }

    onConfirmDelete() {
        this.showModal = false;
        this.showNotification = true;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        this.rs.deleteExpense(this.expenseId).subscribe(() => {
            Utils.scrollPage();
            this.notificationMessage = this.ms.get(Kind.EXPENSE_DELETE_OK);
        });

    }
}
