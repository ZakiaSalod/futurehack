import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JobOpportunityComponent } from './job-opportunity.component';
import { JobOpportunityDetailComponent } from './job-opportunity-detail.component';
import { JobOpportunityPopupComponent } from './job-opportunity-dialog.component';
import { JobOpportunityDeletePopupComponent } from './job-opportunity-delete-dialog.component';

export const jobOpportunityRoute: Routes = [
    {
        path: 'job-opportunity',
        component: JobOpportunityComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.jobOpportunity.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-opportunity/:id',
        component: JobOpportunityDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.jobOpportunity.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobOpportunityPopupRoute: Routes = [
    {
        path: 'job-opportunity-new',
        component: JobOpportunityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.jobOpportunity.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-opportunity/:id/edit',
        component: JobOpportunityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.jobOpportunity.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-opportunity/:id/delete',
        component: JobOpportunityDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.jobOpportunity.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
