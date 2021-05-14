export class Choice {
    constructor(
        public id: number,
        public names: string[],
        public weight: number = 0
    ) {
    }
}
