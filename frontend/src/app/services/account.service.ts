import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs/index';

import { RestService } from './rest.service';
import { Account, AccountData } from '../model';
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
            return this.rest.getAccount().pipe(
                    switchMap(data => {
                        this.setAccount(data);
                        this.subject.next(this.account);
                        return of(this.account);
                    }));
        }
    }

    public reload() {
        this.account = null;
        this.loadAccount().subscribe(() => console.log('Reload account...'));
    }

    private setAccount(data: AccountData) {
        try {
            this.account = new Account(data);
            Utils.deepFreeze(this.account);
        } catch (error) {
            console.log('Error building account: ', error);
        }
    }

}
