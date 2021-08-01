import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { of, zip } from 'rxjs';

import { RestService } from './rest.service';
import { Account, AccountData, ExpenseData } from '../model';
import { Utils } from '../util/utils';

@Injectable()
export class AccountService {
    private account: Account;

    private subject: Subject<Account> = new Subject();

    public dataChanged = this.subject.asObservable();

    public constructor(private rest: RestService) { }

    public loadAccount(): Observable<Account> {
        if (this.account) {
            return of(this.account);
        } else {
            return zip(this.rest.getAccount(), this.rest.getExpenses()).pipe(
                    switchMap(data => {
                        this.setAccount(data[0], data[1]);
                        this.subject.next(this.account);
                        return of(this.account);
                    }));
        }
    }

    public reload() {
        this.account = null;
        this.loadAccount().subscribe(() => console.log('Reload account...'));
    }

    private setAccount(accountData: AccountData, expenseDatas: ExpenseData[]) {
        try {
            this.account = new Account(accountData, expenseDatas);
            Utils.deepFreeze(this.account);
        } catch (error) {
            console.log('Error building account: ', error);
        }
    }

}
