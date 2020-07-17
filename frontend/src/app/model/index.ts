export interface Account {
    expenses?: Expense[];
    categories?: Category[];
    labels?: Label[];
    other?: Category;
    balance?: number;
    dateRange?: DateRange;
    mapYearBalance?: {
        [key: number]: number;
    };
}

export interface Category {
    id?: number;
    name: string;
    description: string;
    balance?: number;
    expenses?: Expense[];
    labels?: Label[];
    dateRange?: DateRange;
    mapYearBalance?: {
        [key: number]: number;
    };
}

export interface Label {
    id?: number;
    name: string;
}

export interface Expense {
    id?: number;
    createdOn: LocalDate;
    description: string;
    amount: number;
    labels?: Label[];
    category: Category;
}

export interface LocalDate {
    year: number;
    month: number;
    day: number;
}

export interface DateRange {
    startDate: LocalDate;
    endDate: LocalDate;
}

export interface CheckLabel  {
    id: number;
    name: string;
    checked: boolean;
}

export interface RadioOption {
    id: number;
    name: string;
    checked: boolean;
}
