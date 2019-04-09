import { BaseEntity } from './../../shared';

export class BusinessUnit implements BaseEntity {
    constructor(
        public id?: number,
        public unitName?: string,
        public size?: string,
        public address?: string,
        public jobShadows?: BaseEntity[],
        public opportunities?: BaseEntity[],
        public industries?: BaseEntity[],
    ) {
    }
}
