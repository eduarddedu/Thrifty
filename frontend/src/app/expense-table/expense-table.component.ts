import {
    Component, OnInit, AfterViewInit,
    Input, ViewChild, OnChanges,
} from '@angular/core';
import { Router } from '@angular/router';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

import { Expense } from '../model';
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

    @Input() data: Expense[];

    expenseId: number;

    @ViewChild(DataTableDirective)
    datatableElement: DataTableDirective;

    dtOptions = {
        searching: false,
        lengthChange: false,
        pageLength: 10,
        columns: [
            { title: 'Id' },
            { title: 'Date' },
            { title: 'Details' },
            { title: 'Amount' }
        ],
        data: [],
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

    searchTerms: Subject<String> = new Subject();

    constructor(private router: Router, private ms: DeleteEntityModalService) { }

    ngOnInit() {
        this.redrawTable(this.data);
        this.searchTerms.pipe(debounceTime(200), distinctUntilChanged())
            .subscribe((term: string) => {
                let expenses: Expense[];
                if (term.length === 0) {
                    expenses = this.data;
                } else {
                    expenses = this.data.filter(e => e.description.toLowerCase().includes(term.toLowerCase()));
                }
                this.redrawTable(expenses);
            });
    }

    ngAfterViewInit() {
        this.redrawTable(this.data);
    }

    ngOnChanges() {
        this.redrawTable(this.data);
    }

    redrawTable(expenses: Expense[]) {
        if (this.datatableElement.dtInstance) {
            this.datatableElement.dtInstance.then((dtInstance: DataTables.Api) => {
                dtInstance.clear();
                for (const e of expenses) {
                    dtInstance.row.add(this.toRowDataObject(e));
                }
                dtInstance.draw();
                this.disableUserSelect();
                this.expenseId = null;
            });
        }
    }

    toRowDataObject(e: Expense) {
        return [e.id, Utils.localDateToIsoDate(e.createdOn), e.description, (e.amount / 100).toFixed(2)];
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

    disableUserSelect() {
        const properties = {
            '-webkit-user-select': 'none',
            '-webkit-touch-callout': 'none',
            '-khtml-user-select': 'none',
            '-moz-user-select': 'none',
            'user-select': 'none'
        };
        $('td').css(properties);
    }
}
