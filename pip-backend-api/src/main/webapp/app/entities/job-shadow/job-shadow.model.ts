import { BaseEntity, User } from './../../shared';

export const enum JobShadowType {
    'INDIVIDUAL',
    'MEDIUM',
    'LARGE'
}

export class JobShadow implements BaseEntity {
    constructor(
        public id?: number,
        public datetime?: any,
        public capacity?: number,
        public jobShadowType?: JobShadowType,
        public transport?: boolean,
        public lunch?: boolean,
        public businessUnit?: BaseEntity,
        public user?: User,
    ) {
        this.transport = false;
        this.lunch = false;
    }
}
