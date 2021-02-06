export class LocalDate {
    year: number;
    month: number;
    day: number;
    constructor(year: number, month: number, day: number) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    static parse(yyyyMMdd: string): LocalDate {
        const pattern = /(\d{4,})-(\d{2,})-(\d{2,})/;
        const matcher = yyyyMMdd.match(pattern);
        if (matcher) {
            const year = Number.parseInt(matcher[1]);
            const month = Number.parseInt(matcher[2]);
            const day = Number.parseInt(matcher[3]);
            return new LocalDate(year, month, day);
        }
        throw new Error('Cannot parse string [' + yyyyMMdd + '] to LocalDate');
    }

    toString() {
        return `${this.year}`
        + '-' +
        (this.month < 10 ? '0' + `${this.month}` : `${this.month}`)
        + '-' +
        (this.day < 10 ? '0' + `${this.day}` : `${this.day}`);
    }
}
