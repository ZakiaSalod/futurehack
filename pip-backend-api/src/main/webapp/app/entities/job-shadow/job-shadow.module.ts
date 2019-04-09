import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PipSharedModule } from '../../shared';
import { PipAdminModule } from '../../admin/admin.module';
import {
    JobShadowService,
    JobShadowPopupService,
    JobShadowComponent,
    JobShadowDetailComponent,
    JobShadowDialogComponent,
    JobShadowPopupComponent,
    JobShadowDeletePopupComponent,
    JobShadowDeleteDialogComponent,
    jobShadowRoute,
    jobShadowPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobShadowRoute,
    ...jobShadowPopupRoute,
];

@NgModule({
    imports: [
        PipSharedModule,
        PipAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobShadowComponent,
        JobShadowDetailComponent,
        JobShadowDialogComponent,
        JobShadowDeleteDialogComponent,
        JobShadowPopupComponent,
        JobShadowDeletePopupComponent,
    ],
    entryComponents: [
        JobShadowComponent,
        JobShadowDialogComponent,
        JobShadowPopupComponent,
        JobShadowDeleteDialogComponent,
        JobShadowDeletePopupComponent,
    ],
    providers: [
        JobShadowService,
        JobShadowPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PipJobShadowModule {}
