import {
    Component, OnInit, AfterViewInit,
    Input, ViewChild, OnChanges,
} from '@angular/core';
import { Router } from '@angular/router';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

import { Category, Label, Account, Expense } from '../model';
import { Kind, AppMessage } from '../model/app-message';
import { Utils } from '../util/utils';
import { DeleteEntityModalService } from '../services/modal.service';

@Component({
    selector: 'app-expense-table',
    templateUrl: './expense-table.component.html',
    styleUrls: ['./expense-table.component.css']
})


/* Docs:
 * https://datatables.net/
 * https://l-lin.github.io/angular-datatables
 */

export class ExpenseTableComponent implements OnInit, OnChanges, AfterViewInit {

    @Input() entity: Account | Category | Label;

    expenses: Expense[];

    expenseId: number;

    @ViewChild(DataTableDirective)
    datatableElement: DataTableDirective;

    dtOptions;

    searchTerms: Subject<String> = new Subject();

    constructor(private router: Router, private ms: DeleteEntityModalService) {
    }

    ngOnInit() {
        this.expenses = this.entity.expenses.slice();
        this.dtOptions = {
            searching: false,
            lengthChange: false,
            pageLength: 10,
            columns: [
                { title: 'Id' },
                { title: 'Date' },
                { title: 'Details' },
                { title: 'Amount' }
            ],
            data: this.expenses.map(e => [e.id, Utils.localDateToIsoDate(e.createdOn), e.description, e.amount / 100]),
            select: 'single',
            rowCallback: (row: Node, data: any[] | Object, index: number) => {
                $('td', row).unbind('click');
                $('td', row).bind('click', () => {
                    this.onRowSelected(data[0]);
                });
                return row;
            },
            order: [['1', 'desc']]
        };
        this.filterExpenses();
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
                dtInstance.row.add([e.id, Utils.localDateToIsoDate(e.createdOn), e.description, (e.amount / 100).toFixed(2)]);
            }
            dtInstance.draw();
            this.disableUserSelectOnTableCells();
            this.expenseId = null;
        });
    }

    onRowSelected(id: number) {
        this.expenseId = id;
    }


    onClickNewExpense() {
        this.router.navigate(['new/expense']);
    }

    onClickEditExpense() {
        this.router.navigate(['edit/expense', this.expenseId]);
    }

    onClickDeleteExpense() {
        this.ms.pushMessage(AppMessage.of(Kind.EXPENSE_DELETE_WARN));
        this.ms.onConfirmDelete(this.onConfirmDeleteExpense.bind(this));
    }

    onConfirmDeleteExpense() {
        this.router.navigate(['delete/expense', this.expenseId]);
    }

    search(value: string) {
        this.searchTerms.next(value);
    }

    filterExpenses() {
        this.searchTerms.pipe(debounceTime(300), distinctUntilChanged())
        .subscribe((term: string) => {
            this.expenses = this.entity.expenses.filter(e => e.description.toLowerCase().includes(term.toLowerCase()));
            this.redrawTable();
        });
    }

    disableUserSelectOnTableCells() {
        $('td').css('-webkit-user-select', 'none');
        $('td').css('-webkit-touch-callout', 'none');
        $('td').css('-khtml-user-select', 'none');
        $('td').css('-moz-user-select', 'none');
        $('td').css('user-select', 'none');
    }
}
